package tesis.frutas.com.appfrutas.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tesis.frutas.com.appfrutas.Adapters.FrutasAdapter;
import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.clases.Fruta;


public class FragmentFrutas extends Fragment {

    public static final String ARG_SECTION_TITLE = "Mis Vehículos";
    private static final String TAG = FragmentFrutas.class.getSimpleName();
    public static final String ARG_SECTION_URL = "";

    RecyclerView recyclerView;
    FrutasAdapter rcAdapter;
    private List<Fruta> list_frutas = Fruta.listAll(Fruta.class);

    AlertDialog alertDialog;

    EditText nombre, descripcion, kcal, proteinas, grasas, carbohidratos;
    Button agregar_fruta;
    Spinner inicio, fin;


    public FragmentFrutas() {
        // Required empty public constructor
    }


    public static FragmentFrutas newInstance(String param1, String param2) {
        FragmentFrutas fragment = new FragmentFrutas();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frutas, container, false);

        //initialize recyclerview
        recyclerView = view.findViewById(R.id.frutas_recycler_view);
        //fixed size of recyclerview layout
        recyclerView.setHasFixedSize(true);
        //initialize linear layout manager horizontally
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        //attach linear layout to recyclerview
        recyclerView.setLayoutManager(linearHorizontal);
        //initialize adapter
        rcAdapter = new FrutasAdapter(getContext(), list_frutas);
        //attach adapter to recyclerview
        recyclerView.setAdapter(rcAdapter);

        FloatingActionButton fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getContext(), "Cdsfsfdsfds", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setCancelable(false);
                View view_dialog = getLayoutInflater().inflate(R.layout.dialog_fruta, null);
                alertDialogBuilder.setView(view_dialog);
                alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                TextView txtclose = view_dialog.findViewById(R.id.txtclose);

                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                nombre = view_dialog.findViewById(R.id.input_nombre);

                agregar_fruta = view_dialog.findViewById(R.id.agregar_fruta);

                agregar_fruta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "Signup");
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //buscador de productos en el recycler
        final MenuItem buscar = menu.findItem(R.id.action_buscar);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchView searchView = (SearchView) buscar.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s = s.toLowerCase();

                List<Fruta> nuevaLista = new ArrayList<>();
                for (Fruta fruta : list_frutas) {
                    String nombre_fruta = fruta.getNombre().toLowerCase();
                    if (nombre_fruta.contains(s)){
                        nuevaLista.add(fruta);
                    }
                }
                rcAdapter.setFilter(nuevaLista);

                return true;
            }
        });
    }
}
