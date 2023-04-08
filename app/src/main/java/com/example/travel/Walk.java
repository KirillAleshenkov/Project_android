package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.GroundOverlay;
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

    MapView mMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);

        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        GeoPoint overlayCenterPoint = new GeoPoint(50.450667, 30.523193);
        IMapController mapController = mMapView.getController();
        mapController.setZoom(17f);
        mapController.setCenter(overlayCenterPoint);
        mMapView.setMapOrientation(0.0f);

        GroundOverlay myGroundOverlay = new GroundOverlay();
        myGroundOverlay.setPosition(overlayCenterPoint);
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.free, null);
        myGroundOverlay.setImage(d.mutate());
        myGroundOverlay.setDimensions(200.0f);
        myGroundOverlay.setTransparency(0.25f);
        myGroundOverlay.setBearing(0);
        mMapView.getOverlays().add(myGroundOverlay);

        mMapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    public class GroundOverlay extends Overlay {

        protected Drawable mImage;
        protected GeoPoint mPosition;
        protected float mBearing;
        protected float mWidth, mHeight;
        protected float mTransparency;
        public final static float NO_DIMENSION = -1.0f;
        protected Point mPositionPixels, mSouthEastPixels;

        public GroundOverlay() {
            super();
            mWidth = 10.0f;
            mHeight = NO_DIMENSION;
            mBearing = 0.0f;
            mTransparency = 0.0f;
            mPositionPixels = new Point();
            mSouthEastPixels = new Point();
        }

        public void setImage(Drawable image){
            mImage = image;
        }

        public Drawable getImage(){
            return mImage;
        }

        public GeoPoint getPosition(){
            return mPosition.clone();
        }

        public void setPosition(GeoPoint position){
            mPosition = position.clone();
        }

        public float getBearing(){
            return mBearing;
        }

        public void setBearing(float bearing){
            mBearing = bearing;
        }

        public void setDimensions(float width){
            mWidth = width;
            mHeight = NO_DIMENSION;
        }

        public void setDimensions(float width, float height){
            mWidth = width;
            mHeight = height;
        }

        public float getHeight(){
            return mHeight;
        }

        public float getWidth(){
            return mWidth;
        }

        public void setTransparency(float transparency){
            mTransparency = transparency;
        }

        public float getTransparency(){
            return mTransparency;
        }

        protected void computeHeight(){
            if (mHeight == NO_DIMENSION && mImage != null){
                mHeight = mWidth * mImage.getIntrinsicHeight() / mImage.getIntrinsicWidth();
            }
        }

        /** @return the bounding box, ignoring the bearing of the GroundOverlay (similar to Google Maps API) */
        public BoundingBox getBoundingBox(){
            computeHeight();
            GeoPoint pEast = mPosition.destinationPoint(mWidth, 90.0f);
            GeoPoint pSouthEast = pEast.destinationPoint(mHeight, -180.0f);
            double north = mPosition.getLatitude()*2 - pSouthEast.getLatitude();
            double west = mPosition.getLongitude()*2 - pEast.getLongitude();
            return new BoundingBox(north, pEast.getLongitude(), pSouthEast.getLatitude(), west);
        }

        public void setPositionFromBounds(BoundingBox bb){
            mPosition = bb.getCenterWithDateLine();
            GeoPoint pEast = new GeoPoint(mPosition.getLatitude(), bb.getLonEast());
            GeoPoint pWest = new GeoPoint(mPosition.getLatitude(), bb.getLonWest());
            mWidth = (float)pEast.distanceToAsDouble(pWest);
            GeoPoint pSouth = new GeoPoint(bb.getLatSouth(), mPosition.getLongitude());
            GeoPoint pNorth = new GeoPoint(bb.getLatNorth(), mPosition.getLongitude());
            mHeight = (float)pSouth.distanceToAsDouble(pNorth);
        }

        @Override public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            if (shadow)
                return;
            if (mImage == null)
                return;

            computeHeight();

            final Projection pj = mapView.getProjection();

            pj.toPixels(mPosition, mPositionPixels);
            GeoPoint pEast = mPosition.destinationPoint(mWidth / 2, 90.0f);
            GeoPoint pSouthEast = pEast.destinationPoint(mHeight / 2, -180.0f);
            pj.toPixels(pSouthEast, mSouthEastPixels);
            int hWidth = mSouthEastPixels.x - mPositionPixels.x;
            int hHeight = mSouthEastPixels.y - mPositionPixels.y;
            mImage.setBounds(-hWidth, -hHeight, hWidth, hHeight);

            mImage.setAlpha(255 - (int) (mTransparency * 255));

            drawAt(canvas, mImage, mPositionPixels.x, mPositionPixels.y, false, -mBearing);
        }

    }
}