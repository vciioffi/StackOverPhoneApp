package com.example.stackoverphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DisplayedQuestion extends AppCompatActivity {

    private TextView tv_titulo,QuestionTextView,CodeTextView,textViewNick;
    private ArrayList<Answers> listaDatos;

    private RecyclerView rv;
    private personAdapterAnswers adapter;
    private String keyQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayed_question);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.azul));
        }

        Bundle bundle = this.getIntent().getExtras();

        tv_titulo= (TextView) findViewById(R.id.tv_titulo);
        QuestionTextView= (TextView) findViewById(R.id.QuestionTextView);
        CodeTextView= (TextView) findViewById(R.id.CodeTextView);
        textViewNick= (TextView) findViewById(R.id.textViewNick);

        tv_titulo.setMovementMethod(new ScrollingMovementMethod());
        QuestionTextView.setMovementMethod(new ScrollingMovementMethod());
        CodeTextView.setMovementMethod(new ScrollingMovementMethod());
        textViewNick= (TextView) findViewById(R.id.textViewNick);

        tv_titulo.setText(bundle.getString("titulo"));
        QuestionTextView.setText(bundle.getString("pregunta"));
        CodeTextView.setText(bundle.getString("codigo"));
        textViewNick.setText(bundle.getString("usuario"));
        AdaptadorRVAnswers adra = new AdaptadorRVAnswers(rellenarRecyclerView());

        listaDatos = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_respuestas);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button_Answer);

        LinearLayoutManager lnm = new LinearLayoutManager(DisplayedQuestion.this);
        lnm.setStackFromEnd(true);
        lnm.setReverseLayout(true);
        rv.setLayoutManager(lnm);

        rv.setAdapter(adra);
        System.out.println("items" +adra.getItemCount());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayedQuestion.this, NuevaRespuesta.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        /*listaDatos.add(new DatosRV("hola","hola","10"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));

        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));
        listaDatos.add(new DatosRV("hola","hola","1"));*/

        //AdaptadorRV adapter = new AdaptadorRV(listaDatos);
        //rv.setAdapter(adapter);


    }

    private void getQuestionKey() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question");
        query.orderByChild("titulo").equalTo(tv_titulo.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println("key de la pregunta : "+snapshot.getKey());
                        keyQuestion = snapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private ArrayList<Answers> rellenarRecyclerView(){

        ArrayList<Answers> listaDatos2 = new ArrayList<>();
        getQuestionKey();
        System.out.println(keyQuestion+" en respuesta");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question");
        query.orderByChild("titulo").equalTo(tv_titulo.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println("key de la pregunta en respuesta : "+snapshot.getKey());

                        DataSnapshot dataSnapshot1 = snapshot.child("Answers");
                        System.out.println("keyr respuesta " +snapshot.child("Answers").getKey());
                        for (DataSnapshot snapshot1 :dataSnapshot1.getChildren()){
                            Answers nuevarespuesta  =snapshot1.getValue(Answers.class);
                            listaDatos2.add(nuevarespuesta);
                        }
                        Collections.sort(listaDatos2);


                      /*  DataSnapshot dataSnapshot1 = snapshot.child("Answers");
                        System.out.println("keyr respuesta " +snapshot.child("Answers").getKey());
                        for (DataSnapshot snapshot1 :dataSnapshot1.getChildren()){
                            Answers nuevarespuesta  =snapshot1.getValue(Answers.class);
                            listaDatos2.add(nuevarespuesta);
                        }*/
                    }
                }
                //AdaptadorRVAnswers adra = new AdaptadorRVAnswers(listaDatos2);
                //rv.setAdapter(adra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return listaDatos2;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        //this.recreate();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            this.finish();
            //this.recreate();
        }
        return super.onKeyDown(keyCode, event);
    }

}