package com.roy.taxicharge;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NaviActivity extends AppCompatActivity {


    LocationManager locationManager;
    ArrayList<PositionType> position = new ArrayList<>();
    TextView textView;
    TextView testTV;
    Location location;
    ArrayList<Long> datelist = new ArrayList<>();
    double speed;
    final int ADDCOIN = 100;
    final double minSpeed = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        testTV = findViewById(R.id.testTV);
        textView = findViewById(R.id.tv);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
// 정확도
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
// 전원 소비량
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
// 고도, 높이 값을 얻어 올지를 결정
        criteria.setAltitudeRequired(false);
// provider 기본 정보(방위, 방향)
        criteria.setBearingRequired(false);
// 속도
        criteria.setSpeedRequired(false);
// 위치 정보를 얻어 오는데 들어가는 금전적 비용
        criteria.setCostAllowed(true);
        //locationProvider = locationManager.getBestProvider(criteria, true);

        //마시멜로우(api23버전)부터 동적퍼미션 필요 : 앱을 다운받을 때 뿐만아니라 사용할때도 퍼미션을 체크하는 방식
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //퍼미션을 요청 다이얼로그 화면 보이기...
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, 10);
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 10:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치정보제공에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("adsf", "isChanged?");
            if (location != null) {

                double latitude= location.getLatitude();
                double longitude= location.getLongitude();
                position.add(new PositionType(latitude, longitude));

                speedForHour(location);

                testTV.append(latitude +"m , "+ longitude + "m");

                if(speed <= minSpeed){

                }
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void clickStart(View view){
        //퍼미션 체크...안되어있으면 하지마!!!
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        location = null;

            //내 위치 자동 갱신
        if(locationManager.isProviderEnabled("gps")){
            locationManager.requestLocationUpdates("gps", 5000, 2, locationListener);
        }else if(locationManager.isProviderEnabled("network")){
            locationManager.requestLocationUpdates("network", 5000, 2, locationListener);
        }

        if(location==null){
            Toast.makeText(this, "Location is not found!! \n \t & Check Start", Toast.LENGTH_LONG).show();
        }else{
            double latitude= location.getLatitude();
            double longitude= location.getLongitude();
            position.add(new PositionType(latitude, longitude)); //첫 시작 포인트

            speedForHour(location);

        }
    }



    public void clickArrived(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //내 위치 자동 갱신 제거
        locationManager.removeUpdates(locationListener);

    }

    public void speedForHour(Location location){
        //Date
        Log.i("adsf", "isEnter to Method?");
        Date date = new Date(System.currentTimeMillis());
        long a1=date.getTime()/1000;
        datelist.add(a1);
        if(position.size()>2) {
            Log.i("adsf", "isEnter to If?");
            float[] distanceForSpeed = new float[2]; //거리결과를 저장하는
            location.distanceBetween(position.get(position.size() - 1).latitude, position.get(position.size() - 1).longitude, position.get(position.size() - 2).latitude, position.get(position.size() - 2).longitude, distanceForSpeed);

            long timeForSpeed = datelist.get(datelist.size() - 1) - datelist.get(datelist.size() - 2);

            speed = (distanceForSpeed[0] / (double) timeForSpeed) *3.6;

            testTV.append(distanceForSpeed[0]+"\n");

            //mySpeed = location.getSpeed();
            if(location.hasSpeed()){
                textView.setText("Current Speed : " + speed + " km/h");
            }
            else {
                textView.setText("0 km/h");
            }

        }else{
            textView.setText("Current Speed : " + 0 + " km/h");
        }
    }

}
