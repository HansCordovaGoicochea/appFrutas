package tesis.frutas.com.appfrutas.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tesis.frutas.com.appfrutas.Fragmentos.FragmentRecetas;
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
    private Spinner spinnerfruta;
    private Long idfruta;

    public RecetasChildAdapter(List<Receta> nameList) {
        this.nameList = nameList;

//        Log.e("namelist", nameList.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView numero;
        TextView nombre;
        ImageView delete_row, editar_row;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            numero = itemView.findViewById(R.id.numero);
            nombre = itemView.findViewById(R.id.nombre_receta);

            delete_row = itemView.findViewById(R.id.borrarReceta);
            editar_row = itemView.findViewById(R.id.editarReceta);
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

        holder.editar_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.itemView.getContext(), ""+position, Toast.LENGTH_SHORT).show();
                final EditText nombre, ingredientes, procedimiento;

                final Receta receta = Receta.findById(Receta.class, nameList.get(position).getId());
                LayoutInflater inflater = (LayoutInflater) holder.itemView.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.itemView.getContext());
                alertDialogBuilder.setCancelable(false);
                View view_dialog =  inflater.inflate(R.layout.dialog_receta, null);
                alertDialogBuilder.setView(view_dialog);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();


                // Spinner element
                spinnerfruta = (Spinner) view_dialog.findViewById(R.id.spFruta);
                // Loading spinner data from database
                loadSpinnerData(holder.itemView.getContext());


                TextView txtclose = view_dialog.findViewById(R.id.txtclose);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                Fruta fruta  = Fruta.findById(Fruta.class, receta.getIdfruta());
                for (int i=1;i<spinnerfruta.getCount();i++){
                    if (spinnerfruta.getItemAtPosition(i).equals(fruta.getNombre())){
                        Log.e(TAG, spinnerfruta.getItemAtPosition(i)+"");
                        spinnerfruta.setSelection(i);
                    }
                }

                nombre = view_dialog.findViewById(R.id.input_receta);
                ingredientes = view_dialog.findViewById(R.id.ingredientes_txt);
                procedimiento = view_dialog.findViewById(R.id.procedimiento_txt);

                nombre.setText(receta.getNombre());
                ingredientes.setText(receta.getIngredientes());
                procedimiento.setText(receta.getProcedimiento());

                Button agregar_receta = view_dialog.findViewById(R.id.agregar_receta);

                agregar_receta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            String _nombre = nombre.getText().toString();
                            String _ingredientes = ingredientes.getText().toString();
                            String _procedimiento = procedimiento.getText().toString();

                            receta.setIdfruta(idfruta);
                            receta.setNombre(_nombre);
                            receta.setIngredientes(_ingredientes);
                            receta.setProcedimiento(_procedimiento);

                            receta.save();

                            alertDialog.dismiss();

                        Fragment fm = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().findFragmentByTag("FragmentRecetas");
                        final FragmentTransaction ft = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                        ft.detach(fm);
                        ft.attach(fm);
                        ft.commit();

                            new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Exito!")
                                    .setContentText("Receta Editada")
                                    .show();

                    }
                });

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


    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData(Context context) {

        // Spinner Drop down elements
        final List<Fruta> lables = Select.from(Fruta.class)
                .orderBy("NOMBRE ASC")
                .list();
        ArrayList<String> options=new ArrayList<String>();

        options.add("- SELECCIONE UNA FRUTA -");
        for(Fruta fruta: lables){
            options.add(fruta.getNombre());
        }
        final List<String> frutasList = new ArrayList<>(options);
// Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context,R.layout.spinner_frutas,frutasList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

//        // Drop down layout style - list view with radio button
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_frutas);
//
//        // attaching data adapter to spinner
        spinnerfruta.setAdapter(spinnerArrayAdapter);

        spinnerfruta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);

//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
//                    Toast.makeText(getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    Fruta objfrutaMENOS = lables.get(position -1);
//                    Log.e(TAG, "objfrutaMENOS : " + objfrutaMENOS.getNombre());
//                    Log.e(TAG, "objfrutaMENOS : " + objfrutaMENOS.getId());
                    idfruta = objfrutaMENOS.getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }
}
