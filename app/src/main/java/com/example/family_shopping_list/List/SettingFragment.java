package com.example.family_shopping_list.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.family_shopping_list.List.ShoppingMainActivity;
import com.example.family_shopping_list.Main.MainActivity;
import com.example.family_shopping_list.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingFragment extends Fragment {

    private EditText newNameEt,newPasswordEt,newPasswordAgainEt,oldPasswordEt;
    private Button newPasswordBt,newNameBt,clearListBt;
    private String newName,message,newPassword,newPasswordAgain,oldPassword;

    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        newNameEt=view.findViewById(R.id.newNameEt);
        oldPasswordEt=view.findViewById(R.id.oldPasswordEt);
        newPasswordEt=view.findViewById(R.id.newPasswordEt);
        newPasswordAgainEt=view.findViewById(R.id.newPasswordAgainEt);
        newPasswordBt=view.findViewById(R.id.newPasswordBt);
        newNameBt=view.findViewById(R.id.newNameBt);
        clearListBt=view.findViewById(R.id.clearListBt);

        db=FirebaseDatabase.getInstance();
        reference=db.getReference().child("families");

        newNameBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName=newNameEt.getText().toString();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean nameIn=false;
                        for(DataSnapshot data: snapshot.getChildren()){
                            if(data.getKey().equals(newName))nameIn=true;
                        }

                        if(!newName.equals("")&& !newName.contains(" ")&& newName.length()>2 && !nameIn){
                            reference.child(ShoppingMainActivity.familyName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        reference.child(newName).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                             reference.child(ShoppingMainActivity.familyName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     AlertDialog.Builder succesfull=new AlertDialog.Builder(getActivity());
                                                     succesfull.setMessage("Név sikeresen megváltoztatva!")
                                                             .setPositiveButton("Oké", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialogInterface, int i) {
                                                                     Intent intent=new Intent(getContext(), MainActivity.class);
                                                                     startActivity(intent);
                                                                     getActivity().finish();
                                                                 }
                                                             }).setCancelable(false).create().show();
                                                 }
                                             });
                                            }
                                        })  ;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Database Error:"+error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            AlertDialog.Builder badName=new AlertDialog.Builder(getActivity());
                            message="Hiba: ";
                            if(newName.equals("")) message+="\n\t Üres nevet nem lehet megadni!";
                            else if(newName.contains(" ")) message+= "\n\t Szóközt nem lehet megadni!";
                            else if(newName.length()<3) message+="\n - A névnek minimum 3 karakter hosszúnak kell lennie";
                            else if (nameIn) message+="\n\t Ez a név már foglalt!";
                            badName.setMessage(message).setPositiveButton("Oké",null).create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Database Error:"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        newPasswordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword=oldPasswordEt.getText().toString();
                newPassword=newPasswordEt.getText().toString();
                newPasswordAgain=newPasswordAgainEt.getText().toString();

                reference.child(ShoppingMainActivity.familyName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!oldPassword.equals("")&& !oldPassword.contains(" ") && snapshot.child("password").getValue().equals(oldPassword)&&
                                !newPassword.equals("") && !newPassword.contains(" ")&& newPassword.length()>6&&
                                newPassword.equals(newPasswordAgain)&&!oldPassword.equals(newPassword)) {
                            reference.child(ShoppingMainActivity.familyName).child("password").setValue(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog.Builder succesfull=new AlertDialog.Builder(getActivity());
                                    succesfull.setMessage("Jelszó sikeresen megváltoztatva!")
                                            .setPositiveButton("Oké", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent=new Intent(getContext(),MainActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            }).setCancelable(false).create().show();
                                }
                            });
                        }else{
                            AlertDialog.Builder badPassword=new AlertDialog.Builder(getActivity());
                            message="Hiba: ";
                            if(oldPassword.equals("")) message+="\n\t Nem írta be a régi jelszót";
                            else if(oldPassword.contains(" ")) message+="\n\t Szóköz nem lehet a régi jelszóban";
                            else if(!snapshot.child("password").getValue().equals(oldPassword)) message+="\n\t A régi jelszó nem egyezik";
                            if(newPassword.equals("")) message+="\n\t Az új jelszó nem lehet üres";
                            else if(newPassword.contains(" ")) message+="\n\t Szóköz nem lehet az új jelszóban";
                            else if(newPassword.length()<7) message+="\n - A jelszónak minimum 7 karakter hosszúnak kell lennie";
                            else if(!newPassword.equals(newPasswordAgain)) message+= "\n\t Az jelszavaknak egyenie kell";
                            if (oldPassword.equals(newPassword)) message+= "\n\t Az új jelszó nem lehet ugyan az mint a régi";
                            message+="!";
                            badPassword.setMessage(message).setPositiveButton("Oké",null).create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Database Error"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        clearListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder clear=new AlertDialog.Builder(getActivity());
                clear.setMessage("Biztos ki szeretné üríteni a listát?");
                clear.setNegativeButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.child(ShoppingMainActivity.familyName).child("products").removeValue().
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                AlertDialog.Builder succesfull=new AlertDialog.Builder(getActivity());
                                succesfull.setMessage("Lista sikeresen Kiürítve!")
                                        .setPositiveButton("Oké",null).create().show();
                            }
                        });
                    }
                }).setPositiveButton("Nem",null).create().show();
            }
        });

        return view;
    }
}