package com.roy.taxicharge;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.nhn.android.naverlogin.OAuthLoginDefine.LOG_TAG;

public class ResultActivity extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "3l8gani7OtvloAAc4FQf";// 애플리케이션 클라이언트 아이디 값


    double[] longitudes;
    double[] latitudes;

    NMapOverlayManager mOverlayManager;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapController mMapController;

    int markerId = NMapPOIflagType.PIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        longitudes = getIntent().getDoubleArrayExtra("longitude");
        latitudes = getIntent().getDoubleArrayExtra("latitude");

        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);
        setContentView(mMapView);
        mMapView.setClickable(true);

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

// create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        // register listener for map state changes
        //mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        //mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);


        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();


        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();
        // use built in zoom controls
        mMapView.setBuiltInZoomControls(true, null);
        //지도를 확대하거나 축소할 때 내장된 컨트롤러를 사용

        NMapPathData pathData = new NMapPathData(longitudes.length);

        pathData.initPathData();

        for(int i= 0; i<longitudes.length; i++){
            if(i==0) {
                pathData.addPathPoint(longitudes[i], latitudes[i], NMapPathLineStyle.TYPE_SOLID);
                continue;
            }
            pathData.addPathPoint(longitudes[i], latitudes[i], 0);
        }

        String latitu = "";
        String longitu = "";
        for(int i=0; i< latitudes.length; i++){
            latitu += latitudes[i];
            longitu += longitudes[i];
        }
        Log.i("latitudes", latitu);
        Log.i("longitutds", longitu);

        pathData.endPathData();

        NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);

        pathDataOverlay.showAllPathData(0);
    }

    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success
            mMapController.setMapCenter(new NGeoPoint(latitudes[latitudes.length/2], longitudes[longitudes.length/2]), 11);
        } else { // fail
            Log.e(LOG_TAG, "onMapInitHandler: error=" + errorInfo.toString());
        }
    }

    public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
        // [[TEMP]] handle a click event of the callout
        Toast.makeText(this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
    }

    public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
        if (item != null) {
            Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
        } else {
            Log.i(LOG_TAG, "onFocusChanged: ");
        }
    }

    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
        // set your callout overlay
        return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
    }

}
