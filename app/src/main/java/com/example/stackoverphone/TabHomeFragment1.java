package com.example.stackoverphone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabHomeFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabHomeFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    private RecyclerView recyclerView;
    private personAdapter adapter;
    private ArrayList<DatosRV> lista;
    DatabaseReference dbArtists;
    //https://www.youtube.com/watch?v=T_QfRU-A3s4

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabHomeFragment1() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabHomeFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static TabHomeFragment1 newInstance(String param1, String param2) {
        TabHomeFragment1 fragment = new TabHomeFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*lista = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question").orderByChild("puntuacion");
        query.addListenerForSingleValueEvent(valueEventListener);*/




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab_home1,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_respuestas);

        LinearLayoutManager lnm = new LinearLayoutManager(getActivity());
        lnm.setStackFromEnd(true);
        lnm.setReverseLayout(true);
        recyclerView.setLayoutManager(lnm);
        //  System.out.println("cuanmtas"+lista.size());

        FirebaseRecyclerOptions<Question> options =
            new FirebaseRecyclerOptions.Builder<Question>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Question").orderByChild("fecha"),Question.class)
                .build();

        adapter = new personAdapter(options);
        recyclerView.setAdapter(adapter);


        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void dump(@NonNull String prefix, @Nullable FileDescriptor fd, @NonNull PrintWriter writer, @Nullable String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*lista = loadDataFirebase();

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_respuestas);
        AdaptadorRV adr = new AdaptadorRV(lista, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adr);*/
    }



    private ArrayList loadDataFirebase(){

        ArrayList<DatosRV> list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question").orderByChild("titulo");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Question qst = snapshot.getValue(Question.class);
                        System.out.println(qst.getTitulo());

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("hola: "+ databaseError);
            }
        });
        return list;
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            lista.clear();
            System.out.println(dataSnapshot.getChildrenCount());
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question qst = snapshot.getValue(Question.class);
                    lista.add(new DatosRV(qst.getTitulo(), qst.getPregunta(), String.valueOf(qst.getPuntuacion())));
                }
                //adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}