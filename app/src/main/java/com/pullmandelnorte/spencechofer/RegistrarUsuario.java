package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orm.SugarContext;
import com.pullmandelnorte.spencechofer.entities.Registro;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.RegistroService;

public class RegistrarUsuario extends AppCompatActivity {
    private EditText nombreusuario,telefono,email,cedula,direccion;
    private Button registrar;
    private Handler handler;
    private  ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        SugarContext.init(this);

        nombreusuario = (EditText) findViewById(R.id.nombreusuario);
        telefono = (EditText) findViewById(R.id.telefono);
        email = (EditText) findViewById(R.id.email);
        cedula = (EditText) findViewById(R.id.cedula);
        direccion = (EditText) findViewById(R.id.direccion);
        registrar = (Button) findViewById(R.id.registrar);
        //solicitar permisos
        //habilitar todos los permisos necesarios
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                !(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                !(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 0);
            }
        }

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogo();
                final Registro registro = new Registro();
                registro.setNombreusuario(nombreusuario.getText().toString());
                registro.setNumerotelefono(telefono.getText().toString());
                registro.setEmail(email.getText().toString());
                registro.setCedula(cedula.getText().toString());
                registro.setDireccion(direccion.getText().toString());
                Gson gson = new Gson();
                String valor = gson.toJson(registro,Registro.class);
                System.out.println(valor);
                RegistroService registroService = API.getApi().create(RegistroService.class);
                Call<Mensaje> registroCall = registroService.enviarUsuario(registro);
                registroCall.enqueue(new Callback<Mensaje>() {
                    @Override
                    public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                        if(response.isSuccessful()){
                            if(response.body().getMensaje().toUpperCase().equals("YA ESTA CREADO") ||
                                    response.body().getMensaje().toUpperCase().equals("NO ES FUNCIONARIO")){
//                                Toast.makeText(RegistrarUsuario.this,response.body().getMensaje(),Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder mensaje = new AlertDialog.Builder(RegistrarUsuario.this);
                                mensaje.setTitle(response.body().getMensaje()+"!!!");
//                                mensaje.setIcon(getResources().getDrawable(android.R.drawable.ic_delete));
                                mensaje.setCancelable(false);
                                mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                mensaje.show();
                            }else{
                                Toast.makeText(RegistrarUsuario.this,response.body().getMensaje(),Toast.LENGTH_SHORT).show();
                                registro.save();
                                finish();
                            }
//                            System.out.println(response.body());
//                            Toast.makeText(RegistrarUsuario.this,response.body().getMensaje(),Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(RegistrarUsuario.this,"Error : " + response.code(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Mensaje> call, Throwable t) {
                        System.out.println(t.getCause());
                        Toast.makeText(RegistrarUsuario.this,"Error de Registro" ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void mostrarDialogo(){
        progress = new ProgressDialog(RegistrarUsuario.this);
        progress.setTitle("¡Espere por favor!");
        progress.setMessage("Enviando...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
            }
        },2000);
    }
}
