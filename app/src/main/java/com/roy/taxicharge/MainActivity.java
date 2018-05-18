package com.roy.taxicharge;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;

public class MainActivity extends AppCompatActivity {

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "3l8gani7OtvloAAc4FQf";
    private static String OAUTH_CLIENT_SECRET = "0ZPhPzMx4m";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private OAuthLoginButton mOAuthLoginButton;
    private ImageButton btn_logout;

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    MenuAdapter menuAdapter;
    Bitmap[] bitmaps = new Bitmap[3];

    ArrayList<String> profile_Items = new ArrayList<>();
    View header_navi;
    Context mContext;

    String accessToken;

    TextView header_name;
    TextView header_email;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.layout_drawer);
        recyclerView = findViewById(R.id.list_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewNavigation();

        //CardView Image
        Resources res = getResources();

        for(int i = 0; i< 3; i++)
            bitmaps[i] = BitmapFactory.decodeResource(res, R.drawable.taxi_1+i);

        menuAdapter = new MenuAdapter(this, bitmaps);
        recyclerView.setAdapter(menuAdapter);

        mContext = this;



        initData();
        //Log.i("test", accessToken);
        if(accessToken==null){
            mOAuthLoginButton.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.INVISIBLE);
        }else {
            updateHeader(R.id.btn_Logout);
            mOAuthLoginButton.setVisibility(View.INVISIBLE);
            btn_logout.setVisibility(View.VISIBLE);
        }

    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    //NavigationView
    private void viewNavigation(){
        navigationView = findViewById(R.id.nav);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch ((item.getItemId())){
                    case R.id.user_search :
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_blacklist :
                        Toast.makeText(MainActivity.this, "BlackList", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_setting :
                        Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });


        header_navi = navigationView.getHeaderView(0);

        mOAuthLoginButton = header_navi.findViewById(R.id.buttonOAuthLoginImg);
        btn_logout = header_navi.findViewById(R.id.btn_Logout);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        circleImageView = header_navi.findViewById(R.id.cv);
        header_name = header_navi.findViewById(R.id.header_textID);
        header_email = header_navi.findViewById(R.id.header_textEmail);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                accessToken = mOAuthLoginInstance.getAccessToken(mContext);
//                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
//                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
//                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                naverAPI();

            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };
    //TODO : LOGIN UPDATE HEADER 수정.
    public void naverAPI() {
        String header = "Bearer " + accessToken; // Bearer 다음에 공백 추가
        Log.i("test", 1+"");
        try {
            String apiURL = "http://openapi.naver.com/v1/nid/me";
            Log.i("test", 2+"");
            URL url = new URL(apiURL);
            Log.i("test", 3+"");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            Log.i("test", 4+"");
            con.setRequestMethod("GET");
            Log.i("test", 5+"");
            con.setRequestProperty("Authorization", header);
            Log.i("test", con.getResponseCode()+"");
            int responseCode = con.getResponseCode();
            Log.i("test", 7+"");
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                Log.i("test", 8+"");
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            Log.i("test", response.toString());
            JSONArray profileArray = new JSONObject(response.toString()).getJSONArray("response");
            Log.i("test", profileArray.toString());
            for(int i = 0; i < profileArray.length(); i++){

                JSONObject datas = profileArray.getJSONObject(i);

                profile_Items.add(datas.optString("email"));
                profile_Items.add(datas.optString("nickname"));
                profile_Items.add(datas.optString("profile_image"));
                Log.i("test", datas.optString("email"));
            }
            Log.i("test", accessToken);
            updateHeader(R.id.buttonOAuthLoginImg);

        } catch (Exception e) {
            Log.i("test", e.getMessage()+"");
        }
    }

    void updateHeader(int viewID){
        switch (viewID){

            case R.id.buttonOAuthLoginImg :
                header_name.setText(profile_Items.get(1));
                header_email.setText(profile_Items.get(0));
                Glide.with(mContext).load(profile_Items.get(2)).into(circleImageView);
                break;
            case R.id.btn_Logout :
                header_name.setText("Guest");
                header_email.setText(profile_Items.get(0));
                Glide.with(mContext).load(R.drawable.ic_account_circle_black_48dp).into(circleImageView);
                break;

        }
    }

    //액션바의 메뉴를 클릭하는 이벤트를 듣는 메소드를 통해
    // 클릭상황을 전달받도록..
    //토글버튼이 클릭상황을 인지하도록....
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    public void clickHeader(View view){

        Toast.makeText(this, "Header_Icon", Toast.LENGTH_SHORT).show();
    }
    public void clickListItem(View view){

    }

    public void clickNaverLogout(View view){
        mOAuthLoginInstance.logout(mContext);
        accessToken = null;
        updateHeader(R.id.btn_Logout);
    }


}