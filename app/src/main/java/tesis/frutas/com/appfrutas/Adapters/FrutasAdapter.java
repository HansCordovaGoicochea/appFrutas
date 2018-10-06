package tesis.frutas.com.appfrutas.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import tesis.frutas.com.appfrutas.ActividadPrincipal;
import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.ScrollingActivity;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.utils.Utils;

public class FrutasAdapter extends RecyclerView.Adapter<FrutasAdapter.ViewHolder> {

    private final Context context;
    private List<Fruta> frutas;
    private int position;
    boolean activo;

    public FrutasAdapter(Context context, List<Fruta> frutas) {
        this.context = context;
        this.frutas = frutas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{


        public TextView nombre_fruta, temporada;
        public ImageView img_fruta, img_calendar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nombre_fruta = itemView.findViewById(R.id.nombre_fruta);
            temporada = itemView.findViewById(R.id.temporada_txt);

            img_fruta = itemView.findViewById(R.id.imagen_fruta);
            img_calendar = itemView.findViewById(R.id.calendar);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent =new Intent(itemView.getContext(), ScrollingActivity.class);



            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(img_fruta, "imagenTransition");
                pairs[1] = new Pair<View, String>(nombre_fruta, "nombreTransition");

                activityOptions = ActivityOptions.makeSceneTransitionAnimation(((ActividadPrincipal)itemView.getContext()), pairs);
            }

            intent.putExtra("idfruta", frutas.get(getAdapterPosition()).getId().toString());
            assert activityOptions != null;
            context.startActivity(intent, activityOptions.toBundle());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            SharedPreferences preferences = itemView.getContext().getSharedPreferences("admin_pref", Context.MODE_PRIVATE);
            activo = preferences.getBoolean("activo",false);
            if (activo){
                menu.setHeaderTitle("Seleccione una opción");
                //groupId, itemId, order, title
                menu.add(0, v.getId(), 0, "Editar");
                menu.add(0, v.getId(), 0, "Eliminar");
            }

        }

    }




    @NonNull
    @Override
    public FrutasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fruta, parent, false);


        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FrutasAdapter.ViewHolder holder, int position) {
        final Fruta item = frutas.get(position);

        String nombre = item.getNombre();
        holder.nombre_fruta.setText(item.getNombre());
        holder.temporada.setText(item.getDateString(holder.itemView.getContext()));

//        String fileName = Normalizer.normalize(nombre.toLowerCase().trim(), Normalizer.Form.NFD).replaceAll(" ", EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR).replaceAll("[^\\p{ASCII}]", "");
        int mes_actual = Utils.getMonth();
        int primer_mes = (int) item.getDateIni();
        int ultimo_mes = (int) item.getDateEnd();

        if (ultimo_mes < primer_mes) {
            ultimo_mes += 12;
            if (mes_actual < primer_mes) {
                mes_actual += 12;
            }
        }
        if (primer_mes == 1 && ultimo_mes == 12) {
            holder.img_calendar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.calendar_ok));
        } else if (primer_mes < mes_actual && mes_actual < ultimo_mes) {
            holder.img_calendar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.calendar_ok));
        } else if (primer_mes == mes_actual) {
            holder.img_calendar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.calendar_ok));
        } else if (ultimo_mes == mes_actual) {
            holder.img_calendar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.calendar_alerta));
        } else {
            holder.img_calendar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.calendar_no));
        }

        String fileName = Normalizer.normalize(nombre.toLowerCase().trim(), Normalizer.Form.NFD).replaceAll(" ", "_").replaceAll("[^\\p{ASCII}]", "");

        Bitmap bitmap = null;

        try{
            ContextWrapper cw = new ContextWrapper(holder.itemView.getContext());
            File dirImages = cw.getDir("frutas", Context.MODE_PRIVATE);
            File myPath = new File(dirImages, fileName + ".png");

            Log.e("ruta", myPath+"");

            FileInputStream fileInputStream =
                    new FileInputStream(myPath);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        }catch (IOException io){
            io.printStackTrace();
        }

        holder.img_fruta.setImageBitmap(bitmap);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return frutas.size();
    }


    public void setFilter(List<Fruta> nuevasFrutas){
        frutas = new ArrayList<>();
        frutas.addAll(nuevasFrutas);
        notifyDataSetChanged();
    }
}
