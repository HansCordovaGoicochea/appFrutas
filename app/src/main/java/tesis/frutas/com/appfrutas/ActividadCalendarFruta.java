package tesis.frutas.com.appfrutas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tesis.frutas.com.appfrutas.Adapters.FrutasAdapter;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.utils.Utils;

public class ActividadCalendarFruta extends AppCompatActivity {


    RecyclerView recyclerView;
    FrutasAdapter rcAdapter;

    List<Fruta> valores_beta = Select.from(Fruta.class)
            .orderBy("NOMBRE ASC")
            .list();

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_calendar_fruta);

        SharedPreferences preferences = getSharedPreferences("admin_pref", MODE_PRIVATE);
        preferences.edit()
                .putBoolean("activo_otro", true).apply();

        Integer mes = getIntent().getIntExtra("mes", 0);

        List<Fruta> valores = new ArrayList<>();

        for (Fruta item: valores_beta){
            int primer_mes = (int) item.getDateIni();
            int ultimo_mes = (int) item.getDateEnd();

            if (ultimo_mes < primer_mes) {
                ultimo_mes += 12;
                if (mes < primer_mes) {
                    mes += 12;
                }
            }

            if ((primer_mes == 1 && ultimo_mes == 12) || ((primer_mes < mes && mes < ultimo_mes) || primer_mes == mes || ultimo_mes == mes)) {
                valores.add(item);
            }
        }

        //initialize recyclerview
        recyclerView = findViewById(R.id.frutas_recycler_view);
        //fixed size of recyclerview layout
        recyclerView.setHasFixedSize(true);
        //initialize linear layout manager horizontally
        LinearLayoutManager linearHorizontal = new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL,false);
        //attach linear layout to recyclerview
        recyclerView.setLayoutManager(linearHorizontal);
        //initialize adapter
        rcAdapter = new FrutasAdapter(getApplicationContext(), valores);
        //attach adapter to recyclerview
        recyclerView.setAdapter(rcAdapter);
            recyclerView.setFadingEdgeLength(0);
        rcAdapter.notifyDataSetChanged();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCalendar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(Utils.monthToString((long) mes, this) + " - " + valores.size() + " Frutas");

        fab =  findViewById(R.id.fab);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStart() {
        super.onStart();

    }

    public void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
    @Override
    public void onResume() {
        super.onResume();
        fab.setVisibility(View.INVISIBLE);

    }
}
