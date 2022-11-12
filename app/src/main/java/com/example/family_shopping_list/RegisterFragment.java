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

public class RegisterFragment extends Fragment {

    private EditText registerNameEt,registerPasswordEt,registerPasswordAgainEt;
    private Button registerBt;

    private String name,password,passwordAgain,message;
    private Family registingFamily;

    private FirebaseDatabase db;
    private DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        registerNameEt=view.findViewById(R.id.registerNameEt);
        registerPasswordEt=view.findViewById(R.id.registerPasswordEt);
        registerPasswordAgainEt=view.findViewById(R.id.registerPasswordAgainEt);
        registerBt=view.findViewById(R.id.registerBt);
        registingFamily=new Family();

        db=FirebaseDatabase.getInstance();
        reference=db.getReference().child("families");

        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=registerNameEt.getText().toString();
                password=registerPasswordEt.getText().toString();
                passwordAgain=registerPasswordAgainEt.getText().toString();

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean namein=false;
                        for(DataSnapshot data: snapshot.getChildren()){
                            if(data.getKey().equals(name)){
                                namein=true;
                            }
                        }

                        if(!name.equals("") && !password.equals("") &&
                                !name.contains(" ")&& name.length()>2 &&!password.contains(" ") && password.length()>6 &&
                                password.equals(passwordAgain)&& !namein){
                            registingFamily.setName(name);
                            registingFamily.setPassword(password);
                            reference.child(name).setValue(registingFamily).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            AlertDialog.Builder success= new AlertDialog.Builder(getActivity());
                                            success.setMessage("Sikeres regisztráció!");
                                            success.setPositiveButton("Oké",null);
                                            success.create().show();
                                            registerNameEt.setText("");
                                            registerPasswordEt.setText("");
                                            registerPasswordAgainEt.setText("");
                                        }
                                    });

                        }else{
                            AlertDialog.Builder notGood= new AlertDialog.Builder(getActivity());
                            message="Hiba: ";
                            if (name.equals("")) message += "\n - Nincs felhasználónév megadva";
                            else if(name.contains(" ")) message+="\n - A névben nem lehet szóköz";
                            else if(name.length()<3) message+="\n - A névnek minimum 3 karakter hosszúnak kell lennie";
                            else if(namein) message+= "\n - Ez a felhasználónév már foglalt";
                            if (password.equals("")) message += "\n - Nincs jelszó megadva";
                            else if(password.contains(" ")) message += "\n - A jelszóban nem lehet szóköz";
                            else if(password.length()<7) message+="\n - A jelszónak minimum 7 karakter hosszúnak kell lennie";
                            else if (!password.equals(passwordAgain)) message += "\n -A jelszavak nem egyeznek";
                            message += "!";
                            notGood.setMessage(message);
                            notGood.setPositiveButton("Oké",null);
                            notGood.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Database Error"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }
}