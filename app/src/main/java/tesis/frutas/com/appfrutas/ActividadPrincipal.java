package tesis.frutas.com.appfrutas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentCalendar;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentDirectorio;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentFrutas;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentMapa;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentRecetas;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentSalud;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.utils.Utils;

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

        int _mes_actual = Utils.getMonth();
        mes_actual.setText(Utils.monthToString(_mes_actual, getApplicationContext()));

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
        }, 1500); // 3000 milliseconds delay


        // Get the shared preferences
        SharedPreferences preferences = getSharedPreferences("admin_pref", MODE_PRIVATE);

        // inicializar usuario para poder ver los controles CRUD
        preferences.edit()
                .putBoolean("activo", false)
                .putString("claveadmin", "123456").apply();

        final AlertDialog.Builder builder = new AlertDialog.Builder(ActividadPrincipal.this);
        builder
                .setIcon(R.drawable.salir)
                .setTitle("¿Seguro de salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        ImageView imagenSalir = navigationView.findViewById(R.id.img_row_salir);
        imagenSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Get the shared preferences
        SharedPreferences preferences = getSharedPreferences("admin_pref", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit()
                .putBoolean("activo", false).apply();

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

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Marcar item presionado
        item.setChecked(true);

        String title = item.getTitle().toString();
        final SharedPreferences preferences = getSharedPreferences("admin_pref", MODE_PRIVATE);
        if (id == R.id.frutas_nav) {
            fragment = new FragmentFrutas();
//            preferences.edit()
//                    .putBoolean("activo", false).apply();
//            preferences.edit()
//                    .putBoolean("activo_otro", false).apply();
        }
        else if(id == R.id.ingresar){

            final EditText clave_admin = new EditText(getApplicationContext());

            if (SweetAlertDialog.DARK_STYLE) {
                clave_admin.setTextColor(Color.WHITE);
            }

            clave_admin.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            clave_admin.setSelection(clave_admin.getText().length());

            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(clave_admin);
//                                            linearLayout.addView(checkBox);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(linearLayout)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("Admin");
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                                String _clave_admin = clave_admin.getText().toString();
                                if (_clave_admin.isEmpty()) {
                                    clave_admin.setError("Debe escribir la clave");
                                    return;
                                } else {
                                    // Check if onboarding_complete is false
                                    if(preferences.getString("claveadmin",null).equals(_clave_admin)) {
                                        preferences.edit()
                                                .putBoolean("activo", true).apply();

                                        seleccionarItem(navigationView.getMenu().getItem(0));

                                        alertDialog.dismiss();
                                    }else{
                                        clave_admin.setText("");
                                        clave_admin.setError("Error clave incorrecta");
                                    }
                                }
                        }
                    });
                }
            });
            alertDialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            alertDialog.show();
        }else if(id == R.id.cerrar){
            final AlertDialog.Builder builder = new AlertDialog.Builder(ActividadPrincipal.this);
            builder
                    .setIcon(R.drawable.salir)
                    .setTitle("¿Seguro de Cerrar Sesión?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // Get the shared preferences
                            SharedPreferences preferences = getSharedPreferences("admin_pref", MODE_PRIVATE);

                            // Set onboarding_complete to true
                            preferences.edit()
                                    .putBoolean("activo", false).apply();
                            seleccionarItem(navigationView.getMenu().getItem(0));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            alertDialog.show();

        }else if(id == R.id.calendario_nav){
            fragment = new FragmentCalendar();
        }else if(id == R.id.recetas_nav){
            fragment = new FragmentRecetas();
        }else if(id == R.id.salud_nav){
            fragment = new FragmentSalud();
        }else if(id == R.id.directorio_nav){
            fragment = new FragmentDirectorio();
        }else if(id == R.id.mapa_nav){
            fragment = new FragmentMapa();
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

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        int stackCount = fragmentManager.getBackStackEntryCount();
        if( fragmentManager.getFragments() != null ) return fragmentManager.getFragments().get( stackCount > 0 ? stackCount-1 : stackCount );
        else return null;
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
