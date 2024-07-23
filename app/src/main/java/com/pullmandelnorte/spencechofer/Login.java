package com.pullmandelnorte.spencechofer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orm.SugarContext;
import com.pullmandelnorte.spencechofer.entities.Registro;
import com.pullmandelnorte.spencechofer.modelo.ModeloLogin;
import com.pullmandelnorte.spencechofer.modelo.ModeloUsuario2;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.LoginService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Login extends AppCompatActivity {
    private static final int REQUEST_ACCESS_FINE = 0;
    private ImageView logoinicio;
    private EditText usuario, contrasenha;
    private Button aceptar;
    private ProgressDialog progress;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        SugarContext.init(Login.this);
        logoinicio = (ImageView) findViewById(R.id.logoinicio);

        // Animación inicial
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(logoinicio, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(800);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(logoinicio, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(800);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(logoinicio, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(800);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(100);
        animatorSet.start();



        //Tiempo de espera pantalla de inicio
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_login);
                usuario = (EditText) findViewById(R.id.loginUsuario);
                contrasenha = (EditText) findViewById(R.id.loginPass);
                aceptar = (Button) findViewById(R.id.login);
                exportDB();
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(habilitarpermisos()){
                            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                AlertNoGps();
                            }else{
                                mostrarDialogo();
                                ModeloLogin modeloLogin = new ModeloLogin();
                                modeloLogin.setCedula(usuario.getText().toString());
//                        modeloLogin.setPass(contrasenha.getText().toString());
//                        modeloLogin.setCedula("15419964-0");
//                        modeloLogin.setCedula("11643244-7");
//                        modeloLogin.setCedula("13773387-0");
//                        modeloLogin.setCedula("13214964-k");
//                        modeloLogin.setCedula("15419964-0");

                                LoginService loginService = API.getApi().create(LoginService.class);
                                Call<ModeloUsuario2> loginCall = loginService.login(modeloLogin);
                                loginCall.enqueue(new Callback<ModeloUsuario2>() {
                                    @Override
                                    public void onResponse(Call<ModeloUsuario2> call, Response<ModeloUsuario2> response) {
                                        if (response.isSuccessful()) {
                                            Gson gson = new Gson();
//                                    List<Registro> registro = Registro.find(Registro.class," CEDULA = ? ",response.body().getCedula());
//                                    if(registro != null && registro.size() > 0){
//                                        Registro r = Registro.findById(Registro.class,registro.get(0).getId());
//                                        r.setNombreusuario(response.body().getNombreusuario());
//                                        r.setDireccion(response.body().getDireccion());
//                                        r.setCedula(response.body().getCedula());
//                                        r.setEmail(response.body().getEmail());
//                                        r.setNumerotelefono(response.body().getTelefono());
//                                        r.save();
//                                    }else{
                                            Registro.deleteAll(Registro.class);
                                            Registro reg = new Registro();
                                            reg.setNombreusuario(response.body().getNombreusuario());
                                            reg.setDireccion(response.body().getDireccion());
                                            reg.setCedula(response.body().getCedula());
                                            reg.setEmail(response.body().getEmail());
                                            reg.setNumerotelefono(response.body().getTelefono());
                                            reg.setVersion(response.body().getVersion());
                                            reg.save();
//                                    }

                                            //validar version
                                            try {
                                                String version = getApplicationContext().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
                                                String versionDisponible = response.body().getVersion();
                                                if (!version.equals(versionDisponible)) {
                                                    AlertDialog.Builder dialogo = new AlertDialog.Builder(Login.this);
                                                    dialogo.setCancelable(false);
                                                    dialogo.setIcon(R.drawable.alert);
                                                    dialogo.setTitle("Actualización disponible");
                                                    dialogo.setMessage("debes de actualizar la aplicación ");
                                                    dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });
                                                    dialogo.show();
                                                } else {
                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } catch (PackageManager.NameNotFoundException e) {
                                                e.printStackTrace();
                                            }


                                        } else {
//                                    Toast.makeText(Login.this,"Error:" + response.code() ,Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder mensaje = new AlertDialog.Builder(Login.this);
                                            mensaje.setTitle("Cédula o Contraseña inválida, Verifique nuevamente!!!");
                                            mensaje.setIcon(getResources().getDrawable(android.R.drawable.ic_delete));
                                            mensaje.setCancelable(false);
                                            mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            mensaje.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ModeloUsuario2> call, Throwable t) {
                                        Toast.makeText(Login.this, "Error de Registro", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                    }
                });
            }
        }, 2500);
    }

    @Override
    protected void onDestroy() {
        if (progress != null) {
            progress.dismiss();
        }
        super.onDestroy();
    }

    public void registrar(View view) {
        Intent intent = new Intent(Login.this, RegistrarUsuario.class);
        startActivity(intent);
    }

    private void mostrarDialogo() {
        progress = new ProgressDialog(Login.this);
        progress.setTitle("¡Espere por favor!");
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(progress != null){
                progress.dismiss();
//                }
            }
        }, 2000);
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.pullmandelnorte.pullmanchofer"
                + "/databases/" + "pullmanchofer.db";
        String backupDBPath = "pullmanchofer.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
//            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this, "DB no Exported!", Toast.LENGTH_LONG).show();
        }
    }

    private void AlertNoGps() {
//        AlertDialog alert = null;
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Login.this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
//        alert = builder.create();
//        alert.show();
        builder.show();
    }

    public boolean habilitarpermisos(){
        /********/
        //habilitar todos los permisos necesarios
        if (
//            !(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                !(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                        !(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_ACCESS_FINE);

            Intent intentConfig = new Intent(Login.this, ConfiguracionPermisosActivity.class);
            startActivity(intentConfig);
            return false;

        }else{
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION},REQUEST_ACCESS_FINE);

                Intent intentConfig = new Intent(Login.this, ConfiguracionPermisosActivity.class);
                startActivity(intentConfig);

                AlertDialog.Builder dialogo = new AlertDialog.Builder(Login.this);
                dialogo.setCancelable(false);
                dialogo.setIcon(R.drawable.alert);
                dialogo.setTitle("Activar permiso");
                dialogo.setMessage("Permitir a la app acceder a la ubicacion todo el tiempo, ir a AJUSTES->APLICACIONES->Agilizagestor");
                dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                dialogo.show();
                return false;
            }

        }

        return true;
    }
}
