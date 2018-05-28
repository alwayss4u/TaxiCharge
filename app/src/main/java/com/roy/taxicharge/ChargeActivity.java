package com.roy.taxicharge;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChargeActivity extends AppCompatActivity {


    LocationManager locationManager;
    ArrayList<PositionType> position = new ArrayList<>();
    TextView chargeTV;
    TextView speedTV;
    ImageView[] gauges = new ImageView[5];
    Location location;
    ArrayList<Long> datelist = new ArrayList<>();
    double speed;
    final int ADDCOIN = 100;
    final double MINSPEED = 15;
    double gauge = 0;
    int basicChargeGauge;
    int taxiCharge;
    float[] distanceForValue = new float[2]; //거리결과를 저장하는

    TextView testTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);


        testTv = findViewById(R.id.testTv);

        speedTV = findViewById(R.id.speedTotext);
        chargeTV = findViewById(R.id.chargeToText);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        gauges[0] = findViewById(R.id.gauge_1);
        gauges[1] = findViewById(R.id.gauge_2);
        gauges[2] = findViewById(R.id.gauge_3);
        gauges[3] = findViewById(R.id.gauge_4);
        gauges[4] = findViewById(R.id.gauge_5);

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

            if (location != null) {

                double latitude= location.getLatitude();
                double longitude= location.getLongitude();
                position.add(new PositionType(latitude, longitude));

                speedForHour(location);

                int minutes = (int)(datelist.get(datelist.size()-1)-datelist.get(0))/60;
                int hours = minutes/60;

                if(speed <= MINSPEED){
                    //TODO : GPS TEST
                    if(position.size()<3) return;
                    long lateTime = datelist.get(datelist.size()-1) -datelist.get(datelist.size()-2);
                    gauge += lateTime*4.057;
                    basicChargeGauge += gauge;
                    //testTv.append("Gauge = " + gauge +", LateTime = "+ lateTime*4.057 + "\n");
                    testTv.append(datelist.get(datelist.size()-1) +" - " + datelist.get(0) + " = " + (datelist.get(datelist.size()-1)- datelist.get(0)) + "\n");

                    testTv.append(hours +" : "+  minutes +" : "+(datelist.get(datelist.size()-1)-datelist.get(0))+"\n");
                    //if(basicChargeGauge<2000) testTv.append("기본Gauge" + basicChargeGauge +"\n");
                    if(gauge >=142){
                        for(int i = 0; i < gauges.length; i++) gauges[i].setVisibility(View.INVISIBLE);

                        if(basicChargeGauge <2000) return;

                        taxiCharge += ADDCOIN;
                        gauge = 0;
                        chargeTV.setText("￦"+taxiCharge);
                    }
                }
                else{
                    if(distanceForValue[0]==0){ return; }
                    gauge += distanceForValue[0];
                    basicChargeGauge += gauge;
                    //testTv.append("Gauge = " + gauge +", Distance = "+ distanceForValue[0] + "\n");
                    testTv.append(datelist.get(datelist.size()-1) +" - " + datelist.get(0) + " = " + (datelist.get(datelist.size()-1)- datelist.get(0))+"\n");
                    testTv.append(hours +" : "+  minutes +" : "+(datelist.get(datelist.size()-1)-datelist.get(0))+"\n");

                    if(basicChargeGauge<2000) testTv.append("기본Gauge" + basicChargeGauge +"\n");
                    if(gauge >=142){
                        for(int i = 0; i < gauges.length; i++) gauges[i].setVisibility(View.INVISIBLE);

                        if(basicChargeGauge <2000) return;

                        taxiCharge += ADDCOIN;
                        gauge = 0;
                        chargeTV.setText("￦"+taxiCharge);
                    }
                }

                //Gauge Image
                if(gauge > 20){
                    gauges[0].setVisibility(View.VISIBLE);
                    if(gauge > 48){
                        gauges[1].setVisibility(View.VISIBLE);
                        if(gauge > 76){
                            gauges[2].setVisibility(View.VISIBLE);
                            if(gauge > 102){
                                gauges[3].setVisibility(View.VISIBLE);
                                if(gauge > 130){
                                    gauges[4].setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
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
            locationManager.requestLocationUpdates("gps", 3000, 2, locationListener);
        }else if(locationManager.isProviderEnabled("network")){
            locationManager.requestLocationUpdates("network", 3000, 2, locationListener);
        }

        if(location==null){
            Toast.makeText(this, "Location is not found!! \n \t & Check Start", Toast.LENGTH_LONG).show();
        }else{
            double latitude= location.getLatitude();
            double longitude= location.getLongitude();
            position.add(new PositionType(latitude, longitude)); //첫 시작 포인트

            speedForHour(location);
        }

        taxiCharge = 3000;

        chargeTV.setText("￦"+taxiCharge);
    }

    public void clickArrived(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        if (datelist.size() < 10 || position.size() < 10) {
            Toast.makeText(this, "이동거리가 너무 짧거나, Start 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //내 위치 자동 갱신 제거
        locationManager.removeUpdates(locationListener);

        //AlertDialog를 만들어주는 건축가(Builder)객체 생성
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Result");
        builder.setIcon(android.R.drawable.ic_dialog_info);

        //커스텀뷰로 메세지영역 설정하기


        LayoutInflater inflater= getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_result, null);

        TextView text_ResultTime = view.findViewById(R.id.timeTv);
        TextView taxiChargeText = view.findViewById(R.id.chargeTv);

        taxiChargeText.setText("￦ "+taxiCharge);

        int minute_result = (int)((datelist.get(datelist.size()-1)-datelist.get(0))/60);

        int second_result = (int)((datelist.get(datelist.size()-1)-datelist.get(0))%60);

        int hour_result = minute_result/60;

       text_ResultTime.setText(hour_result + " : " + (minute_result-hour_result*60) + " : " + second_result);

        builder.setView(view);

        builder.setPositiveButton("경로비교", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //이전 Activity의 기록 삭제.

                Intent intent = new Intent(builder.getContext(), ResultActivity.class);
                intent.putExtra("course", position);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(builder.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        AlertDialog dialog= builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void speedForHour(Location location){
        //Date
        datelist.add(System.currentTimeMillis()/1000);

        if(position.size()>2) {
            location.distanceBetween(position.get(position.size() - 1).latitude, position.get(position.size() - 1).longitude, position.get(position.size() - 2).latitude, position.get(position.size() - 2).longitude, distanceForValue);

            long timeForSpeed = datelist.get(datelist.size() - 1) - datelist.get(datelist.size() - 2);

            speed = (distanceForValue[0] / (double) timeForSpeed) *3.6;
            //mySpeed = location.getSpeed();
            if(location.hasSpeed()){
                speedTV.setText(Math.round(speed*100d)/100 +" km/h");
            }
            else {
                speedTV.setText("0 km/h");
            }
        }else{
            speedTV.setText(0 + " km/h");
        }
    }
}