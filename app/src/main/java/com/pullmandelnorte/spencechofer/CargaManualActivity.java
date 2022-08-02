package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pullmandelnorte.spencechofer.modelo.Cedula;
import com.pullmandelnorte.spencechofer.modelo.Empleado;
import com.pullmandelnorte.spencechofer.modelo.EmpleadoCarga;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

public class CargaManualActivity extends AppCompatActivity {
    private EditText cedula,asiento,temperatura;
    private TextView nombrecompleto;
    private LinearLayout layout_asiento, layout_temperatura;
    private Button verificar;
    private Button registrar;
    private String idviaje;
    private AlertDialog dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_manual);

        layout_asiento = findViewById(R.id.layout_asiento);
        layout_temperatura = findViewById(R.id.layout_temperatura);
        cedula = findViewById(R.id.cedula);
        asiento = findViewById(R.id.asiento);
        temperatura = findViewById(R.id.temperatura);
        nombrecompleto = findViewById(R.id.nombrecompleto);
        verificar = findViewById(R.id.verificar);
        registrar = findViewById(R.id.registrar);

        layout_asiento.setVisibility(LinearLayout.GONE);
        layout_temperatura.setVisibility(LinearLayout.GONE);
        registrar.setVisibility(LinearLayout.GONE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idviaje = bundle.getString("idviaje");
        }
        verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!cedula.getText().toString().equals("")){
                    setProgressDialog();
                    Cedula ced = new Cedula(cedula.getText().toString());
                    ReservaService reservaService = API.getApi().create(ReservaService.class);
                    Call<Empleado> resCall = reservaService.funConsultaEmpleado(ced);
                    resCall.enqueue(new Callback<Empleado>() {
                        @Override
                        public void onResponse(Call<Empleado> call, Response<Empleado> response) {
                            if(response.isSuccessful()){
                                nombrecompleto.setText(response.body().getNombre_completo());
                                Toast.makeText(CargaManualActivity.this,"Ingrese los siguientes datos",Toast.LENGTH_SHORT).show();
                                layout_asiento.setVisibility(LinearLayout.VISIBLE);
                                layout_temperatura.setVisibility(LinearLayout.VISIBLE);
                                registrar.setVisibility(LinearLayout.VISIBLE);
                                dialog2.dismiss();
                            }else{
                                Toast.makeText(CargaManualActivity.this,"No se verifica existencia del empleado",Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Empleado> call, Throwable t) {
                            Toast.makeText(CargaManualActivity.this,"Error de conexion",Toast.LENGTH_SHORT).show();
                            dialog2.dismiss();
                        }
                    });
                }else{
                    Toast.makeText(CargaManualActivity.this,"Ingresar Datos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!asiento.getText().toString().equals("") && !temperatura.getText().toString().equals("") ){
                    setProgressDialog();
                    EmpleadoCarga empleadoCarga = new EmpleadoCarga();
                    empleadoCarga.setCedula(cedula.getText().toString());
                    empleadoCarga.setIdviaje(idviaje);
                    empleadoCarga.setAsiento(asiento.getText().toString());
                    empleadoCarga.setTemperatura(temperatura.getText().toString());
                    ReservaService reservaService = API.getApi().create(ReservaService.class);
                    Call<Mensaje> resCall = reservaService.funCargarSubidaManual(empleadoCarga);
                    resCall.enqueue(new Callback<Mensaje>() {
                        @Override
                        public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(CargaManualActivity.this,"Datos ingresados correctamente",Toast.LENGTH_SHORT).show();
                                nombrecompleto.setText("");
                                cedula.setText("");
                                asiento.setText("");
                                temperatura.setText("");
                                layout_asiento.setVisibility(LinearLayout.GONE);
                                layout_temperatura.setVisibility(LinearLayout.GONE);
                                registrar.setVisibility(LinearLayout.GONE);
                                dialog2.dismiss();
                            }else{
                                Toast.makeText(CargaManualActivity.this,response.body().getMensaje(),Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Mensaje> call, Throwable t) {
                            Toast.makeText(CargaManualActivity.this,"Error de conexion",Toast.LENGTH_SHORT).show();
                            dialog2.dismiss();
                        }
                    });

                }else{
                    Toast.makeText(CargaManualActivity.this,"Ingresar Datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(ll);

        dialog2 = builder.create();
        dialog2.show();
        Window window = dialog2.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog2.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog2.getWindow().setAttributes(layoutParams);
        }
    }
}