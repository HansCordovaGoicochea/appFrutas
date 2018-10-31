package tesis.frutas.com.appfrutas.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tesis.frutas.com.appfrutas.R;
import tesis.frutas.com.appfrutas.clases.Directorio;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<Directorio> directorios;

    public RVAdapter(List<Directorio> directorios){
        this.directorios = directorios;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView nombre;
        TextView direccion;
        TextView telefono;
        TextView correo;
        TextView pagina;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nombre = (TextView)itemView.findViewById(R.id.txtNombre);
            direccion = (TextView)itemView.findViewById(R.id.txtDireccion);
            telefono = (TextView)itemView.findViewById(R.id.txtTelefono);
            correo = (TextView)itemView.findViewById(R.id.txtCorreo);
            pagina = (TextView)itemView.findViewById(R.id.txtPagina);
            imagen = (ImageView)itemView.findViewById(R.id.imagen);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directorio, parent, false);
        ViewHolder pvh = new ViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(directorios.get(position).getNombre());
        holder.direccion.setText(directorios.get(position).getDireccion());
        holder.telefono.setText(directorios.get(position).getTelefono());
        holder.correo.setText(directorios.get(position).getCorreo());
        holder.pagina.setText(directorios.get(position).getPagina());
        holder.imagen.setImageResource(directorios.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return directorios.size();
    }


}
