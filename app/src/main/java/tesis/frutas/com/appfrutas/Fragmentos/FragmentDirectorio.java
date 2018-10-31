package tesis.frutas.com.appfrutas.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import tesis.frutas.com.appfrutas.Adapters.RVAdapter;
import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.clases.Directorio;

public class FragmentDirectorio extends Fragment {

    private List<Directorio> directorios;

    private void inicializarData() {
        directorios = new ArrayList<>();
        directorios.add(new Directorio(
                "Muni. Provincial de San Marcos",
                "Dirección: Jr. Leoncio Prado Nº 360",
                "Teléfono: (076) 558338",
                "Correo: muniprovsanmarcos@hotmail.com",
                "Pág. Web: www.munisanmarcos.gob.pe",
                R.drawable.municipalidad
        ));
        directorios.add(new Directorio(
                "Dir. Regional de Agricultura",
                "Dirección: Av. Cajamarca Nº 610",
                "Teléfono: (076) 558221",
                "Corre: ",
                "Pág. Web:",
                R.drawable.drac
        ));
        directorios.add(new Directorio(
                "Serv. Nacional de Sanidad Agraria",
                "Dirección: Av. Cajamarca Nº 610",
                "Teléfono: ",
                "Corre: ",
                "Pág. Web:",
                R.drawable.senasa
        ));
    }


    public FragmentDirectorio() {
        // Required empty public constructor
    }

    public static FragmentDirectorio newInstance(String param1, String param2) {
        FragmentDirectorio fragment = new FragmentDirectorio();
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
        final View viewRoot =  inflater.inflate(R.layout.fragment_directorio, container, false);

        RecyclerView rv = (RecyclerView)viewRoot.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        inicializarData();
        RVAdapter adapter = new RVAdapter(directorios);
        rv.setAdapter(adapter);

        return viewRoot;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_buscar);
        item.setVisible(false);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_nav);
        spinner.setVisibility(View.GONE);
    }
}
