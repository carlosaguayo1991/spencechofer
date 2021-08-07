package com.pullmandelnorte.spencechofer.ui.viajes;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.entities.Registro;
import com.pullmandelnorte.spencechofer.modelo.Mensaje;
import com.pullmandelnorte.spencechofer.modelo.ModeloUsuario;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.RegistroService;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActualizarUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActualizarUsuarioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText nombreusuario,telefono,email,cedula,direccion,pass;
    private Button actualizar;
    private  ProgressDialog progress;

    public ActualizarUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActualizarUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActualizarUsuarioFragment newInstance(String param1, String param2) {
        ActualizarUsuarioFragment fragment = new ActualizarUsuarioFragment();
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
        View view = inflater.inflate(R.layout.fragment_actualizar_usuario, container, false);
        nombreusuario = (EditText) view.findViewById(R.id.nombreusuario);
        telefono = (EditText) view.findViewById(R.id.telefono);
        email = (EditText) view.findViewById(R.id.email);
        cedula = (EditText) view.findViewById(R.id.cedula);
        direccion = (EditText) view.findViewById(R.id.direccion);
        pass = (EditText) view.findViewById(R.id.contrasena);
        actualizar = (Button) view.findViewById(R.id.actualizar);
        //cargar los datos actuales
        List<Registro> usuario = Registro.listAll(Registro.class);
        if(usuario != null && usuario.size() > 0){
            nombreusuario.setText(usuario.get(0).getNombreusuario());
            telefono.setText(usuario.get(0).getNumerotelefono());
            email.setText(usuario.get(0).getEmail());
            cedula.setText(usuario.get(0).getCedula());
            direccion.setText(usuario.get(0).getDireccion());
        }else {
            Toast.makeText(getContext(),"No se encontro usuario", Toast.LENGTH_SHORT).show();
        }

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pass.getText().toString().equals("") || pass.getText().toString().equals(null)){
//                    Toast.makeText(view.getContext(),"Debes ingresar contraseña",Toast.LENGTH_SHORT).show();
//                }else{
                    mostrarDialogo();
                    ModeloUsuario registro = new ModeloUsuario();
                    registro.setNombreusuario(nombreusuario.getText().toString());
                    registro.setNumerotelefono(telefono.getText().toString());
                    registro.setEmail(email.getText().toString());
                    registro.setCedula(cedula.getText().toString());
                    registro.setDireccion(direccion.getText().toString());
                    registro.setPass(pass.getText().toString());
                    Gson gson = new Gson();
                    String valor = gson.toJson(registro,ModeloUsuario.class);
                    System.out.println(valor);
                    RegistroService registroService = API.getApi().create(RegistroService.class);
                    Call<Mensaje> registroCall = registroService.modificarUsuario(registro);
                    registroCall.enqueue(new Callback<Mensaje>() {
                        @Override
                        public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                            if(response.isSuccessful()){
                                if(response.body().getMensaje().toUpperCase().equals("USUARIO MODIFICADO")){
                                    Toast.makeText(getActivity(),response.body().getMensaje(),Toast.LENGTH_SHORT).show();
//                                    Navigation.createNavigateOnClickListener(R.id.reservaFragment,null);
//                                    Navigation.findNavController(v).navigate(R.id.reservaFragment);
                                    Navigation.findNavController(v).navigateUp();
                                }else{
                                    Toast.makeText(getActivity(),response.body().getMensaje(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getActivity(),"Error : " + response.code(),Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Mensaje> call, Throwable t) {
                            System.out.println(t.getCause());
                            Toast.makeText(getActivity(),"Error de Registro" ,Toast.LENGTH_SHORT).show();
                        }
                    });
//                }
            }
        });
        return view;
    }

    private void mostrarDialogo(){
        progress = new ProgressDialog(getContext());
        progress.setTitle("¡Espere por favor!");
        progress.setMessage("Actualizando...");
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
