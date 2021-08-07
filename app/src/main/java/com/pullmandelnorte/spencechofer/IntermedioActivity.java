package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pullmandelnorte.spencechofer.adapter.IntermedioAdapter;
import com.pullmandelnorte.spencechofer.modelo.ModeloIntermedio;
import com.pullmandelnorte.spencechofer.utils.Contenido;

import java.util.List;

public class IntermedioActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressDialog progress;
    private Handler handler;
    private List<ModeloIntermedio> listaIntermedio;
    private String idviaje;
    private TextView salir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedio);
        listView = findViewById(R.id.listview);
        salir = findViewById(R.id.salir);
        Toolbar toolbar = findViewById(R.id.toolbar_intermedio);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idviaje = bundle.getString("idviaje");
        }

        progress = new ProgressDialog(IntermedioActivity.this);
        progress.setTitle("¡Espere por favor!");
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                IntermedioAdapter busesAdapter = new IntermedioAdapter(IntermedioActivity.this, listaIntermedio);

//                SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
//                        productosAdapter);
//                swingBottomInAnimationAdapter.setAbsListView(listView);
//                listView.setAdapter(swingBottomInAnimationAdapter);
                listView.setAdapter(busesAdapter);
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
                listaIntermedio = Contenido.getIntermedios(IntermedioActivity.this, idviaje);

                // at the end write this.
//                                        handler.sendEmptyMessage(0);
                handler.sendEmptyMessageDelayed(0,200);
            }
        }.start();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}