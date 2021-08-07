package com.pullmandelnorte.spencechofer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.modelo.ModeloIntermedio;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IntermedioAdapter extends ArrayAdapter<ModeloIntermedio> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ModeloIntermedio> items;

    public IntermedioAdapter(@NonNull Context context, List<ModeloIntermedio> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.mContext = context;
    }

    private static class ViewHolder{
        public TextView nombre;
        public TextView hora;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public ModeloIntermedio getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable ModeloIntermedio item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_intermedio,parent,false);
            holder = new ViewHolder();
            holder.nombre = convertView.findViewById(R.id.nombre);
            holder.hora = convertView.findViewById(R.id.hora);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nombre.setText(items.get(position).getNombre());
        holder.hora.setText(items.get(position).getHora());
        return convertView;
    }
}
