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
import android.util.Log;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import tesis.frutas.com.appfrutas.Adapters.AdapterSpinner;
import tesis.frutas.com.appfrutas.Adapters.RecetasAdapter;
import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.clases.Receta;
import tesis.frutas.com.appfrutas.utils.Utils;


public class FragmentRecetas extends Fragment implements AdapterView.OnItemSelectedListener{


    private static final String TAG = FragmentRecetas.class.getSimpleName();

    RecyclerView recyclerView;
    RecetasAdapter rcAdapter;

    private String[] mLocations;

    private String estado_lista = "";

    private static List<Fruta> list_frutas;
    private List<Fruta> nuevaLista;
    private List<Fruta> valores;

    AlertDialog alertDialog;

    Spinner spinnerfruta;
    Long idfruta;
    EditText nombre, ingredientes, procedimiento;
    Button agregar_receta;
    FloatingActionButton fab;
    boolean activo;
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
        list_frutas = Select.from(Fruta.class)
                .orderBy("NOMBRE ASC")
                .list();
        rcAdapter = new RecetasAdapter(getContext(), list_frutas);


        //attach adapter to recyclerview
        recyclerView.setAdapter(rcAdapter);

        fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getContext(), "Cdsfsfdsfds", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setCancelable(false);
                View view_dialog = getLayoutInflater().inflate(R.layout.dialog_receta, null);
                alertDialogBuilder.setView(view_dialog);
                alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();


                // Spinner element
                spinnerfruta = (Spinner) view_dialog.findViewById(R.id.spFruta);
                // Loading spinner data from database
                loadSpinnerData();

                TextView txtclose = view_dialog.findViewById(R.id.txtclose);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                nombre = view_dialog.findViewById(R.id.input_receta);
                ingredientes = view_dialog.findViewById(R.id.ingredientes_txt);
                procedimiento = view_dialog.findViewById(R.id.procedimiento_txt);

                agregar_receta = view_dialog.findViewById(R.id.agregar_receta);

                agregar_receta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validate()) {
                            onSignupFailed();
                            return;
                        }


                        List<Fruta> frutas = Fruta.find(Fruta.class, "nombre = ?", nombre.getText().toString());
                        if (frutas.size() > 0) {
                            Toast.makeText(getActivity().getBaseContext(), "La receta ya existe!", Toast.LENGTH_LONG).show();
                            agregar_receta.setEnabled(true);
                        }else{
                            String _nombre = nombre.getText().toString();
                            String _ingredientes = ingredientes.getText().toString();
                            String _procedimiento = procedimiento.getText().toString();

                            Receta receta = new Receta();
                            receta.setIdfruta(idfruta);
                            receta.setNombre(_nombre);
                            receta.setIngredientes(_ingredientes);
                            receta.setProcedimiento(_procedimiento);

                            receta.save();

                            alertDialog.dismiss();

                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Exito!")
                                    .setContentText("Receta Creada")
                                    .show();
                        }
                    }
                });

            }
        });


        spinner = (Spinner) getActivity().findViewById(R.id.spinner_nav);
        spinner.setOnItemSelectedListener(this);

        return view;
    }
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {

        // Spinner Drop down elements
        final List<Fruta> lables = Select.from(Fruta.class)
                .orderBy("NOMBRE ASC")
                .list();
        ArrayList<String> options=new ArrayList<String>();

        options.add("- SELECCIONE UNA FRUTA -");
        for(Fruta fruta: lables){
            options.add(fruta.getNombre());
        }
        final List<String> frutasList = new ArrayList<>(options);
// Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_frutas,frutasList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

//        // Drop down layout style - list view with radio button
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_frutas);
//
//        // attaching data adapter to spinner
        spinnerfruta.setAdapter(spinnerArrayAdapter);

        spinnerfruta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
//                    Toast.makeText(getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    Fruta objfrutaMENOS = lables.get(position -1);
//                    Log.e(TAG, "objfrutaMENOS : " + objfrutaMENOS.getNombre());
//                    Log.e(TAG, "objfrutaMENOS : " + objfrutaMENOS.getId());
                    idfruta = objfrutaMENOS.getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    public void onSignupFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Llene los datos correctamente", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String _nombre = nombre.getText().toString();
        String _ingredientes = ingredientes.getText().toString();
        String _procedimiento = procedimiento.getText().toString();

        if (_nombre.isEmpty() || _nombre.length() < 2) {
            nombre.setError("Al menos 2 caracteres");
            valid = false;
        } else {
            nombre.setError(null);
        }

        if (_ingredientes.isEmpty()) {
            ingredientes.setError("Al menos 1 caracteres");
            valid = false;
        } else {
            ingredientes.setError(null);
        }
        if (_procedimiento.isEmpty()) {
            procedimiento.setError("Al menos 1 caracteres");
            valid = false;
        } else {
            procedimiento.setError(null);
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
