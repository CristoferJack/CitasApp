package com.example.mdtk.citasapp.cita;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.proveedor.Contrato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;

public class CitaPendienteListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {


    public CitaPendienteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    int id_trabajador_registro = 0;
    int day = 0;
    int month = 0;
    int year = 0;
    CitaCursorAdapter mAdapter;
    LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    ActionMode actionMode;
    View viewSeleccionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cita_pendiente_list, container, false);


        id_trabajador_registro = getDefault(getActivity().getContentResolver());

        Calendar fechaActual = Calendar.getInstance();
        day= fechaActual.get(Calendar.DATE);
        month= fechaActual.get(Calendar.MONTH);
        year=fechaActual.get(Calendar.YEAR);
        mAdapter = new CitaCursorAdapter(getActivity());
        setListAdapter(mAdapter);

        return v;
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
        String empleadoSelection = " AND "+Contrato.Cita.ID_TRABAJADOR +" = '"+ id_trabajador_registro +"'";
        String selection = Contrato.Cita.FECHA_HORA+" > strftime('%Y-%m-%d %H:%M', datetime('now','localtime')) AND "+ Contrato.Cita.FECHA_HORA+" < strftime('%Y-%m-%d 23:59', datetime('now','localtime')) AND " +
                "estado ='" + G.ESTADO_REGISTRADA +"' "+ empleadoSelection;
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
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}
