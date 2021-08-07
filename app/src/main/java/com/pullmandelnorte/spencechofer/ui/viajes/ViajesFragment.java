package com.pullmandelnorte.spencechofer.ui.viajes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.pullmandelnorte.spencechofer.BuscarViaje;
import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.adapter.ViajeAdapter;
import com.pullmandelnorte.spencechofer.entities.Viaje;
import com.pullmandelnorte.spencechofer.utils.Contenido;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViajesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViajesFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static Button buscar;
    public static Button nomina;
    public static String nroviaje;
    public static ViajeAdapter viajeAdapterAsignado;
    private ProgressDialog progress;
    private ListView listView;
    private Handler handler;
    private List<Viaje> listaviajeiniciado;
    public ViajesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViajesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViajesFragment newInstance(String param1, String param2) {
        ViajesFragment fragment = new ViajesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viajes, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        buscar = (Button) view.findViewById(R.id.buscar);
        nomina = view.findViewById(R.id.link);
        listView = view.findViewById(R.id.activity_listview);
        progress = new ProgressDialog(view.getContext());
        progress.setTitle("Espera por favor!!");
        progress.setMessage("Espere!!");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progress.dismiss();
                if(listaviajeiniciado != null && listaviajeiniciado.size() > 0){
                    buscar.setEnabled(false);
                    buscar.setVisibility(View.GONE);
                    nomina.setEnabled(true);
                    nomina.setVisibility(View.VISIBLE);
                    nroviaje = listaviajeiniciado.get(0).getNroViaje();
                }else{
                    buscar.setEnabled(true);
                    buscar.setVisibility(View.VISIBLE);
                    nomina.setEnabled(false);
                    nomina.setVisibility(View.GONE);
                }

                viajeAdapterAsignado = new ViajeAdapter(view.getContext(), listaviajeiniciado,false);
                listView.setAdapter(viajeAdapterAsignado);
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

                super.handleMessage(msg);
            }
        };

        new Thread() {
            public void run() {
                // Write Your Downloading logic here
                listaviajeiniciado = Contenido.getViajeAsignado(view.getContext());

                // at the end write this.
//                                        handler.sendEmptyMessage(0);
                handler.sendEmptyMessageDelayed(0,200);
            }
        }.start();

        buscar.setOnClickListener(this);
        nomina.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buscar:
                Intent intent = new Intent(v.getContext(), BuscarViaje.class);
                startActivity(intent);
                break;
            case R.id.link:
                StringBuffer url = new StringBuffer();
                url.append("https://buspooling.cl/reserva/vistas/vista_viaje.php?idviaje=").append(nroviaje);
                Uri uri = Uri.parse(url.toString());
                Intent inte = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(inte);
                break;
        }
    }
}
