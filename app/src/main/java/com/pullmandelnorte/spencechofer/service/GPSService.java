package com.pullmandelnorte.spencechofer.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.pullmandelnorte.spencechofer.R;
import com.pullmandelnorte.spencechofer.entities.Viaje;
import com.pullmandelnorte.spencechofer.modelo.TrackingModel;
import com.pullmandelnorte.spencechofer.ws.api.API;
import com.pullmandelnorte.spencechofer.ws.api.apiServices.ReservaService;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GPSService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final Logger logger = Logger.getLogger(GPSService.class.getName());
    private static final int FOREGROUND_SERVICE_ID = 111;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    //    private static  int INTERVAL = 300000;
    private static  int INTERVAL = 90000; //cada 1 min y medio
    private static int FAST_INTERVAL = 90000; //cada 1 min y medio
    //    private static int FAST_INTERVAL = 300000;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private Location mCurrentLocation;
    private TrackingModel trackingModel;
    public GPSService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getString(R.string.app_name);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Connected through SDL")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build();
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(FOREGROUND_SERVICE_ID, notification);
        } else {
            startForeground(FOREGROUND_SERVICE_ID, notification,ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initGoogleAPIClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.info("onStartComand service");
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected())
                startLocationUpdates();
            else
                mGoogleApiClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this,"onDestroy service",Toast.LENGTH_SHORT).show();
        logger.info("onDestroy service");
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(@NonNull @NotNull Location location) {
        System.out.println("Location : " + location.getProvider());
        mCurrentLocation = location;
        refreshUI();
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
        logger.info("onConnected");
        if (!mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }
    @Override
    public void onConnectionSuspended(int i) {
        logger.info("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {
        logger.info("onConnectionFailed");
    }

    private void initGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            //Creamos una peticion de ubicacion con el objeto LocationRequest
            createLocationRequest();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FAST_INTERVAL);
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                mRequestingLocationUpdates = true;
            }
        }
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void refreshUI() {
        if (mCurrentLocation != null) {
//            mTvLatitud.setText(String.valueOf(mCurrentLocation.getLatitude()));
//            mTvLongitud.setText(String.valueOf(mCurrentLocation.getLongitude()));
//            Toast.makeText(this,"refreshUI - LATITUD : " + mCurrentLocation.getLatitude() + " - LONGITUD : " + mCurrentLocation.getLongitude() ,Toast.LENGTH_SHORT).show();
            logger.info("refreshUI2 - LATITUD : " + mCurrentLocation.getLatitude() + " - LONGITUD : " + mCurrentLocation.getLongitude() );
            crearTracking(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

        }
    }

    private void crearTracking(double latitude, double longitude) {
        List<Viaje> viaje = Viaje.listAll(Viaje.class);
        if(viaje != null && viaje.size() > 0){
            trackingModel = new TrackingModel();
            trackingModel.setIdViaje(viaje.get(0).getNroViaje());
            trackingModel.setLatitud(String.valueOf(latitude));
            trackingModel.setLongitud(String.valueOf(longitude));
            ReservaService rs = API.getApi().create(ReservaService.class);
            Call<ResponseBody> rsCall = rs.funCargarUbicacion(trackingModel);
            rsCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        System.out.println("Envio ubicacion : "+ latitude +"-" + longitude);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Error al enviar ubicacion : "+ latitude +"-" + longitude);
                }
            });
        }
    }
}