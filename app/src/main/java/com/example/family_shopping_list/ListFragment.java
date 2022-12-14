package com.example.family_shopping_list;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private Button productsDeleteBt,buyProductsBt;
    private ListView productsLV;
    private Product[] productsArray;
    private FloatingActionButton helpFab;
    private ArrayAdapter<Product> adapter;

    private Handler handler;
    private Runnable runnable;
    private Integer delay=500;

    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_list, container, false);
       productsLV=view.findViewById(R.id.productsLV);
       productsDeleteBt=view.findViewById(R.id.productsDeleteBt);
       buyProductsBt=view.findViewById(R.id.buyProductsBt);
       helpFab=view.findViewById(R.id.helpFab);

       db=FirebaseDatabase.getInstance();
       reference=db.getReference().child("families").child(ShoppingMainActivity.familyName).child("products");

       runnable=new Runnable() {
           @Override
           public void run() {
               updateProductsList();
               handler.postDelayed(this,delay);
           }
       };
       handler=new Handler();
       handler.post(runnable);

        buyProductsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data:snapshot.getChildren()){
                            if(Integer.parseInt(data.child("state").getValue().toString())==2){
                                reference.child(data.getKey()).child("state").setValue(3);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Database Error:"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        productsDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        List<Product> pList=new ArrayList<>();
                        for(DataSnapshot data:snapshot.getChildren()){
                            if(Integer.parseInt(data.child("state").getValue().toString())!=3){
                                Product p=new Product();
                                p.setName(data.child("name").getValue().toString());
                                p.setNumber(Integer.parseInt(data.child("number").getValue().toString()));
                                if(!data.child("information").getValue().equals("")){
                                    p.setInformation(data.child("information").getValue().toString());
                                }else p.setInformation("");
                                p.setState(Integer.parseInt(data.child("state").getValue().toString()));
                                pList.add(p);
                            }
                        }
                        reference.removeValue();
                        int i=1;
                        for (Product p:pList){
                            reference.child(String.valueOf(i)).setValue(p);
                            i++;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Database Error:"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        helpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder help=new AlertDialog.Builder(getActivity());
                help.setMessage("Haszn??lati utas??t??sok:" +
                        "\n1.A legbaloldali f??l??n lehet ??j term??ket hozz??adni" +
                        "\n2.A k??z??ps?? f??l??n lehet a hozz??adott term??keket megtekinteni" +
                        "\n3.Szink??d: " +
                        "\n\t-Z??ld: A term??k nincs a kos??rban ??s nincs megv??ve sem" +
                        "\n\t-S??rga: A term??k a kos??rban van" +
                        "\n\t-Piros: A term??k megvan v??ve" +
                        "\n4.Term??kek megv??s??rl??sa gomb: A kos??rban l??v?? term??keket ??tv??ltosztatja megvettnek" +
                        "\n5.Megv??s??rolt term??kek t??rl??se gomb: A megv??s??rolt term??keket kit??rli a list??b??l");
                help.setPositiveButton("Ok??",null).create().show();
            }
        });

       return view;
    }

    private void updateProductsList(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsArray=new Product[(int) snapshot.getChildrenCount()];
                int i=0;
                for(DataSnapshot data: snapshot.getChildren()){
                    Product p=new Product();
                    p.setName(data.child("name").getValue().toString());
                    p.setNumber(Integer.parseInt(data.child("number").getValue().toString()));
                    if(!data.child("information").getValue().equals("")){
                        p.setInformation(data.child("information").getValue().toString());
                    }
                    p.setState(Integer.parseInt(data.child("state").getValue().toString()));
                    productsArray[i]=p;
                    i++;
                }
                adapter=new ArrayAdapter<Product>(getContext(), android.R.layout.simple_list_item_1,productsArray){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View v= super.getView(position, convertView, parent);
                        TextView Tv=v.findViewById(android.R.id.text1);
                        if(productsArray[position].getState()==1){
                            Tv.setTextColor(0xFF14591D);
                        }else if(productsArray[position].getState()==2){
                            Tv.setTextColor(0xFFCC8B00);
                        }else if(productsArray[position].getState()==3){
                            Tv.setTextColor(0xFFBB342F);
                        }
                        return v;
                    }
                };
                productsLV.setAdapter(adapter);

                productsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(productsArray[i].getState()==1){
                            productsArray[i].setState(2);
                            reference.child(String.valueOf(i+1)).child("state").setValue(2);
                        }else if(productsArray[i].getState()==2){
                            productsArray[i].setState(1);
                            reference.child(String.valueOf(i+1)).child("state").setValue(1);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error"+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}