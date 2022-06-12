package com.example.stackoverphone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VPAdapter extends FragmentStateAdapter {
    private static FirebaseUser user;
    private static String userID;
    private static StorageReference storageReference;
    private static String userNickname;
    private static String nombre;

    public VPAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        getNickname();
        switch (position) {
            case 0:
                return  new TabHomeFragment1();
            case 1:
                return  new TabHomeFragment2();
            default:
                return  new TabHomeFragment3(userNickname);
        }
    }
    @Override
    public int getItemCount() {return 3; }

    public static void getNickname(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getEmail();
        System.out.println("el correo es"+userID);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Usuarios");
        query.orderByChild("mail").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuarios userP = snapshot.getValue(Usuarios.class);
                        System.out.println("nicnkame:: "+ userP.getNick());
                        userNickname = userP.getNick();

                    }

                }
                //System.out.println("vacio");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
