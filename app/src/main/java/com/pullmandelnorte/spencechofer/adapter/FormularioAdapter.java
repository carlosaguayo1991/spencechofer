package com.pullmandelnorte.spencechofer.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.entities.Preguntas;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class FormularioAdapter extends ArrayAdapter<Preguntas> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private List<Preguntas> items;
    private List<Preguntas> orig;
    private Context mContext;
    private boolean validarReapuesta;
    //Dialogo
    private Dialog mDialog;
    private TextView mDialogOKButton;
    private TextView mDialogCancelButton;
    private TextView mDialogTitle;

    private static final String tipoInteger = "INTEGER";
    private static final String tipoString = "STRING";
    private static final String tipoBoolean = "BOOLEAN";
    private static final String tipoRange = "RANGE";

    private EditText editText;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RatingBar ratingBar;

    List<Preguntas> preguntas;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FormularioAdapter(@NonNull Context context, List<Preguntas> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.mContext = context;
        preguntas = Preguntas.find(Preguntas.class," CAMPO = ?","true");
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Preguntas getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View view) {
        final int possition = (Integer) view.getTag();
        final View v = view;

        //validar que la pregunta a respoder no sea una modificacion
//        if(auditoriamobile.size() != 0){
//            auditoriamobiledetalles = Auditoriamobiledetalle.find(Auditoriamobiledetalle.class,"codauditoriamobile = ? and codauditoriadetalle = ?",
//                    String.valueOf(auditoriamobile.get(0).getCodauditoriamobile()),String.valueOf(items.get(possition).getCodauditoriadetalle()));
//        }

        switch (view.getId()) {
            case R.id.responder:

                if (mDialog == null) {
                    mDialog = new Dialog(mContext, R.style.CustomDialogTheme);
                }
                mDialog.setContentView(R.layout.dialog_universal_warning);
                mDialog.setCancelable(false);
                mDialog.show();

                final LinearLayout linearLayout = (LinearLayout) mDialog.findViewById(R.id.layout_dialogo);
                final String tipo = items.get(possition).getTipodato();
                switch (tipo) {
                    case tipoInteger:
                        editText = new EditText(mContext);
                        editText.setHint("Respuesta");
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                        if(auditoriamobiledetalles.size() != 0){
//                            editText.setText(auditoriamobiledetalles.get(0).getRespuesta());
//                        }
                        linearLayout.addView(editText);
                        break;
                    case tipoString:
                        editText = new EditText(mContext);
                        editText.setHint("Respuesta");
                        if(preguntas.get(possition).getRespuesta() != null){
                            editText.setText(preguntas.get(possition).getRespuesta());
                        }
                        linearLayout.addView(editText);
                        break;
                    case tipoBoolean:
                        radioGroup = new RadioGroup(mContext);
                        radioButton1 = new RadioButton(mContext);
                        radioButton1.setText("Si");
                        radioButton2 = new RadioButton(mContext);
                        radioButton2.setText("No");

                        radioGroup.addView(radioButton1);
                        radioGroup.addView(radioButton2);
                        if(preguntas.get(possition).getRespuesta() != null){
                            if(preguntas.get(possition).getRespuesta().equals("Si")){
                                radioButton1.setChecked(true);
                                radioButton2.setChecked(false);
                            }else {
                                radioButton1.setChecked(false);
                                radioButton2.setChecked(true);
                            }
                        }
                        linearLayout.addView(radioGroup);
                        break;
                    case tipoRange:
                        LinearLayout contenedor = new LinearLayout(mContext);
                        contenedor.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        contenedor.setGravity(Gravity.CENTER);
                        ratingBar = new RatingBar(mContext);
                        ratingBar.setNumStars(5);
                        ratingBar.setMax(5);
                        ratingBar.setStepSize(0.5f);
//                        if(auditoriamobiledetalles.size() != 0){
//                            ratingBar.setRating(Float.valueOf(auditoriamobiledetalles.get(0).getRespuesta()));
//                        }
                        contenedor.addView(ratingBar);
                        linearLayout.addView(contenedor);
                        break;
                }


                mDialogOKButton = (TextView) mDialog.findViewById(R.id.dialogo_ok);
                mDialogCancelButton = (TextView) mDialog.findViewById(R.id.dialogo_cancel);
                mDialogTitle = (TextView) mDialog.findViewById(R.id.dialogo_titulo);

                //Datos dentro del dialogo
//                mDialogTitle.setText(items.get(possition).getNombrecampo());
                mDialogOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validarReapuesta = true;
                        switch (tipo) {
                            case tipoInteger:
                                //validar que no ingrese null o vacio
                                if (!editText.getText().toString().equals("")) {
//                                    if(auditoriamobiledetalles.size() != 0){
//                                        actualizarAditoriaMobileDetalle(auditoriamobiledetalles.get(0).getId(),editText.getText().toString());
//                                    }else{
//                                        crearAuditoriaMobileDetalle(editText.getText().toString(), items.get(possition).getCodauditoria(),
//                                                items.get(possition).getCodauditoriadetalle(),auditoriamobile.get(0).getCodauditoriamobile());
//                                    }
                                } else {
                                    Toast.makeText(mContext, "Debes ingresar respuesta", Toast.LENGTH_LONG).show();
//                                    validarReapuesta = false;
                                }
                                break;
                            case tipoString:
                                if (!editText.getText().toString().equals("")) {
//                                    if(preguntas.get(possition).getRespuesta().equals("")){
                                    actualizarPregunta(preguntas.get(possition).getId(), editText.getText().toString());
//                                        actualizarAditoriaMobileDetalle(auditoriamobiledetalles.get(0).getId(),editText.getText().toString());
//                                    }else{

//                                        crearAuditoriaMobileDetalle(editText.getText().toString(), items.get(possition).getCodauditoria(),
//                                                items.get(possition).getCodauditoriadetalle(),auditoriamobile.get(0).getCodauditoriamobile());
//                                    }
                                } else {
                                    Toast.makeText(mContext, "Debes ingresar respuesta", Toast.LENGTH_LONG).show();
                                    validarReapuesta = false;
                                }
                                break;
                            case tipoBoolean:
                                //radioButton1 = si
                                //radioButton2 = no
                                if (radioButton1.isChecked() == false && radioButton2.isChecked() == false) {
                                    Toast.makeText(mContext, "Debes ingresar respuesta", Toast.LENGTH_LONG).show();
                                    validarReapuesta = false;
                                } else {
//                                    if(preguntas.get(possition).getRespuesta() == null){
                                        if (radioButton1.isChecked()) {
                                            actualizarPregunta(preguntas.get(possition).getId(), "Si");
//                                            actualizarAditoriaMobileDetalle(auditoriamobiledetalles.get(0).getId(),"Si");
                                        } else if (radioButton2.isChecked()) {
                                            actualizarPregunta(preguntas.get(possition).getId(), "No");
//                                            actualizarAditoriaMobileDetalle(auditoriamobiledetalles.get(0).getId(),"No");
                                        }
//                                    }else{
//                                        if (radioButton1.isChecked()) {
//                                            crearAuditoriaMobileDetalle("Si", items.get(possition).getCodauditoria(), items.get(possition).getCodauditoriadetalle(),auditoriamobile.get(0).getCodauditoriamobile());
//                                        } else if (radioButton2.isChecked()) {
//                                            crearAuditoriaMobileDetalle("No", items.get(possition).getCodauditoria(), items.get(possition).getCodauditoriadetalle(),auditoriamobile.get(0).getCodauditoriamobile());
//                                        }
//                                    }
                                }


                                break;
                            case tipoRange:
//                                if(auditoriamobiledetalles.size() != 0){
//                                    actualizarAditoriaMobileDetalle(auditoriamobiledetalles.get(0).getId(),String.valueOf(ratingBar.getRating()));
//                                }else{
//                                    crearAuditoriaMobileDetalle(String.valueOf(ratingBar.getRating()), items.get(possition).getCodauditoria(), items.get(possition).getCodauditoriadetalle(),auditoriamobile.get(0).getCodauditoriamobile());
//                                }
                                break;
                        }
                        if(validarReapuesta){
                                Toast.makeText(mContext,"Respuesta Guardada",Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }

                    }
                });

                mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
//
        }
    }

    private static class ViewHolder {
        //        public TextView nroitem;
        public TextView nombrecampo;
        public TextView descripcion;
        public TextView responder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_pregunta, parent, false);
            holder = new ViewHolder();
//            holder.nroitem = (TextView) convertView.findViewById(R.id.nroitem);
            holder.nombrecampo = (TextView) convertView.findViewById(R.id.nombrecampo);
            holder.descripcion = (TextView) convertView.findViewById(R.id.descripcion);
            holder.responder = (TextView) convertView.findViewById(R.id.responder);
            holder.responder.setOnClickListener(this);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.responder.setTag(position);

        holder.nombrecampo.setText(items.get(position).getTexto());


        Preguntas item = getItem(position);


        if (preguntas.size() != 0 ) {
            if (preguntas.get(position).getRespuesta() == null) {
//                holder.nroitem.setTextColor(Color.parseColor("#00abc0"));
                holder.nombrecampo.setTextColor(Color.parseColor("#FF7414"));
                holder.responder.setBackgroundColor(Color.parseColor("#FF7414"));
                holder.responder.setText("Responder");
            } else {
//                holder.nroitem.setTextColor(Color.parseColor("#008000"));
                holder.nombrecampo.setTextColor(Color.parseColor("#008000"));
                holder.responder.setBackgroundColor(Color.parseColor("#008000"));
                holder.responder.setText("Editar");
            }
        } else {
            Toast.makeText(mContext, "No existe preguntas", Toast.LENGTH_LONG).show();
        }

        return convertView;
    }

    //elimina un item de la lista pasandole la posicion del adaptador
    public void remove(int position) {
        items.remove(position);
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void updateData(List<Preguntas> viewModels) {
        orig = null;
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }


    private void actualizarPregunta(Long id, String respuesta) {
        Preguntas p = Preguntas.findById(Preguntas.class, id);
        p.setRespuesta(respuesta);
        p.save();
        //actualiza adaptador
        preguntas = Preguntas.find(Preguntas.class," CAMPO = ?","true");
        updateData(preguntas);
    }
}
