package com.example.family_shopping_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class ShoppingMainActivity extends AppCompatActivity {

    private TextView familyTv;
    static String familyName;
    private BottomNavigationView menu;

    private ListFragment listFragment= new ListFragment();
    private ListAddFragment listAddFragment= new ListAddFragment();
    private SettingFragment settingFragment= new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_main);
        familyTv=findViewById(R.id.familyTv);
        Bundle extra=getIntent().getExtras();
        if(extra!=null){
            familyTv.setText(extra.getString("familyName") +" család");
            familyName=extra.getString("familyName");
        }else{
            familyTv.setText("NULL család");
        }

        menu= findViewById(R.id.menu);
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.list:
                        getSupportFragmentManager().beginTransaction().replace(R.id.shoppingFrame,listFragment).commit();
                        return true;
                    case R.id.list_plus:
                        getSupportFragmentManager().beginTransaction().replace(R.id.shoppingFrame,listAddFragment).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.shoppingFrame,settingFragment).commit();
                        return true;
                }
                return false;
            }
        });
        menu.setSelectedItemId(R.id.list);
    }





}