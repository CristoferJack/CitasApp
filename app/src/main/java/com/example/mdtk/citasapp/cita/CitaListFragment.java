package com.example.mdtk.citasapp.cita;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.pojo.Empleado;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.proveedor.Contrato;
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

import static android.content.ContentValues.TAG;

public class CitaListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	CitaCursorAdapter mAdapter;
	LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	ActionMode actionMode;
	View viewSeleccionado;
	CompactCalendarView calendarView;
	private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
	private SimpleDateFormat simpleDateTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
	int day;
	int month;
	int year;

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
				Intent intent = new Intent(getActivity(), CitaInsertarActivity.class);
				intent.putExtras(date);
				startActivity(intent);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its
	 * instance number.
	 */

	Spinner spnEmpleado;
	int empleadoId = 0;
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
		ArrayList<Empleado> empleadoList = new ArrayList<>();
		empleadoList.add(new Empleado(0, "Todos"));
		empleadoList.add(new Empleado(1, "Celeste"));
		empleadoList.add(new Empleado(2, "Richard"));
		empleadoList.add(new Empleado(3, "Victoria"));
		empleadoList.add(new Empleado(4, "Eloy"));
		ArrayAdapter<Empleado> adapter = new ArrayAdapter<Empleado>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, empleadoList);
		spnEmpleado.setAdapter(adapter);
		spnEmpleado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Empleado empleado = (Empleado) parent.getSelectedItem();
                empleadoId = empleado.getID();
				getLoaderManager().restartLoader(0,null,CitaListFragment.this);

				try {
					DisponibilidadDias(empleadoId);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
		spnEmpleado.setSelection(1);//por defecto CELESTE


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
		//Log.i(LOGTAG, "onActivityCreated");

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

		/*getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				actionMode.finish();
			}
		});*/

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
					int cicloId = (Integer) viewSeleccionado.getTag();
					CitaProveedor.deleteRecord(getActivity().getContentResolver(), cicloId);
					break;
				case R.id.menu_editar:
					Intent intent = new Intent(getActivity(),CitaModificarActivity.class);
					cicloId = (Integer) viewSeleccionado.getTag();
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
				                          Contrato.Cita.EMPLEADO_ID
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
		String empleadoSelection = empleadoId==0?"":" AND "+Contrato.Cita.EMPLEADO_ID+" = '"+empleadoId+"'";
		String selection = Contrato.Cita.FECHA_HORA+" like '"+dia+"%'" + empleadoSelection;
		String orderBy = "datetime("+Contrato.Cita.FECHA_HORA+") ASC";
		return new CursorLoader(getActivity(),
				baseUri, null, selection, null, orderBy);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
		// old cursor once we return.)

		Uri laUriBase = Uri.parse("content://"+Contrato.AUTHORITY+"/Cita");
		//nos suscribimos a la URI osea q cuando cambie o se borre se tiene q enterar y cambiar la inofrmacion de la lista
		data.setNotificationUri(getActivity().getContentResolver(), laUriBase);
		mAdapter.swapCursor(data);
		try {
			DisponibilidadDias(empleadoId);
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

	public class CitaCursorAdapter extends CursorAdapter {
		public CitaCursorAdapter(Context context) {
			super(context, null, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			int ID = cursor.getInt(cursor.getColumnIndex(Contrato.Cita._ID));
			String servicio = cursor.getString(cursor.getColumnIndex(Contrato.Cita.SERVICIO));
			String cliente = cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE));
			String empleado = cursor.getString(cursor.getColumnIndex(Contrato.Cita.EMPLEADO_ID));

			TextView textviewServicio = (TextView) view.findViewById(R.id.textview_ciclo_list_item_servicio);
			textviewServicio.setText(servicio);

			TextView textviewCliente = (TextView) view.findViewById(R.id.textview_ciclo_list_item_cliente);
            textviewCliente.setText(cliente);

			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
			int color = generator.getColor(empleado); //Genera un color según el nombre
			String hora ="";
			try {
				String d = cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA));
				Date dat = simpleDateTime.parse(d);
				hora = simpleTime.format(dat);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			TextDrawable drawable = TextDrawable.builder()
					.beginConfig()
					.fontSize(60) // size of text in pixels
					.endConfig()
					.buildRoundRect(hora.substring(0,5), color,20);

			ImageView image = (ImageView) view.findViewById(R.id.image_view);
			image.setImageDrawable(drawable);

			view.setTag(ID);

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.cita_list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
}
