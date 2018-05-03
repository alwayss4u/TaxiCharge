package com.roy.taxicharge;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

        public class MainActivity extends AppCompatActivity {

            ActionBarDrawerToggle drawerToggle;
            DrawerLayout drawerLayout;
            NavigationView navigationView;
            RecyclerView recyclerView;
            MenuAdapter menuAdapter;
            Bitmap[] bitmaps = new Bitmap[3];
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                drawerLayout = findViewById(R.id.layout_drawer);
                navigationView = findViewById(R.id.nav);
        recyclerView = findViewById(R.id.list_main);

        //item icon색조를 적용하지 않음
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

        Resources res = getResources();

        for(int i = 0; i< 3; i++){

            bitmaps[i] = BitmapFactory.decodeResource(res, R.drawable.taxi_1 +i);

        }
        Log.i("aabbsd", bitmaps.toString());
        menuAdapter = new MenuAdapter(this, bitmaps);
        recyclerView.setAdapter(menuAdapter);
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

}