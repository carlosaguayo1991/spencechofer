package com.pullmandelnorte.spencechofer.ui.viajes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pullmandelnorte.spencechofer.EscaneoActivity;
import com.pullmandelnorte.spencechofer.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link lecturaQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lecturaQrFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnScanner;
    private TextView resultado;

    public lecturaQrFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lecturaQrFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static lecturaQrFragment newInstance(String param1, String param2) {
        lecturaQrFragment fragment = new lecturaQrFragment();
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
        View view = inflater.inflate(R.layout.fragment_lectura_qr, container, false);
        btnScanner = view.findViewById(R.id.button);
        resultado = view.findViewById(R.id.resultado);
        btnScanner.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if( intentResult != null)
            if(intentResult.getContents() != null){
                resultado.setText(intentResult.getContents());
            }else{
                resultado.setText("ERROR");
            }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
//                new IntentIntegrator(getActivity()).initiateScan();
                Intent intent = new Intent(v.getContext(), EscaneoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
