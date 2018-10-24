package tesis.frutas.com.appfrutas.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import tesis.frutas.com.appfrutas.ActividadPrincipal;
import tesis.frutas.com.appfrutas.R;


public class FragmentCalendar extends Fragment {

    private class monthClickListener implements View.OnClickListener {
        private final int month;

        public monthClickListener(int i) {
            this.month = i;
        }

        public void onClick(View v) {
//            Intent i = new Intent(FragmentCalendar.this.getActivity(), ActividadPrincipal.class);
//            i.putExtra("mes", this.month);
//            FragmentCalendar.this.startActivity(i);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            Toast.makeText(getContext(), "mes"+ this.month,Toast.LENGTH_SHORT).show();
            Fragment fragment = new FragmentFrutas();
            Bundle bundle = new Bundle();
            bundle.putInt("mes", this.month);
            fragment.setArguments(bundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    public FragmentCalendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCalendar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalendar newInstance(String param1, String param2) {
        FragmentCalendar fragment = new FragmentCalendar();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_nav);
        spinner.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        TextView[] textMonthArray = new TextView[12];
        for (int i = 1; i <= 12; i++) {
            textMonthArray[i - 1] = (TextView) root_view.findViewById(getActivity().getResources().getIdentifier("calFragmentMonth" + i, "id", getActivity().getPackageName()));
            textMonthArray[i - 1].setOnClickListener(new monthClickListener(i));

        }

        return  root_view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_buscar);
        item.setVisible(false);
    }
}
