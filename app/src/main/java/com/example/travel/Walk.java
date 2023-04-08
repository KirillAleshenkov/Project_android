package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Walk extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView = null;


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
        GeoPoint startPoint = new GeoPoint(51.2049,58.5668);
        mapController.setCenter(startPoint);
        //Поворот карты с помощью жестов
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        mapView.getOverlays().add(rotationGestureOverlay);
        rotationGestureOverlay.setEnabled(true);





        //your items
        //ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        //items.add(new OverlayItem("Title", "Description", new GeoPoint(51.6,58.29)));


        /*ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index,final OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index,final OverlayItem item) {
                return false;
            }
        })
        mOverlay.setFocusItemsOnTap(true);

        mapView.getOverlays().add(mOverlay);*/
        //mapView.getOverlays().add();
        //setContentView(mapView);


        Marker marker = new Marker(mapView);
        //marker.setPosition(new GeoPoint(51.2049,58.5668));
        Drawable drawable = getResources().getDrawable(R.drawable.free);
        //drawable.setS;
        marker.setIcon(drawable);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
        marker.setTitle("Маркер");
        marker.setSnippet("Комментарий");
        setContentView(mapView);


        Overlay mOverlay = new Overlay() {

            @Override
            public boolean onScroll(MotionEvent pEvent1, MotionEvent pEvent2, float pDistanceX, float pDistanceY, MapView pMapView) {

                marker.setPosition(new GeoPoint((float) pMapView.getMapCenter().getLatitude(),
                        (float) pMapView.getMapCenter().getLongitude()));

                return super.onScroll(pEvent1, pEvent2, pDistanceX, pDistanceY, pMapView);
            }
        };

        mapView.getOverlays().add(mOverlay);

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
