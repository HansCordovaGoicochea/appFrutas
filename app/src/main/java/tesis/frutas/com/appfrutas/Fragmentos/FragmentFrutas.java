package tesis.frutas.com.appfrutas.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return view;
    }

}
