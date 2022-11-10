package com.example.family_shopping_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private EditText loginNameEt,loginPasswordEt;
    private Button loginBt,registerFBt;
    private MainCallbackFragment mainCallbackFragment;
    private String name,password,message;


    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        loginNameEt=view.findViewById(R.id.loginNameEt);
        loginPasswordEt= view.findViewById(R.id.loginPasswordEt);
        loginBt=view.findViewById(R.id.loginBt);
        registerFBt=view.findViewById(R.id.registerFBt);


        loginNameEt.setText("");
        loginPasswordEt.setText("");

        db=FirebaseDatabase.getInstance();
        reference=db.getReference().child("families");

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=loginNameEt.getText().toString();
                password=loginPasswordEt.getText().toString();

                if(!name.equals("") && !password.equals("")) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean notfFound = true;
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if (data.getKey().equals(name) &&
                                        data.child("password").getValue().equals(password)) {
                                    Intent i = new Intent(getContext(), ShoppingMainActivity.class);
                                    i.putExtra("familyName", name);
                                    startActivity(i);
                                    notfFound = false;
                                    loginNameEt.setText("");
                                    loginPasswordEt.setText("");
                                }
                            }
                            if (notfFound) {
                                AlertDialog.Builder cantlogin = new AlertDialog.Builder(getActivity());
                                cantlogin.setMessage("Ilyen felhasználónév, jelszó páros nem létezik!");
                                cantlogin.setPositiveButton("Oké", null);
                                cantlogin.create().show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Database Error"+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    AlertDialog.Builder notGood= new AlertDialog.Builder(getActivity());
                    message="Hiba: ";
                    if (name.equals("")) message += "\n - Nincs felhasználónév megadva";
                    if (password.equals("")) message += "\n - Nincs jelszó megadva";
                    message += "!";
                    notGood.setMessage(message);
                    notGood.setPositiveButton("Oké",null);
                    notGood.create().show();
                }
            }
        });


        registerFBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainCallbackFragment !=null){
                    mainCallbackFragment.changeMainFragment();
                }
            }
        });
        return view;
    }

    public void setMainCallbackFragment(MainCallbackFragment mainCallbackFragment){
        this.mainCallbackFragment=mainCallbackFragment;
    }


}