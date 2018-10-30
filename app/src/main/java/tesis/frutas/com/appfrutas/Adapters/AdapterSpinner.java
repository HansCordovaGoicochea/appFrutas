package tesis.frutas.com.appfrutas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tesis.frutas.com.appfrutas.R;

public class AdapterSpinner extends BaseAdapter{
    Context context;
    String[] data;
    LayoutInflater inflater;
    int layoutResourceId;
    String titulo;

    public AdapterSpinner(Context context, int textViewResourceId, String[] data, String titulo) {
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.layoutResourceId = textViewResourceId;
        this.titulo = titulo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View actionBarView = this.inflater.inflate(R.layout.ab_main_view, null);

        TextView title = (TextView) actionBarView.findViewById(R.id.ab_basemaps_title);
        title.setTextSize(1, (this.context.getResources().getDimension(R.dimen.abc_text_size_body_1_material) / this.context.getResources().getDisplayMetrics().density) + 2.0f);
        title.setTextColor(this.context.getResources().getColor(R.color.md_white_1000));
        TextView subtitle = (TextView) actionBarView.findViewById(R.id.ab_basemaps_subtitle);
//        title.setText(this.context.getResources().getString(R.string.app_name));
        title.setText(titulo);
        subtitle.setText(this.data[position].toUpperCase());
        subtitle.setTextSize(1, (this.context.getResources().getDimension(R.dimen.abc_text_size_subhead_material) / this.context.getResources().getDisplayMetrics().density) - 4.0f);
        subtitle.setTextColor(this.context.getResources().getColor(R.color.md_white_1000));

//        if (position == 0) {
//            return initialSelection(false);
//        }

        return actionBarView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View actionBarDropDownView = this.inflater.inflate(R.layout.ab_dropdown_view, null);
        ((TextView) actionBarDropDownView.findViewById(R.id.ab_basemaps_dropdown_title)).setText(this.data[position]);

//        if (position == 0) {
//            initialSelection(true);
//        }
//        TextView tv = (TextView) actionBarDropDownView;
//        if(position == 0){
//            // Set the hint text color gray
//            tv.setTextColor(Color.GRAY);
//        }
//        else {
//            tv.setTextColor(Color.BLACK);
//        }

        return actionBarDropDownView;
    }

    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(context);
        view.setText("- Seleccione -");
        int spacing = 5;
        view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    @Override
    public int getCount() {
        return this.data.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }


//    @Override
//    public boolean isEnabled(int position) {
//        if(position == 0)
//        {
//            // Disable the first item from Spinner
//            // First item will be use for hint
//            return false;
//        }
//        else
//        {
//            return true;
//        }
//    }
}
