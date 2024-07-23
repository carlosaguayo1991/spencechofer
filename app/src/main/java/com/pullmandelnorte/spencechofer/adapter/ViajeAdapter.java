package com.pullmandelnorte.spencechofer.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pullmandelnorte.spencechofer.BuscarViaje;
import com.pullmandelnorte.spencechofer.CargaManualActivity;
import com.pullmandelnorte.spencechofer.EncuestaChoferes;
import com.pullmandelnorte.spencechofer.EscaneoActivity;
import com.pullmandelnorte.spencechofer.EscaneoSeleccionActivity;
import com.pullmandelnorte.spencechofer.IntermedioActivity;
import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.entities.Choferesviajes;
import com.pullmandelnorte.spencechofer.entities.Viaje;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloViaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloViajeId;
import com.pullmandelnorte.spencechofer.service.GPSService;
import com.pullmandelnorte.spencechofer.ui.viajes.ViajesFragment;
import com.pullmandelnorte.spencechofer.utils.Contenido;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViajeAdapter extends ArrayAdapter<Viaje> implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Viaje> items;
    public ProgressDialog progressIniciar;
    public ProgressDialog progressFinalizar;
    public boolean iniciar;
    public ReservaService reservaService;
    public boolean encuestaRealizada;


    public ViajeAdapter(@NonNull Context context, List<Viaje> items, boolean iniciar) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.mContext = context;
        this.iniciar = iniciar;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Viaje getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.qr:
//                Intent intent = new Intent(mContext, EscaneoActivity.class);
//                intent.putExtra("idviaje", items.get(position).getNroViaje());
//                mContext.startActivity(intent);
                Intent intent = new Intent(mContext, EscaneoSeleccionActivity.class);
                intent.putExtra("idviaje", items.get(position).getNroViaje());
                mContext.startActivity(intent);
                break;
            case R.id.manual:
                Intent intentManual = new Intent(mContext, CargaManualActivity.class);
                intentManual.putExtra("idviaje", items.get(position).getNroViaje());
                mContext.startActivity(intentManual);
                break;
            case R.id.aceptar:
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setMessage("¿Deseas iniciar??");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressIniciar = new ProgressDialog(mContext);
                        progressIniciar.setTitle("¡Espere por favor!");
                        progressIniciar.setMessage("Iniciando...");
                        progressIniciar.setCancelable(false);
                        progressIniciar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressIniciar.show();
                        encuestaRealizada = true;
                        if (encuestaRealizada) {
                            progressIniciar.dismiss();
                            Choferesviajes.deleteAll(Choferesviajes.class);
//                            Toast.makeText(mContext, "Iniciando viaje...", Toast.LENGTH_SHORT).show();
                            iniciarViaje(mContext,items.get(position).getNroViaje());


                        } else {
                            //hacemos la peticion para saber si tienen encuesta
                            ModeloViajeId viajeEncuesta = new ModeloViajeId(items.get(position).getNroViaje());
                            reservaService = API.getApi().create(ReservaService.class);
                            Call<Choferesviajes> choferesviajesCall = reservaService.funChoferesViaje(viajeEncuesta);
                            choferesviajesCall.enqueue(new Callback<Choferesviajes>() {
                                @Override
                                public void onResponse(Call<Choferesviajes> call, Response<Choferesviajes> response) {
                                    progressIniciar.dismiss();
                                    if (response.isSuccessful()) {
                                        Choferesviajes.deleteAll(Choferesviajes.class);
                                        Choferesviajes cv = new Choferesviajes();
                                        cv.setIdviaje(items.get(position).getNroViaje());
                                        cv.setCondicion_chofer_principal(response.body().getCondicion_chofer_principal());
                                        cv.setChoferes_principal(response.body().getChoferes_principal());
                                        cv.setNombre_principal(response.body().getNombre_principal());
                                        cv.setRealizado_principal(false);
                                        cv.setCondicion_chofer_secundaria(response.body().getCondicion_chofer_secundaria());
                                        cv.setChoferes_secundaria(response.body().getChoferes_secundaria());
                                        cv.setNombre_chofer_secundaria(response.body().getNombre_chofer_secundaria());
                                        cv.setRealizado_secundario(false);
                                        cv.setCondicion_auxiliar(response.body().getCondicion_auxiliar());
                                        cv.setAuxiliar(response.body().getAuxiliar());
                                        cv.setNombre_auxiliar(response.body().getNombre_auxiliar());
                                        cv.setRealizado_auxiliar(false);
                                        cv.save();
                                        if (response.body().getCondicion_chofer_principal().equals("true") ||
                                                response.body().getCondicion_chofer_secundaria().equals("true") ||
                                                response.body().getCondicion_auxiliar().equals("true")) {
                                            AlertDialog.Builder dialogEncuesta = new AlertDialog.Builder(v.getContext());
                                            dialogEncuesta.setTitle("Encuesta");
                                            dialogEncuesta.setMessage("Debes de realizar encuesta a conductores");
                                            dialogEncuesta.setCancelable(false);
                                            dialogEncuesta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent1 = new Intent(v.getContext(), EncuestaChoferes.class);
                                                    intent1.putExtra("idviaje", items.get(position).getNroViaje());
                                                    mContext.startActivity(intent1);
                                                }
                                            });
                                            dialogEncuesta.show();
                                        } else {
//                                            progressIniciar.dismiss();
//                                            Toast.makeText(mContext, "Iniciando viaje...", Toast.LENGTH_SHORT).show();
                                        iniciarViaje(mContext,items.get(position).getNroViaje());
                                        }
                                    } else {
//                                        progressIniciar.dismiss();
                                        Toast.makeText(v.getContext(), "Error:" + response.code(), Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(mContext, "Iniciando viaje...", Toast.LENGTH_SHORT).show();
                                        iniciarViaje(mContext,items.get(position).getNroViaje());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Choferesviajes> call, Throwable t) {
                                    progressIniciar.dismiss();
                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.intermedios:
                Intent i = new Intent(mContext, IntermedioActivity.class);
                i.putExtra("idviaje", items.get(position).

                        getNroViaje());
                mContext.startActivity(i);
                break;
            case R.id.finalizar:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(v.getContext());
                dialog2.setMessage("¿Deseas finalizar?");
                dialog2.setCancelable(false);
                dialog2.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressFinalizar = new ProgressDialog(mContext);
                        progressFinalizar.setTitle("¡Espere por favor!");
                        progressFinalizar.setMessage("Finalizando...");
                        progressFinalizar.setCancelable(false);
                        progressFinalizar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressFinalizar.show();
                        ModeloViaje viaje1 = new ModeloViaje(items.get(position).getNroViaje());
                        ReservaService reservaService1 = API.getApi().create(ReservaService.class);
                        Call<Mensaje> reservaServiceCall1 = reservaService1.finalizarviaje(viaje1);
                        reservaServiceCall1.enqueue(new Callback<Mensaje>() {
                            @Override
                            public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                                if (response.isSuccessful()) {
                                    progressFinalizar.dismiss();
                                    remove(items.get(position));
                                    notifyDataSetChanged();
                                    ViajesFragment.buscar.setEnabled(true);
                                    ViajesFragment.buscar.setVisibility(View.VISIBLE);
                                    ViajesFragment.nomina.setEnabled(false);
                                    ViajesFragment.nomina.setVisibility(View.GONE);
                                    ViajesFragment.viajeAdapterAsignado.updateData(Contenido.getViajeAsignado(mContext));
                                    Toast.makeText(mContext, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, GPSService.class);
                                    mContext.stopService(intent);

                                }
                            }

                            @Override
                            public void onFailure(Call<Mensaje> call, Throwable t) {
                                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog2.show();
                break;
        }
    }

    private static class ViewHolder {
        public ImageView image;
        public TextView nroviaje;
        public TextView ruta;
        public TextView maquina;
        public TextView choferprincipal;
        public TextView chofersecundario;
        public TextView auxiliar;
        public TextView cantidadasientos;
        public TextView fechapartida;
        public TextView fechallegada;
        public TextView horaposturachofer;
        public TextView horapartida;
        public TextView horallegada;
        public TextView trayecto;
        public TextView aceptar;
        public TextView finalizar;
        public TextView intermedio;
        public TextView codigoqr;
        public TextView manual;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_viaje, parent, false);
            holder = new ViewHolder();
//            holder.image = (ImageView) convertView
//                    .findViewById(R.id.list_image);
            holder.nroviaje = (TextView) convertView
                    .findViewById(R.id.nroviaje);
            holder.ruta = (TextView) convertView
                    .findViewById(R.id.ruta);
            holder.maquina = (TextView) convertView
                    .findViewById(R.id.maquina);
            holder.choferprincipal = (TextView) convertView
                    .findViewById(R.id.choferprincipal);
            holder.chofersecundario = (TextView) convertView
                    .findViewById(R.id.chofersecundario);
            holder.auxiliar = (TextView) convertView
                    .findViewById(R.id.auxiliar);
            holder.cantidadasientos = (TextView) convertView
                    .findViewById(R.id.cantidadAsientos);
            holder.fechapartida = (TextView) convertView
                    .findViewById(R.id.fechapartida);
            holder.fechallegada = (TextView) convertView
                    .findViewById(R.id.fechallegada);
            holder.horaposturachofer = (TextView) convertView
                    .findViewById(R.id.horaposturachofer);
            holder.horapartida = (TextView) convertView
                    .findViewById(R.id.horapartida);
            holder.horallegada = (TextView) convertView
                    .findViewById(R.id.horallegada);
            holder.trayecto = (TextView) convertView
                    .findViewById(R.id.trayecto);
            holder.aceptar = (TextView) convertView
                    .findViewById(R.id.aceptar);
            holder.aceptar.setOnClickListener(this);
            holder.finalizar = (TextView) convertView
                    .findViewById(R.id.finalizar);
            holder.finalizar.setOnClickListener(this);
            holder.intermedio = (TextView) convertView
                    .findViewById(R.id.intermedios);
            holder.intermedio.setOnClickListener(this);
            holder.codigoqr = (TextView) convertView
                    .findViewById(R.id.qr);
            holder.codigoqr.setOnClickListener(this);
            holder.manual = (TextView) convertView
                    .findViewById(R.id.manual);
            holder.manual.setOnClickListener(this);
//            holder.image.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (iniciar) {
            holder.finalizar.setVisibility(View.GONE);
            holder.intermedio.setVisibility(View.GONE);
            holder.codigoqr.setVisibility(View.GONE);
            holder.manual.setVisibility(View.GONE);
            holder.aceptar.setVisibility(View.VISIBLE);
        } else {
            holder.finalizar.setVisibility(View.VISIBLE);
            holder.intermedio.setVisibility(View.VISIBLE);
            holder.codigoqr.setVisibility(View.VISIBLE);
            holder.manual.setVisibility(View.VISIBLE);
            holder.aceptar.setVisibility(View.GONE);
        }

        holder.aceptar.setTag(position);
        holder.intermedio.setTag(position);
        holder.finalizar.setTag(position);
        holder.codigoqr.setTag(position);
        holder.manual.setTag(position);

        holder.nroviaje.setText("NRO. VIAJE : " + items.get(position).getNroViaje());
        holder.ruta.setText("RUTA : " + items.get(position).getRuta());
        holder.maquina.setText("MAQUINA : " + items.get(position).getMaquina());
        holder.choferprincipal.setText("CHOFER PRINCIPAL : " + items.get(position).getChoferPrincipal());
        holder.chofersecundario.setText("CHOFER SECUNDARIO : " + items.get(position).getChoferSecundario());
        holder.auxiliar.setText("AUXILIAR : " + items.get(position).getAuxiliar());
        holder.cantidadasientos.setText("CANTIDAD ASIENTOS : " + items.get(position).getCantidadAsientos());
        holder.fechapartida.setText("FECHA PARTIDA : " + items.get(position).getFechaPartida());
        holder.fechallegada.setText("FECHA LLEGADA : " + items.get(position).getFechaLlegada());
        holder.horaposturachofer.setText("HORA POSTURA CHOFER : " + items.get(position).getHoraPosturaChofer());
        holder.horapartida.setText("HORA PARTIDA : " + items.get(position).getHoraPartida());
        holder.horallegada.setText("HORA LLEGADA : " + items.get(position).getHoraLlegada());
        holder.trayecto.setText("TRAYECTO : " + items.get(position).getTrayecto());
        return convertView;
//        return super.getView(position, convertView, parent);
    }

    public void updateData(List<Viaje> viewModels) {
        if (viewModels != null && viewModels.size() > 0)
            ViajesFragment.nroviaje = viewModels.get(0).getNroViaje();
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void iniciarViaje(Context context, String nroviaje) {
        ModeloViaje viaje = new ModeloViaje(nroviaje);
        ReservaService reservaService = API.getApi().create(ReservaService.class);
        Call<Mensaje> reservaServiceCall = reservaService.iniciarviaje(viaje);
        reservaServiceCall.enqueue(new Callback<Mensaje>() {
            @Override
            public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                if (response.isSuccessful()) {
//                    progressIniciar.dismiss();
                    ViajesFragment.buscar.setEnabled(false);
                    ViajesFragment.buscar.setVisibility(View.GONE);
                    ViajesFragment.nomina.setEnabled(true);
                    ViajesFragment.nomina.setVisibility(View.VISIBLE);
                    ViajesFragment.viajeAdapterAsignado.updateData(Contenido.getViajeAsignado(context));
                    BuscarViaje.actividadBuscarViaje.finish();
                    Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, GPSService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent);
                    }else{
                        context.startService(intent);
                    }


                }
            }

            @Override
            public void onFailure(Call<Mensaje> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
