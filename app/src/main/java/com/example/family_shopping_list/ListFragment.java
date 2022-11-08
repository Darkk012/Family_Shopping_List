package com.example.family_shopping_list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private Button productsDeleteBt;
    private ListView productsLV;
    private String[] productsArray;

    private Handler handler;
    private Integer delay=1000;

    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_list, container, false);
       productsLV=view.findViewById(R.id.productsLV);
       productsDeleteBt=view.findViewById(R.id.productsDeleteBt);

       db=FirebaseDatabase.getInstance();
       reference=db.getReference().child("families").child(ShoppingMainActivity.familyName).child("products");

       handler=new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               updateProductsList();
               handler.postDelayed(this,delay);
           }
       },delay);


       return view;
    }

    private void updateProductsList(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsArray=new String[(int) snapshot.getChildrenCount()];
                int i=0;
                for(DataSnapshot data: snapshot.getChildren()){
                    String p= (i+1) +". Termék:";
                    p+="\n \t " +data.child("name").getValue().toString()+
                            "\n \t Mennyiség:"+data.child("number").getValue().toString();
                    if(!data.child("information").getValue().toString().equals("")){
                        p+="\n \t Egyéb Információ: "+data.child("information").getValue().toString();
                    }
                    productsArray[i]=p;
                    i++;
                }
                productsLV.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,productsArray));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}