package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Walk extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView = null;
    private MyLocationNewOverlay myLocationOverlay = null;
    final Context context = this;
    //final DisplayMetrics dm = context.getResources().getDisplayMetrics();
    private CompassOverlay mCompassOverlay;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        mapView = new MapView(this);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(4.0);
        //Статичный поворот карты
        //mapView.setMapOrientation(45.0f);
        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(51.6, 58.29);
        mapController.setCenter(startPoint);
        //Поворот карты с помощью жестов
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        mapView.getOverlays().add(rotationGestureOverlay);
        mapView.setMultiTouchControls(true);
        rotationGestureOverlay.setEnabled(true);
        mapView.setMapOrientation(0);
        float currentBearing = mapView.getMapOrientation();
        RotateAnimation RotateAnimation = new RotateAnimation(currentBearing, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation.setDuration(1000);
        mapView.startAnimation(RotateAnimation);
        //чувтвительность поворота карты
        //this.mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapView);
        //this.mCompassOverlay.enableCompass();
        //mapView.getOverlays().add(this.mCompassOverlay);
        //Масштаб карты
        //ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        //mScaleBarOverlay.setCentred(true);
        //mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        //mapView.getOverlays().add(this.mCompassOverlay);


        // установка карты в макет
        setContentView(mapView);
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size()> 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
