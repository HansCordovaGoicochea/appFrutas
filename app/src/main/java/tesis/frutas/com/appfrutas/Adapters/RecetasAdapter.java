package tesis.frutas.com.appfrutas.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.ScrollingActivity;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.clases.Receta;
import tesis.frutas.com.appfrutas.utils.Utils;

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.ViewHolder>{

    private final Context context;
    private List<Fruta> frutas;
    private List<Fruta> frutasFiltered;
    private int position;
    boolean activo;
    private ArrayList<Integer> counter = new ArrayList<Integer>();

    public RecetasAdapter(Context context, List<Fruta> frutas) {
        this.context = context;
        this.frutas = frutas;
        this.frutasFiltered = frutas;

        for (int i = 0; i < frutas.size(); i++) {
            counter.add(0);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView nombre_fruta, temporada;
        public ImageView img_fruta, img_calendar;

        RecyclerView cardRecyclerView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nombre_fruta = itemView.findViewById(R.id.nombre_fruta);
            temporada = itemView.findViewById(R.id.temporada_txt);

            img_fruta = itemView.findViewById(R.id.imagen_fruta);
            img_calendar = itemView.findViewById(R.id.calendar);

            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.card_view_vehiculo);
        }

        @Override
        public void onClick(View view) {


        }


    }

    @NonNull
    @Override
    public RecetasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fruta_receta, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecetasAdapter.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

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


        holder.cardRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter.get(position) % 2 == 0) {
                    holder.cardRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.cardRecyclerView.setVisibility(View.GONE);
                }
//
                counter.set(position, counter.get(position) + 1);

                String[] argu = {
                        String.valueOf(item.getId())
                };

                List<Receta> receta_items = Receta.find(Receta.class, "IDFRUTA = ? ", argu);

                RecetasChildAdapter cuponClienteAdapter = new RecetasChildAdapter(receta_items);
                holder.cardRecyclerView.setAdapter(cuponClienteAdapter);
            }
        });



    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
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
