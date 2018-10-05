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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
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

                nombre = view_dialog.findViewById(R.id.input_nombre);
                kcal = view_dialog.findViewById(R.id.input_kcal);
                grasas = view_dialog.findViewById(R.id.input_grasas);
                proteinas = view_dialog.findViewById(R.id.input_proteinas);
                carbohidratos = view_dialog.findViewById(R.id.input_carbo);
                descripcion = view_dialog.findViewById(R.id.descripcion_txt);

                final Spinner spinner = view_dialog.findViewById(R.id.primer_mes);
                final Spinner spinner2 = view_dialog.findViewById(R.id.ultimo_mes);

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

                        if (!validate()) {
                            onSignupFailed();
                            return;
                        }
                        List<Fruta> frutas = Fruta.find(Fruta.class, "nombre = ?", nombre.getText().toString());
                        if (frutas.size() > 0) {
                            Toast.makeText(getActivity().getBaseContext(), "La fruta ya existe!", Toast.LENGTH_LONG).show();
                            agregar_fruta.setEnabled(true);
                        }else{
                            agregar_fruta.setEnabled(false);

                            String _nombre = nombre.getText().toString();
                            String _kcal = kcal.getText().toString();
                            String _grasas = grasas.getText().toString();
                            String _proteinas = proteinas.getText().toString();
                            String _carbohidratos = carbohidratos.getText().toString();
                            String _descripcion = descripcion.getText().toString();
                            String selectedPrimero = getResources().getStringArray(R.array.months_number_array)[spinner.getSelectedItemPosition()];
                            String selectedUltimo = getResources().getStringArray(R.array.months_number_array)[spinner2.getSelectedItemPosition()];

                            Fruta fruta = new Fruta();
                            fruta.setNombre(_nombre);
                            fruta.setKcal(_kcal);
                            fruta.setGrasas(_grasas);
                            fruta.setProteinas(_proteinas);
                            fruta.setCarbohidratos(_carbohidratos);
                            fruta.setDescripcion(_descripcion);
                            fruta.setDateIni(Long.parseLong(selectedPrimero));
                            fruta.setDateEnd(Long.parseLong(selectedUltimo));
                            fruta.save();
//                            Log.e(TAG, selectedPrimero);
//                            Log.e(TAG, selectedUltimo);

                            list_frutas.clear();
                            List<Fruta> list_frutas_nuevo = Fruta.listAll(Fruta.class);
                            list_frutas.addAll(list_frutas_nuevo);
                            rcAdapter.notifyDataSetChanged();

                            alertDialog.dismiss();

                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Exito!")
                                    .setContentText("Fruta Creado")
                                    .show();
                        }
                    }
                });
            }
        });

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

        if (_kcal.isEmpty() || _kcal.length() < 2) {
            kcal.setError("Al menos 2 caracteres");
            valid = false;
        } else {
            kcal.setError(null);
        }

        if (_grasas.isEmpty() || _grasas.length() < 2) {
            grasas.setError("Al menos 2 caracteres");
            valid = false;
        } else {
            grasas.setError(null);
        }

        if (_proteinas.isEmpty() || _proteinas.length() < 2) {
            proteinas.setError("Al menos 2 caracteres");
            valid = false;
        } else {
            proteinas.setError(null);
        }

        if (_carbohidratos.isEmpty() || _carbohidratos.length() < 2) {
            carbohidratos.setError("Al menos 2 caracteres");
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
