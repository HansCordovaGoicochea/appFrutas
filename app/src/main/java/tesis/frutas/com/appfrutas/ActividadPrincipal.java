package tesis.frutas.com.appfrutas;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import tesis.frutas.com.appfrutas.Fragmentos.FragmentFrutas;

public class ActividadPrincipal extends AppCompatActivity{

    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private ProgressDialog pd;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);


        pd = ProgressDialog.show(this, "Cargando...", "Porfavor espere...");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView mes_actual = (TextView) headerView.findViewById(R.id.mes_actual);
        mes_actual.setText("OCTUBRE");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (navigationView != null) {
                    prepararDrawer(navigationView);
                    // Seleccionar item por defecto
                    seleccionarItem(navigationView.getMenu().getItem(0));
                }
                pd.dismiss();
            }
        }, 2000); // 3000 milliseconds delay




    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void prepararDrawer(NavigationView navigationView) {


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // Crear nuevo fragmento

                        seleccionarItem(menuItem);
                        drawer.closeDrawer(GravityCompat.START);

                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem item) {

        fragmentManager = getSupportFragmentManager();

        // Marcar item presionado
        item.setChecked(true);

        fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Marcar item presionado
        item.setChecked(true);

        String title = item.getTitle().toString();

        if (id == R.id.frutas_nav) {
            fragment = new FragmentFrutas();
        }

        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
        setTitle(title); // Setear título actual

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_buscar) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
