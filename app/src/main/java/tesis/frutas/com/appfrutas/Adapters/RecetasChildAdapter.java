package tesis.frutas.com.appfrutas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.ScrollingActivity;
import tesis.frutas.com.appfrutas.ScrollingRecetaActivity;
import tesis.frutas.com.appfrutas.clases.Fruta;
import tesis.frutas.com.appfrutas.clases.Receta;


public class RecetasChildAdapter extends RecyclerView.Adapter<RecetasChildAdapter.ViewHolder>{

    private static final String TAG = RecetasChildAdapter.class.getSimpleName();
    private List<Receta> nameList;
    private List<Fruta> frutas;
    private Fruta fruta;

    private Integer posicion_cupon;


    public RecetasChildAdapter(List<Receta> nameList) {
        this.nameList = nameList;

//        Log.e("namelist", nameList.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView numero;
        TextView nombre;
        ImageView delete_row;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            numero = itemView.findViewById(R.id.numero);
            nombre = itemView.findViewById(R.id.nombre_receta);

            delete_row = itemView.findViewById(R.id.borrarReceta);
            verBoton();

        }

        @Override
        public void onClick(View v) {
            Intent intent =new Intent(itemView.getContext(), ScrollingRecetaActivity.class);
//            Toast.makeText(itemView.getContext(), ""+getAdapterPosition(), Toast.LENGTH_SHORT).show();
            intent.putExtra("idreceta", nameList.get(getAdapterPosition()).getId().toString());
            itemView.getContext().startActivity(intent);
        }

        private void verBoton() {
            SharedPreferences preferences = itemView.getContext().getSharedPreferences("admin_pref", Context.MODE_PRIVATE);
            boolean activo = preferences.getBoolean("activo", false);

            if (!activo){
                delete_row.setVisibility(View.INVISIBLE);
            }else{
                delete_row.setVisibility(View.VISIBLE);
            }
        }

    }

    @NonNull
    @Override
    public RecetasChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receta, parent, false);



        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecetasChildAdapter.ViewHolder holder, final int position) {


        String numero = "N° "+(position+1);
        String nombre = nameList.get(position).getNombre();

        holder.numero.setText(numero);
        holder.nombre.setText(nombre);

        holder.delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.itemView.getContext(), ""+position, Toast.LENGTH_SHORT).show();

                Receta receta = Receta.findById(Receta.class, nameList.get(position).getId());
                receta.delete();
                removeAt(position);

                new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Exito!")
                        .setContentText("Receta Eliminada")
                        .show();

            }
        });

    }

    public void removeAt(int position) {
        nameList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, nameList.size());
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }
}
