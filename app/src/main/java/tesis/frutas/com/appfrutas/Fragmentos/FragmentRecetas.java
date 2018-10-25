package tesis.frutas.com.appfrutas.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tesis.frutas.com.appfrutas.Adapters.AdapterSpinner;
import tesis.frutas.com.appfrutas.Adapters.RecetasAdapter;
import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.utils.Utils;


public class FragmentRecetas extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String ARG_SECTION_TITLE = "";
    private static final String TAG = FragmentRecetas.class.getSimpleName();
    public static final String ARG_SECTION_URL = "";

    RecyclerView recyclerView;
    RecetasAdapter rcAdapter;

    private String[] mLocations;

    private String estado_lista = "";

    private static List<Fruta> list_frutas = Select.from(Fruta.class)
            .orderBy("NOMBRE ASC")
            .list();
    private List<Fruta> nuevaLista;
    private List<Fruta> valores;

    AlertDialog alertDialog;

    EditText nombre, descripcion, kcal, proteinas, grasas, carbohidratos;
    Button agregar_fruta;
    Spinner inicio, fin;
    FloatingActionButton fab;
    boolean activo;
    ImageView imagencargar;
    Spinner spinner;

    public FragmentRecetas() {
        // Required empty public constructor

    }


    public static FragmentRecetas newInstance(String param1, String param2) {
        FragmentRecetas fragment = new FragmentRecetas();
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
        rcAdapter = new RecetasAdapter(getContext(), list_frutas);


        //attach adapter to recyclerview
        recyclerView.setAdapter(rcAdapter);

        fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
            }
        });


        spinner = (Spinner) getActivity().findViewById(R.id.spinner_nav);
        spinner.setOnItemSelectedListener(this);

        return view;
    }


    public void onSignupFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Llene los datos correctamente", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String _nombre = nombre.getText().toString();
        String _kcal = kcal.getText().toString();
        String _grasas = grasas.getText().toString();
        String _proteinas = proteinas.getText().toString();
        String _carbohidratos = carbohidratos.getText().toString();
        String _descripcion = descripcion.getText().toString();

        if (_nombre.isEmpty() || _nombre.length() < 2) {
            nombre.setError("Al menos 2 caracteres");
            valid = false;
        } else {
            nombre.setError(null);
        }

        if (_kcal.isEmpty()) {
            kcal.setError("Al menos 1 caracteres");
            valid = false;
        } else {
            kcal.setError(null);
        }

        if (_grasas.isEmpty()) {
            grasas.setError("Al menos 1 caracteres");
            valid = false;
        } else {
            grasas.setError(null);
        }

        if (_proteinas.isEmpty()) {
            proteinas.setError("Al menos 1 caracteres");
            valid = false;
        } else {
            proteinas.setError(null);
        }

        if (_carbohidratos.isEmpty()) {
            carbohidratos.setError("Al menos 1 caracteres");
            valid = false;
        } else {
            carbohidratos.setError(null);
        }
        if (_descripcion.isEmpty() || _descripcion.length() < 20) {
            descripcion.setError("Al menos 20 caracteres");
            valid = false;
        } else {
            descripcion.setError(null);
        }


        return valid;
    }

    @Override
    public void onResume() {
        super.onResume();
        spinner.setVisibility(View.VISIBLE);
        SharedPreferences preferences = getActivity().getSharedPreferences("admin_pref", Context.MODE_PRIVATE);
        activo = preferences.getBoolean("activo",false);
//        Toast.makeText(getContext(), activo+"",Toast.LENGTH_SHORT).show();
        if (!activo){
            fab.setVisibility(View.INVISIBLE);
            hideItemCerrar();
            showItemIngresar();
        }else{
            hideItemIngresar();
            showItemCerrar();
            fab.setVisibility(View.VISIBLE);
        }
//        Collections.sort(list_frutas);
        Collections.sort(list_frutas, new CustomComparator());
        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(getContext(), ""+spinner.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
        valores = new ArrayList<>();
        List<Fruta> valores_beta = Select.from(Fruta.class)
                .orderBy("NOMBRE ASC")
                .list();
        int mes_actual = Utils.getMonth();

        if (position == 0) {
            valores = Select.from(Fruta.class)
                    .orderBy("NOMBRE ASC")
                    .list();
        } else if (position == 1) {

            for (Fruta item: valores_beta){
                int primer_mes = (int) item.getDateIni();
                int ultimo_mes = (int) item.getDateEnd();

                if (ultimo_mes < primer_mes) {
                    ultimo_mes += 12;
                    if (mes_actual < primer_mes) {
                        mes_actual += 12;
                    }
                }

                if (primer_mes == 1 && ultimo_mes == 12 || ((primer_mes < mes_actual && mes_actual < ultimo_mes) || primer_mes == mes_actual || ultimo_mes == mes_actual)) {
                    valores.add(item);
                }
            }

        } else if (position == 2) {
            mes_actual = (Utils.getMonth() + 1) % 12;

            for (Fruta item: valores_beta){
                int primer_mes = (int) item.getDateIni();
                int ultimo_mes = (int) item.getDateEnd();

                if (!(primer_mes == 1 && ultimo_mes == 12) && (primer_mes == mes_actual || primer_mes == (mes_actual + 1) % 12)) {
                    valores.add(item);
                }
            }
        }
        estado_lista = "Filtro";
//        list_frutas.clear();
        rcAdapter = new RecetasAdapter(getActivity(), valores);
        //attach adapter to recyclerview
        recyclerView.setAdapter(rcAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class CustomComparator implements Comparator<Fruta> {
        @Override
        public int compare(Fruta o1, Fruta o2) {
            return o1.getNombre().compareTo(o2.getNombre());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mLocations = getResources().getStringArray(R.array.spinner_selection_array);
        //poblar spinner
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) item.getActionView();


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.spinner_selection_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        spinner.setAdapter(new AdapterSpinner(getContext(), R.layout.support_simple_spinner_dropdown_item, mLocations,(String) getActivity().getTitle()));

        //buscador de productos en el recycler
        final MenuItem buscar = menu.findItem(R.id.action_buscar);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchView searchView = (SearchView) buscar.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                rcAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s = s.toLowerCase();

//                rcAdapter.getFilter().filter(s);
                if (s.length() > 0){
                    estado_lista = "Buscando";
                }

                nuevaLista = new ArrayList<>();
                for (Fruta fruta : list_frutas) {
                    String nombre_fruta = fruta.getNombre().toLowerCase();
                    if (nombre_fruta.contains(s)){
                        nuevaLista.add(fruta);
                    }
                }

                rcAdapter.setFilter(nuevaLista);


                return false;
            }



        });
    }


    private void hideItemIngresar()
    {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.ingresar).setVisible(false);
    }

    private void hideItemCerrar()
    {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.cerrar).setVisible(false);
    }

    private void showItemIngresar()
    {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.ingresar).setVisible(true);
    }

    private void showItemCerrar()
    {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.cerrar).setVisible(true);
    }

}