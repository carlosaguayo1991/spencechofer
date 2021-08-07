package com.pullmandelnorte.spencechofer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.entities.Choferesviajes;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EncuestaChoferAdapter extends ArrayAdapter<String> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> items;
    private List<String> itemsEncuestaChofer;

    public EncuestaChoferAdapter(@NonNull Context context, List<String> items, List<String> listEncuestaChofer) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.itemsEncuestaChofer = listEncuestaChofer;
        this.mContext = context;
    }

    private static class ViewHolder{
        public TextView nombre;
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
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable String item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_chofer_encuesta,parent,false);
            holder = new ViewHolder();
            holder.nombre = convertView.findViewById(R.id.nombre);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //verificar si ya realizo encuesta
        List<Choferesviajes> chofEncue = Choferesviajes.listAll(Choferesviajes.class);
        if(itemsEncuestaChofer.get(position).equals("PRINCIPAL")){
            if(chofEncue.get(0).isRealizado_principal()){
                holder.nombre.setTextColor(Color.parseColor("#00BB2D"));
            }else{
                holder.nombre.setTextColor(Color.parseColor("#000000"));
            };
        }else if(itemsEncuestaChofer.get(position).equals("SECUNDARIO")){
            if(chofEncue.get(0).isRealizado_secundario()){
                holder.nombre.setTextColor(Color.parseColor("#00BB2D"));
            }else{
                holder.nombre.setTextColor(Color.parseColor("#000000"));
            };
        }else{
            if(chofEncue.get(0).isRealizado_auxiliar()){
                holder.nombre.setTextColor(Color.parseColor("#00BB2D"));
            }else{
                holder.nombre.setTextColor(Color.parseColor("#000000"));
            };
        }

        holder.nombre.setText(items.get(position));
        return convertView;
    }

    public void updateData(List<String> viewModels) {
        itemsEncuestaChofer.clear();
        itemsEncuestaChofer.addAll(viewModels);
        notifyDataSetChanged();
    }
}
