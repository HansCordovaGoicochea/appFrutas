package tesis.frutas.com.appfrutas;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Normalizer;

import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.utils.Utils;

public class ScrollingActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    Fruta fruta;

    ImageView imagenHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String idfruta = intent.getStringExtra("idfruta");
//        Toast.makeText(getApplicationContext(), idvehiculo.toString(),Toast.LENGTH_SHORT).show();

        fruta = Fruta.findById(Fruta.class, Integer.valueOf(idfruta));

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(fruta.getNombre());

        imagenHead = findViewById(R.id.imagenColapsing);

        ponerImagenDinamicaToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        int mes_actual = Utils.getMonth();
        int primer_mes = (int) fruta.getDateIni();
        int ultimo_mes = (int) fruta.getDateEnd();

        if (ultimo_mes < primer_mes) {
            ultimo_mes += 12;
            if (mes_actual < primer_mes) {
                mes_actual += 12;
            }
        }
        if (primer_mes == 1 && ultimo_mes == 12) {

            //get the drawable
            Drawable myFabSrc = getResources().getDrawable(R.drawable.en_temporada);
            //copy it in a new one
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            //set the color filter, you can use also Mode.SRC_ATOP
            willBeWhite.mutate().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            //set it to your fab button initialized before
            fab.setImageDrawable(willBeWhite);

        } else if (primer_mes < mes_actual && mes_actual < ultimo_mes) {
            //get the drawable
            Drawable myFabSrc = getResources().getDrawable(R.drawable.en_temporada);
            //copy it in a new one
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            //set the color filter, you can use also Mode.SRC_ATOP
            willBeWhite.mutate().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            //set it to your fab button initialized before
            fab.setImageDrawable(willBeWhite);

        } else if (primer_mes == mes_actual) {
            //get the drawable
            Drawable myFabSrc = getResources().getDrawable(R.drawable.en_temporada);
            //copy it in a new one
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
//            set the color filter, you can use also Mode.SRC_ATOP
//            willBeWhite.mutate().setColorFilter(getResources().getColor(R.color.primaryColor), PorterDuff.Mode.MULTIPLY);
            //set it to your fab button initialized before
            fab.setImageDrawable(willBeWhite);

        } else if (ultimo_mes == mes_actual) {
            //get the drawable
            Drawable myFabSrc = getResources().getDrawable(R.drawable.alert_temporada);
            //copy it in a new one
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            //set the color filter, you can use also Mode.SRC_ATOP
//            willBeWhite.mutate().setColorFilter(getResources().getColor(R.color.md_yellow_500), PorterDuff.Mode.MULTIPLY);
            //set it to your fab button initialized before
            fab.setImageDrawable(willBeWhite);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_orange_600)));
        } else {
            //get the drawable
            Drawable myFabSrc = getResources().getDrawable(R.drawable.no_temporada);
            //copy it in a new one
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            //set the color filter, you can use also Mode.SRC_ATOP
//            willBeWhite.mutate().setColorFilter(getResources().getColor(R.color.md_red_400), PorterDuff.Mode.MULTIPLY);
            //set it to your fab button initialized before
            fab.setImageDrawable(willBeWhite);

            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_red_400)));

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == android.R.id.home) {
//            Log.e("dfdsf", "atrasssss");
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ponerImagenDinamicaToolbar(){

        String fileName = Normalizer.normalize(fruta.getNombre().toLowerCase().trim(), Normalizer.Form.NFD).replaceAll(" ", "_").replaceAll("[^\\p{ASCII}]", "");

        Bitmap bitmap = null;

        try{
            ContextWrapper cw = new ContextWrapper(getApplication());
            File dirImages = cw.getDir("frutas", Context.MODE_PRIVATE);
            File myPath = new File(dirImages, fileName + ".png");

            Log.e("ruta", myPath+"");

            FileInputStream fileInputStream =
                    new FileInputStream(myPath);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        }catch (IOException io){
            io.printStackTrace();
        }

        imagenHead.setImageBitmap(bitmap);

    }
}
