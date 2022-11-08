package com.example.family_shopping_list;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.renderscript.ScriptGroup;

public class MainActivity extends AppCompatActivity implements MainCallbackFragment  {

    private Fragment mainFragment;
    private FragmentManager mainFragmentManager;
    private FragmentTransaction mainFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMainFragment();
    }

    private void addMainFragment(){
        LoginFragment mainFragment= new LoginFragment();
        mainFragment.setMainCallbackFragment(this);
        mainFragmentManager= getSupportFragmentManager();
        mainFragmentTransaction= mainFragmentManager.beginTransaction();
        mainFragmentTransaction.add(R.id.mainFrame,mainFragment);
        mainFragmentTransaction.commit();
    }

    private void replaceMainFragment(){
        mainFragment= new RegisterFragment();
        mainFragmentManager= getSupportFragmentManager();
        mainFragmentTransaction= mainFragmentManager.beginTransaction();
        mainFragmentTransaction.addToBackStack(null);
        mainFragmentTransaction.replace(R.id.mainFrame,mainFragment);
        mainFragmentTransaction.commit();
    }


    @Override
    public void changeMainFragment() {
        replaceMainFragment();
    }
}