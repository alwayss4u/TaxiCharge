package com.roy.taxicharge;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
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

    NMapOverlayManager nMapOverlayManager;
    //nMapViewerResourceProvider nMapViewerResourceProvider = new nMapViewerResourceProvider(this);
    NMapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mMapView, nMapViewerResourceProvider);

        longitudes = getIntent().getDoubleArrayExtra("longitude");
        latitudes = getIntent().getDoubleArrayExtra("latitude");

        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();


        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        mMapView.setBuiltInZoomControls(true, null);
        //지도를 확대하거나 축소할 때 내장된 컨트롤러를 사용
        NMapPathData pathData = new NMapPathData(longitudes.length);

        pathData.initPathData();

        for(int i= 0; i<longitudes.length; i++)
            pathData.addPathPoint(latitudes[i], longitudes[i], 0);

        pathData.endPathData();



//        NMapPathDataOverlay pathDataOverlay = nMapOverlayManager.createPathDataOverlay();

//        String clientId = "YOUR_CLIENT_ID";//애플리케이션 클라이언트 아이디값";
//        String clientSecret = "YOUR_CLIENT_SECRET";//애플리케이션 클라이언트 시크릿값";
//        try {
//            String addr = URLEncoder.encode("불정로 6", "UTF-8");
//            String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
//            //String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml
//            URL url = new URL(apiURL);
//            HttpURLConnection con = (HttpURLConnection)url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("X-Naver-Client-Id", clientId);
//            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
//            int responseCode = con.getResponseCode();
//            BufferedReader br;
//            if(responseCode==200) { // 정상 호출
//                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            } else {  // 에러 발생
//                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            }
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = br.readLine()) != null) {
//                response.append(inputLine);
//            }
//            br.close();
//            System.out.println(response.toString());
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success
            mMapController.setMapCenter(new NGeoPoint(latitudes[latitudes.length/2], longitudes[longitudes.length/2]), 11);
        } else { // fail
            Log.e(LOG_TAG, "onMapInitHandler: error=" + errorInfo.toString());
        }
    }

}
