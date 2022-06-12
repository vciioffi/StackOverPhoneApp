package com.example.stackoverphone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabHomeFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabHomeFragment3 extends Fragment {

    private static FirebaseUser user;
    private static String userID;
    private static StorageReference storageReference;
    private static String userNickname;
    private static String nombre;

    private Bitmap bitmap;
    private FirebaseRecyclerOptions<Question> options;
    private personAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View v;
    private RecyclerView recyclerView;
    private ArrayList<DatosRV> lst;
    //https://www.youtube.com/watch?v=T_QfRU-A3s4

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabHomeFragment3() {

        // Required empty public constructor
    }
    public TabHomeFragment3(String nicko) {

        // Required empty public constructor
        userNickname=nicko;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabHomeFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static TabHomeFragment3 newInstance(String param1, String param2) {
        TabHomeFragment3 fragment = new TabHomeFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getNickname();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question");
         options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(query.orderByChild("usuario").equalTo(userNickname),Question.class)
                        .build();

        System.out.println("usuario respuestas !!! "+userNickname);
        /*lst = new ArrayList<>();

        lst.add(new DatosRV("hola","hola","10"));
        lst.add(new DatosRV("hola","hola","1"));
        lst.add(new DatosRV("hola","hola","1"));*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_tab_home3,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_respuestas);

        LinearLayoutManager lnm = new LinearLayoutManager(getActivity());
        lnm.setStackFromEnd(true);
        lnm.setReverseLayout(true);
        recyclerView.setLayoutManager(lnm);
        //  System.out.println("cuanmtas"+lista.size());

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /*Query query = reference.child("Question");
        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(query.orderByChild("usuario").equalTo(userNickname),Question.class)
                        .build();*/


        adapter = new personAdapter(options);

        recyclerView.setAdapter(adapter);


        return v;

    }

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

    public static String getNickname2(){
        String nombre2;
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
                        nombre = userP.getNick();

                    }

                }
                //System.out.println("vacio");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        nombre2 = nombre;
        return nombre2;
    }

    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    /*private void getImage() {
        StorageReference storageImages = FirebaseStorage.getInstance().getReference("images/"+userNickname);
        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageImages.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("no se ha podido tener la foto");
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}