package com.example.ben.tezfillup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mMenuButton;
    FragmentAccount fragmentAccount;
    FragmentGuide fragmentGuide;
    FragmentSlide fragmentSlide;
    FragmentOcr fragmentOcr;
    String PageName;

    public static AppBarLayout app;
    public static DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences.Editor editor = getSharedPreferences("MYPREFS", MODE_PRIVATE).edit();
        editor.putString("PageName", "MainActivity");
        editor.commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mMenuButton = findViewById(R.id.circle_menu_main_button);
        app=(AppBarLayout) findViewById(R.id.app);

        final CircleMenuView menu = findViewById(R.id.circle_menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Bundle bundle=new Bundle();
                if(index!=0){
                }
                switch (index) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        fragmentSlide = FragmentSlide.newInstance(true);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.container, fragmentSlide);
                        ft.addToBackStack("fragmentSlide");
                        ft.commit();
                        mMenuButton.setX(0);
                        mMenuButton.setY(0);
                        break;
                    case 2:
                        bundle.putString("sayfa", "form");
                        fragmentOcr = FragmentOcr.newInstance(true);
                        FragmentManager fragmentManager2 = getFragmentManager();
                        FragmentTransaction ft2 = fragmentManager2.beginTransaction();
                        fragmentOcr.setArguments(bundle);
                        ft2.replace(R.id.container, fragmentOcr);
                        ft2.commit();
                        mMenuButton.setX(0);
                        mMenuButton.setY(0);
                        break;
                    case 3:
                        fragmentAccount = FragmentAccount.newInstance(true);
                        FragmentManager fragmentManager3 = getFragmentManager();
                        FragmentTransaction ft3 = fragmentManager3.beginTransaction();
                        ft3.replace(R.id.container, fragmentAccount);
                        ft3.commit();
                        mMenuButton.setX(0);
                        mMenuButton.setY(0);
                        break;
                    case 4:
                        fragmentGuide = FragmentGuide.newInstance(true);
                        FragmentManager fragmentManager4 = getFragmentManager();
                        FragmentTransaction ft4 = fragmentManager4.beginTransaction();
                        ft4.replace(R.id.container, fragmentGuide);
                        ft4.commit();
                        mMenuButton.setX(0);
                        mMenuButton.setY(0);
                        break;
                }
            }

        });

    }

    @Override
    public void onBackPressed() {

        SharedPreferences SP = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        PageName = SP.getString("PageName", null);

        try{
            if(fragmentSlide.onBackPressed() || fragmentAccount.onBackPressed() || fragmentGuide.onBackPressed() || fragmentOcr.onBackPressed()) {
                super.onBackPressed();
                PageName = "MainActivity";
            }
        }
        catch (Exception e){
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        }
   }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }


}