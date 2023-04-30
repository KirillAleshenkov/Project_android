package com.example.travel;

import static android.graphics.Color.rgb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

//import com.google.android.gms.maps.MapView;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Walk extends AppCompatActivity {

    private Button button;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //button = findViewById(R.id.button2);
        //


        Button myButton = new Button(this);
        myButton.setText("Push Me");
        myButton.setLeft(10);
        myButton.setRight(10);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        mapView = new MapView(this);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(4.0);
        //Статичный поворот карты
        //mapView.setMapOrientation(45.0f);
        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        GeoPoint loc = new GeoPoint(51.2049, 58.5668);
        GeoPoint startPoint = loc;
        mapController.setCenter(startPoint);


        Marker marker = new Marker(mapView);

        Drawable drawable = getResources().getDrawable(R.drawable.marker);
        marker.setIcon(drawable);

        marker.setTitle("Маркер");
        marker.setSnippet("Комментарий");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setPosition(loc);


        //Поворот карты с помощью жестов
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(this, mapView);
        mapView.getOverlays().add(rotationGestureOverlay);
        rotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(marker);


        List<GeoPoint> geoPoint = new ArrayList<>();
        geoPoint.add(new GeoPoint(51.3049, 58.5668));
        geoPoint.add(new GeoPoint(51.4049, 58.7668));
        geoPoint.add(new GeoPoint(51.5049, 58.5668));
        Polyline line = new Polyline();   //see note below!
        line.setColor(rgb(0, 255, 0));
        line.setWidth(5);
        line.setPoints(geoPoint);
        mapView.getOverlayManager().add(line);




        CompassOverlay mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mapView);
        mapView.getOverlays().add(mCompassOverlay);
        mCompassOverlay.enableCompass();
        //mapView.setTilesScaledToDpi(true);
        //mapView.setLayerType (View.LAYER_TYPE_SOFTWARE, null); // Отключить аппаратное ускорение (требуется при рисовании трасс)
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mapView.getOverlays().add(mScaleBarOverlay);
        mScaleBarOverlay.setAlignBottom(true);
        mScaleBarOverlay.setLineWidth(1 * (getResources().getDisplayMetrics()).density);
        mScaleBarOverlay.setMaxLength(0.85f);
        //mapView.getOverlay.add(myButton);
        mapView.addView(myButton,200,200);
    }

    public class ContentFragment1 extends Fragment {
        public ContentFragment1(){
            super(R.layout.fragment_button);
        }
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
                    permissionsToRequest.toArray(new String[0]),REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}