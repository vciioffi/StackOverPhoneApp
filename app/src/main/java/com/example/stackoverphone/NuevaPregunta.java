package com.example.stackoverphone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;


public class NuevaPregunta extends AppCompatActivity {
    private Button submitQuestionButton;
    private EditText titleEditText, questionEditText, codeEditText;
    public int preguntas;
    private static FirebaseUser user;
    private static String userID;
    private static StorageReference storageReference;
    private static String userNickname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_pregunta);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.azul));
        }

        submitQuestionButton = (Button) findViewById(R.id.submitQuestionButton);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        questionEditText = (EditText) findViewById(R.id.questionEditText);
        codeEditText = (EditText) findViewById(R.id.codeEditText);
        getNickname();


        submitQuestionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {


                if (titleEditText.getText().toString().isEmpty() || questionEditText.getText().toString().isEmpty()) {

                    Toast.makeText(NuevaPregunta.this, "Complete título y pregunta", Toast.LENGTH_LONG).show();

                } else {

                    //añadir pregunta a la bbdd
                    Question qst;

                    if (codeEditText.getText().toString().isEmpty()) {
                        qst = new Question(userNickname,titleEditText.getText().toString(), questionEditText.getText().toString(), LocalDate.now().toString(),
                                0);

                    } else {
                        qst = new Question(userNickname, titleEditText.getText().toString(), questionEditText.getText().toString(), codeEditText.getText().toString(), LocalDate.now().toString(),
                                0);
                    }
                    System.out.println("se crea question");
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbr = db.getReference(Question.class.getSimpleName());
                    dbr.push().setValue(qst).addOnCompleteListener(suc ->
                    {
                        System.out.println("bien");

                        getQuestionValue();
                        Toast.makeText(NuevaPregunta.this, "Pregunta creada correctamente", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(NuevaPregunta.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }).addOnFailureListener(er ->
                    {
                        System.out.println(er.getMessage());
                        System.out.println("nop");
                    });


                }

            }

        });


    }


    private void getQuestionValue() {
        FirebaseUser user;
        String userID;
        Usuarios userP;

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Usuarios");
        query.orderByChild("mail").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println("key de la usuareio : "+snapshot.getKey());

                        String keyuser = snapshot.getKey();
                        Usuarios userP = snapshot.getValue(Usuarios.class);
                        preguntas = userP.getPreguntas();
                        userP.setPreguntas(userP.getPreguntas()+1);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Usuarios");
                        reference2.child(keyuser).child("preguntas").setValue(userP.getPreguntas());

                    }

                }
                //System.out.println("vacio");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

}