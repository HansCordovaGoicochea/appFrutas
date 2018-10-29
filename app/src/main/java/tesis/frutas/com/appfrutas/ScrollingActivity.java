package tesis.frutas.com.appfrutas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    ImageView month;
    TextView descripcion;
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

            Drawable myFabSrc = getResources().getDrawable(R.drawable.en_temporada);
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            fab.setImageDrawable(willBeWhite);

        } else if (primer_mes < mes_actual && mes_actual < ultimo_mes) {

            Drawable myFabSrc = getResources().getDrawable(R.drawable.en_temporada);
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            fab.setImageDrawable(willBeWhite);

        } else if (primer_mes == mes_actual) {
            Drawable myFabSrc = getResources().getDrawable(R.drawable.en_temporada);
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            fab.setImageDrawable(willBeWhite);

        } else if (ultimo_mes == mes_actual) {

            Drawable myFabSrc = getResources().getDrawable(R.drawable.alert_temporada);

            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            fab.setImageDrawable(willBeWhite);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_orange_600)));
        } else {

            Drawable myFabSrc = getResources().getDrawable(R.drawable.no_temporada);
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            fab.setImageDrawable(willBeWhite);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_red_400)));

        }


        month = (ImageView) findViewById(R.id.ImageViewEnero);
        setMonthImage(1);
        month = (ImageView) findViewById(R.id.ImageViewFebrero);
        setMonthImage(2);
        month = (ImageView) findViewById(R.id.ImageViewMarzo);
        setMonthImage(3);
        month = (ImageView) findViewById(R.id.ImageViewAbril);
        setMonthImage(4);
        month = (ImageView) findViewById(R.id.ImageViewMayo);
        setMonthImage(5);
        month = (ImageView) findViewById(R.id.ImageViewJunio);
        setMonthImage(6);
        month = (ImageView) findViewById(R.id.ImageViewJulio);
        setMonthImage(7);
        month = (ImageView) findViewById(R.id.ImageViewAgosto);
        setMonthImage(8);
        month = (ImageView) findViewById(R.id.ImageViewSeptiembre);
        setMonthImage(9);
        month = (ImageView) findViewById(R.id.ImageViewOctubre);
        setMonthImage(10);
        month = (ImageView) findViewById(R.id.ImageViewNoviembre);
        setMonthImage(11);
        month = (ImageView) findViewById(R.id.imageViewDiciembre);
        setMonthImage(12);

        int actualMonth = Utils.getMonth();
        TextView monthText = null;

        switch (actualMonth) {
            case 1:
                monthText = (TextView) findViewById(R.id.textView1);
                break;
            case 2:
                monthText = (TextView) findViewById(R.id.textView2);
                break;
            case 3:
                monthText = (TextView) findViewById(R.id.textView3);
                break;
            case 4:
                monthText = (TextView) findViewById(R.id.textView4);
                break;
            case 5:
                monthText = (TextView) findViewById(R.id.textView5);
                break;
            case 6:
                monthText = (TextView) findViewById(R.id.textView6);
                break;
            case 7:
                monthText = (TextView) findViewById(R.id.textView7);
                break;
            case 8:
                monthText = (TextView) findViewById(R.id.textView8);
                break;
            case 9:
                monthText = (TextView) findViewById(R.id.textView9);
                break;
            case 10:
                monthText = (TextView) findViewById(R.id.textView10);
                break;
            case 11:
                monthText = (TextView) findViewById(R.id.textView11);
                break;
            case 12:
                monthText = (TextView) findViewById(R.id.textView12);
                break;
        }
        //seteamos el mes actual y pintamos los que estan marcados para la temporada
        monthText.setTypeface(null, Typeface.BOLD);
        monthText.setTextSize(2, 11.0f);

        ((TextView) findViewById(R.id.kCalValor)).setText(fruta.getKcal());
        ((TextView) findViewById(R.id.grasasValor)).setText(fruta.getGrasas());
        ((TextView) findViewById(R.id.proteinasValor)).setText(fruta.getProteinas());
        ((TextView) findViewById(R.id.carbohidratosValor)).setText(fruta.getCarbohidratos());
        descripcion = (TextView) findViewById(R.id.elementDescription);
        descripcion.setText(getParagraphedString(this.fruta.getDescripcion()));

        // cuando hagamos click en el icono de alerta mostramos el modal de los textos
        ((ImageView) findViewById(R.id.fichaInfoButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ScrollingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_detalle_nutricion, null);
                dialogBuilder.setView(dialogView);

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        int result = Utils.isMonthIncluded(actualMonth, (int) this.fruta.getDateIni(), (int) this.fruta.getDateEnd());

        CharSequence dialogTitle = new String();
        CharSequence dialogDescription = new String();
        if (result == 3) {
            dialogDescription = getResources().getText(R.string.seasonTitleOutDescription).toString();
            dialogTitle = getResources().getText(R.string.seasonTitleOut).toString();
        }
        if (result == 1) {

            dialogDescription = getResources().getText(R.string.seasonTitleFirstDescription).toString();
            dialogTitle = getResources().getText(R.string.seasonTitleFirst).toString();
        }
        if (result == 2) {

            dialogDescription = getResources().getText(R.string.seasonTitleLastDescription).toString();
            dialogTitle = getResources().getText(R.string.seasonTitleLast).toString();
        }
        if (result == 0) {

            dialogDescription = getResources().getText(R.string.seasonTitleOkDescription).toString();
            dialogTitle = getResources().getText(R.string.seasonTitleOk).toString();
        }

        final AlertDialog.Builder content = new AlertDialog.Builder(this).setTitle(dialogTitle).setMessage(dialogDescription);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    content.show();

            }
        });

    }

    private String getParagraphedString(String textDescription) {
        return textDescription.replace("  ", System.getProperty("line.separator") + System.getProperty("line.separator"));
    }

    private void setMonthImage(int monthEval) {
        switch (Utils.isMonthIncluded(monthEval, (int) fruta.getDateIni(), (int) fruta.getDateEnd())) {
            case 0:
                month.setImageDrawable(getResources().getDrawable(R.drawable.cuadro_verde));
                return;
            case 1:
                month.setImageDrawable(getResources().getDrawable(R.drawable.cuadro_verde));
                return;
            case 2:
                month.setImageDrawable(getResources().getDrawable(R.drawable.cuadro_verde));
                return;
            case 3:
                month.setImageDrawable(getResources().getDrawable(R.drawable.cuadro_gris_2));
                return;
            default:
                month.setImageDrawable(getResources().getDrawable(R.drawable.cuadro_gris));
                return;
        }
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
