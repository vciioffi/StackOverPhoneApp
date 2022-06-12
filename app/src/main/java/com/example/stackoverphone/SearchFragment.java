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
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private SearchView searchView;
    View v;

    private RecyclerView recyclerView_Search;
    private personAdapter adapter;
    private AdaptadorRV adaptador2;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search,container,false);
        recyclerView_Search = (RecyclerView) v.findViewById(R.id.recyclerView_Search);

        LinearLayoutManager lnm = new LinearLayoutManager(getActivity());
        lnm.setStackFromEnd(true);
        lnm.setReverseLayout(true);
        recyclerView_Search.setLayoutManager(lnm);
        searchView = (SearchView) v.findViewById(R.id.searchView);
        TextView textView4 = (TextView) v.findViewById(R.id.textView4);
        /*ArrayList<Question> filteredList = new ArrayList<>();
        filteredList.add(new Question("hola","ewasdf","sdf","asdf",4));
        AdaptadorRV_Search adr = new AdaptadorRV_Search(filteredList);
        recyclerView_Search.setAdapter(adr);*/

       FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Question").orderByChild("fecha"),Question.class)
                        .build();

        adapter = new personAdapter(options);

        //recyclerView_Search.setAdapter(adapter);

       // System.out.println("principio; "+recyclerView_Search.getChildCount());
        // recyclerView_Search = (RecyclerView) view.findViewById(R.id.recyclerView_Search);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               /* recyclerView_Search.setLayoutManager(lnm);
                FirebaseRecyclerOptions<Question> options =
                        new FirebaseRecyclerOptions.Builder<Question>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Question").orderByChild("fecha"),Question.class)
                                .build();
                adapter = new personAdapter(options);
                recyclerView_Search.setAdapter(adapter);*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               /* System.out.println(s +" escrito");
                FirebaseRecyclerOptions<Question> options =
                        new FirebaseRecyclerOptions.Builder<Question>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Question").orderByChild("fecha"),Question.class)
                                .build();
                adapter = new personAdapter(options);
                recyclerView_Search.setAdapter(adapter);
                System.out.println(adapter.getItemCount()+"adadp");
                System.out.println(recyclerView_Search.getChildCount());*/
                filter(s);
                textView4.setVisibility(View.INVISIBLE);
                return true;
            }
        });

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
    private void filter(String text){
        ArrayList<Question> filteredList = new ArrayList<>();

        for (int i =0; i<adapter.getItemCount();i++){

            if (adapter.getItem(i).getTitulo().toLowerCase().contains(text))
            filteredList.add(adapter.getItem(i));
            System.out.println(text);

        }

        AdaptadorRV_Search adr = new AdaptadorRV_Search(filteredList);
        recyclerView_Search.setAdapter(adr);
      //  adaptador2 = new AdaptadorRV(filteredList);
    }
}