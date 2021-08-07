package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.widget.ListView;

import com.pullmandelnorte.spencechofer.adapter.ViajeAdapter;
import com.pullmandelnorte.spencechofer.entities.Viaje;
import com.pullmandelnorte.spencechofer.utils.Contenido;

import java.util.List;

public class BuscarViaje extends AppCompatActivity {
    public static Activity actividadBuscarViaje;
    private ListView listView;
    private ProgressDialog progress;
    private Handler handler;
    private List<Viaje> listaBuses;
    public static ViajeAdapter viajeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_viaje);
        actividadBuscarViaje = this;
        listView = (ListView) findViewById(R.id.activity_listview);

        progress = new ProgressDialog(BuscarViaje.this);
        progress.setTitle("Espera por favor!!");
        progress.setMessage("Espere!!");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                viajeAdapter = new ViajeAdapter(BuscarViaje.this, listaBuses,true);

//                SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
//                        productosAdapter);
//                swingBottomInAnimationAdapter.setAbsListView(listView);
//                listView.setAdapter(swingBottomInAnimationAdapter);
                listView.setAdapter(viajeAdapter);
//                assert swingBottomInAnimationAdapter.getViewAnimator() != null;
//
//                swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
//                        INITIAL_DELAY_MILLIS);


                listView.setClipToPadding(false);
                listView.setDivider(null);
                Resources r = getResources();
                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        8, r.getDisplayMetrics());
                listView.setDividerHeight(px);
                listView.setFadingEdgeLength(0);
                listView.setFitsSystemWindows(true);
                px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                        r.getDisplayMetrics());
                listView.setPadding(px, px, px, px);
                listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);

//                listar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(Detalle.this, CargarPedidos.class);
//                        intent.putExtra("nropedido",nropedido);
//                        startActivity(intent);
//                    }
//                });
                super.handleMessage(msg);
            }
        };

        new Thread() {
            public void run() {
                // Write Your Downloading logic here
                listaBuses = Contenido.getViajeList(BuscarViaje.this);

                // at the end write this.
//                                        handler.sendEmptyMessage(0);
                handler.sendEmptyMessageDelayed(0,200);
            }
        }.start();
    }

//    public void iniciarViaje(String idviaje){
//        viajeAdapter.iniciarViaje(BuscarViaje.this,idviaje);
//    }
}
