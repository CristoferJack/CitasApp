package com.example.mdtk.citasapp.cita;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.aplicacion.AppController;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.pojo.Trabajador;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.proveedor.Contrato;
import com.example.mdtk.citasapp.sync.Sincronizacion;
import com.example.mdtk.citasapp.sync.SincronizacionTrabajador;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.mdtk.citasapp.proveedor.TrabajadorProveedor.readAllRecord;
import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;

public class CitaListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	CitaCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	ActionMode actionMode;
	View viewSeleccionado;
	CompactCalendarView calendarView;
	private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
	private SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
	int day;
	int month;
	int year;
	int id_trabajador_registro = 0;
	public static CitaListFragment newInstance() {
		CitaListFragment f = new CitaListFragment();
		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem menuItem = menu.add(Menu.NONE, G.INSERTAR, Menu.NONE, "Insertar");
		menuItem.setIcon(R.drawable.ic_action_registro);
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		MenuItem menuItem2 = menu.add(Menu.NONE, G.SINCRONIZAR, Menu.NONE, "Sincronizar");
		menuItem2.setIcon(R.drawable.ic_action_sincronizar);
		menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case(G.INSERTAR):
				Bundle date = new Bundle();
				date.putInt("Day", day);
				date.putInt("Month", month);
				date.putInt("Year", year);
				date.putInt("id_trabajador_seleccionado", id_trabajador_seleccionado);
				Intent intent = new Intent(getActivity(), CitaInsertarActivity.class);
				intent.putExtras(date);
				startActivity(intent);
				break;
			case(G.SINCRONIZAR):
				final ProgressDialog progresRing = ProgressDialog.show(getActivity(), null, "Sincronizando...", true);
				progresRing.setCancelable(false);

				AppController.getInstance().setSincronizacionTrabajador(new SincronizacionTrabajador(getActivity()));
				AppController.getInstance().getSincronizacionTrabajador().sincronizar();

				AppController.getInstance().setSincronizacion(new Sincronizacion(getActivity()));
				AppController.getInstance().getSincronizacion().sincronizar();

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (Exception e) {
						}
						progresRing.dismiss();
					}
				}).start();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its
	 * instance number.
	 */

	Spinner spnEmpleado;
	int id_trabajador_seleccionado = 0;
	TextView txtMes;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cita_list, container, false);

		Calendar fechaActual = Calendar.getInstance();
		day= fechaActual.get(Calendar.DATE);
		month= fechaActual.get(Calendar.MONTH);
		year=fechaActual.get(Calendar.YEAR);

		mAdapter = new CitaCursorAdapter(getActivity());
		setListAdapter(mAdapter);

		calendarView = v.findViewById(R.id.calendarAgenda);

		spnEmpleado = (Spinner)v.findViewById(R.id.spnEmpleado);
		ArrayList<Trabajador> trabajadorList = new ArrayList<>();
		trabajadorList.add(new Trabajador(0,"Todos",""));
		trabajadorList.addAll(readAllRecord(getActivity().getContentResolver()));
		ArrayAdapter<Trabajador> adapter = new ArrayAdapter<Trabajador>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, trabajadorList);
		spnEmpleado.setAdapter(adapter);
		spnEmpleado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Trabajador trabajador = (Trabajador) parent.getSelectedItem();
                id_trabajador_seleccionado = trabajador.getID();
				getLoaderManager().restartLoader(0,null,CitaListFragment.this);

				try {
					DisponibilidadDias(id_trabajador_seleccionado);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
		id_trabajador_registro = getDefault(getActivity().getContentResolver());
		int posicionEmpleado = 0;
		for(Trabajador e : trabajadorList){
			if(e.getID() == id_trabajador_registro) {
				spnEmpleado.setSelection(posicionEmpleado);
				break;
			}
			posicionEmpleado++;
		}

		//set initial title
		txtMes = (TextView) v.findViewById(R.id.txtMes);
		txtMes.setText(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));

		//set title on calendar scroll
		calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
			@Override
			public void onDayClick(Date dateClicked) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateClicked);
				year= calendar.get(Calendar.YEAR);
				month= calendar.get(Calendar.MONTH);
				day= calendar.get(Calendar.DATE);
				getLoaderManager().restartLoader(0,null,CitaListFragment.this);
			}

			@Override
			public void onMonthScroll(Date firstDayOfNewMonth) {
				txtMes.setText(dateFormatForMonth.format(firstDayOfNewMonth));
			}
		});

		return v;
	}

	private void DisponibilidadDias(int empleadoId) throws ParseException {
		Map<String,List<Cita>> cita = CitaProveedor.disponibilidadByEmpleado(getActivity().getContentResolver(),empleadoId);
		calendarView.removeAllEvents();
		for(Map.Entry<String, List<Cita>> c: cita.entrySet()){
			Calendar cal = Calendar.getInstance();
			cal.setTime(simpleDate.parse(c.getKey()));
			long calLong =  cal.getTimeInMillis() ;
			for(Cita d : c.getValue()){
				Event ev1 = new Event(Color.RED, calLong, null);
				calendarView.addEvent(ev1);
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mCallbacks = this;

		getLoaderManager().initLoader(0, null, mCallbacks);

		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				actionMode = getActivity().startActionMode(mActionModeCallback);
				view.setSelected(true);
				viewSeleccionado = view;
				return false;
			}
		});

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				view.setSelected(true);
				viewSeleccionado = view;
                Intent intent = new Intent(getActivity(),CitaModificarActivity.class);
				String cicloId = (String) viewSeleccionado.getTag();
                intent.putExtra(Contrato.Cita._ID, cicloId);
                startActivity(intent);
			}
		});

	}


	ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
			MenuInflater inflater = actionMode.getMenuInflater();
			inflater.inflate(R.menu.menu_contextual, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
			switch (menuItem.getItemId()){
				case R.id.menu_borrar:
					String cicloId = (String) viewSeleccionado.getTag();
					CitaProveedor.deleteRecordSincronizacion(getActivity().getContentResolver(), cicloId,id_trabajador_registro);
					break;
				case R.id.menu_editar:
					Intent intent = new Intent(getActivity(),CitaModificarActivity.class);
					cicloId = (String) viewSeleccionado.getTag();
					intent.putExtra(Contrato.Cita._ID, cicloId);
					startActivity(intent);
					break;
			}
			actionMode.finish();
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode actionMode) {
			actionMode = null;
		}
	};

	SimpleDateFormat simpleDateBase =  new SimpleDateFormat("yyyy-MM-dd");

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		String columns[] = new String[] { Contrato.Cita._ID,
										  Contrato.Cita.SERVICIO,
				                          Contrato.Cita.CLIENTE,
											Contrato.Cita.FECHA_HORA,
				                          Contrato.Cita.ID_TRABAJADOR
										};

		Uri baseUri = Contrato.Cita.CONTENT_URI;

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		Calendar calFecha = Calendar.getInstance();
		calFecha.clear();
		calFecha.set(Calendar.YEAR, year);
		calFecha.set(Calendar.MONTH, month);
		calFecha.set(Calendar.DATE, day);
		String dia = simpleDateBase.format(calFecha.getTime());
		String empleadoSelection = id_trabajador_seleccionado ==0?"":" AND "+Contrato.Cita.ID_TRABAJADOR +" = '"+ id_trabajador_seleccionado +"'";
		String selection = Contrato.Cita.FECHA_HORA+" like '"+dia+"%' AND estado ='" + G.ESTADO_REGISTRADA +"' "+ empleadoSelection;
		String orderBy = "strftime('%Y-%m-%d %H:%M',"+Contrato.Cita.FECHA_HORA+") ASC";
		return new CursorLoader(getActivity(),
				baseUri, columns, selection, null, orderBy);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)

		Uri laUriBase = Uri.parse("content://"+Contrato.AUTHORITY+"/Cita");
		//nos suscribimos a la URI osea q cuando cambie o se borre se tiene q enterar y cambiar la inofrmacion de la lista
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);
		mAdapter.swapCursor(data);

		try {
			DisponibilidadDias(id_trabajador_seleccionado);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed.  We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}

/*	@Override
	public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
		year= i;
		month= i1;
		day= i2;
		getLoaderManager().restartLoader(0,null,this);
	}*/
}
