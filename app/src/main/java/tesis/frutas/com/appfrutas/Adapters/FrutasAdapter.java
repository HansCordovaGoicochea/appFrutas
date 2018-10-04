package tesis.frutas.com.appfrutas.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.clases.Fruta;

public class FrutasAdapter extends RecyclerView.Adapter<FrutasAdapter.ViewHolder> {

    private final Context context;
    private final List<Fruta> frutas;

    public FrutasAdapter(Context context, List<Fruta> frutas) {
        this.context = context;
        this.frutas = frutas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView nombre_fruta, temporada;
        public ImageView img_fruta, img_calendar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nombre_fruta = itemView.findViewById(R.id.nombre_fruta);
            temporada = itemView.findViewById(R.id.temporada_txt);

            img_fruta = itemView.findViewById(R.id.imagen_fruta);
            img_calendar = itemView.findViewById(R.id.calendar);

        }

        @Override
        public void onClick(View view) {
//            Intent intent =new Intent(itemView.getContext(), QrActivity.class);
//            intent.putExtra("idvehiculo", vehiculos.get(getAdapterPosition()).getId().toString());
//            context.startActivity(intent);
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
    public void onBindViewHolder(@NonNull FrutasAdapter.ViewHolder holder, int position) {
        final Fruta item = frutas.get(position);

        holder.nombre_fruta.setText(item.getNombre());
        holder.temporada.setText(item.getDateIni()+" - "+item.getDateEnd());

    }

    @Override
    public int getItemCount() {
        return frutas.size();
    }
}
