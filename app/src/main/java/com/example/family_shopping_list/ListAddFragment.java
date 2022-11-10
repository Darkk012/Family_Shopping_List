package com.example.family_shopping_list;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListAddFragment extends Fragment {

    private EditText productInformationEt,productNameEt,productNumberEt;
    private Button listAddBt;

    private String productName,productInformation,productNumberString,message;
    private Integer productNumber;


    private Product product;

    private FirebaseDatabase db;
    private DatabaseReference reference;
    private long maxid=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list_add, container, false);
        productInformationEt=view.findViewById(R.id.productInformationEt);
        productNameEt=view.findViewById(R.id.productNameEt);
        productNumberEt=view.findViewById(R.id.productNumberEt);
        listAddBt=view.findViewById(R.id.listAddBt);
        productNumber=0;
        product=new Product();

        db=FirebaseDatabase.getInstance();
        reference=db.getReference().child("families").child(ShoppingMainActivity.familyName).child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid =(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error"+error, Toast.LENGTH_SHORT).show();
            }
        });

        listAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName=productNameEt.getText().toString();
                productInformation=productInformationEt.getText().toString();
                productNumberString=productNumberEt.getText().toString();
                if(!productNumberString.equals("")) {
                    productNumber = Integer.parseInt(productNumberString);
                }

                if(!productName.equals("") && productNumber>0 && !productNumberString.equals("")){
                    product.setName(productName);
                    product.setNumber(productNumber);
                    if(!productInformation.equals("")){
                        product.setInformation(productInformation);
                    }else{
                        product.setInformation("");
                    }
                    reference.child(String.valueOf(maxid+1)).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AlertDialog.Builder success= new AlertDialog.Builder(getActivity());
                            success.setMessage("Termék hozzáadva!");
                            success.setPositiveButton("Oké",null);
                            success.create().show();
                            productNameEt.setText("");
                            productInformationEt.setText("");
                            productNumberEt.setText("");
                        }
                    });
                }else{
                    AlertDialog.Builder notGoodData =new AlertDialog.Builder(getActivity());
                    message="Hiba: ";
                    if(productName.equals("")) message+= "\n\t - Nincs terméknév megadva";
                    if(productNumberString.equals("")) message+= "\n\t - Nincs mennyiség megadva";
                    else if(productNumber<=0) message+= "\n\t - Legalább egy mennyiségnek kell lennie";
                    message+= "!";
                    notGoodData.setMessage(message);
                    notGoodData.setPositiveButton("Oké",null).create().show();
                }

            }
        });

        return view;
    }
}