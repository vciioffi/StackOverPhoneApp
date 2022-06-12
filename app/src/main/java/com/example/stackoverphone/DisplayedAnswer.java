package com.example.stackoverphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayedAnswer extends AppCompatActivity {

    private TextView questionEditText,textViewPuntos, codeEditText,tv_Usuario,tv_Puntuacion,textViewUser,tv_Flecha,textViewPregunta,textViewTitulo;
    private String keyAnswer;
    private String keyQuestion;
    private CircleImageView imgItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayed_answer);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.azul));
        }

        Bundle bundle = this.getIntent().getExtras();

        //pregunta
        textViewPuntos = (TextView)findViewById(R.id.textViewPuntos);
        textViewPregunta =(TextView) findViewById(R.id.textViewPregunta);
        textViewTitulo =(TextView) findViewById(R.id.textViewTitulo);
        textViewUser =(TextView) findViewById(R.id.textViewUser);
        imgItem = (CircleImageView) findViewById(R.id.imgItem);


        //respuesta
        questionEditText= (TextView) findViewById(R.id.questionEditText);
        codeEditText= (TextView) findViewById(R.id.codeEditText);
        tv_Usuario= (TextView) findViewById(R.id.tv_Usuario);
        tv_Puntuacion= (TextView) findViewById(R.id.tv_Puntuacion);
        tv_Flecha= (TextView) findViewById(R.id.tv_Flecha);


        tv_Puntuacion.setText(bundle.getString("puntuacion"));
        questionEditText.setText(bundle.getString("respuesta"));
        codeEditText.setText(bundle.getString("codigo"));
        tv_Usuario.setText(bundle.getString("usuario"));

        getKeyAnswer();

        tv_Flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValuetoAnswer();
            }
        });
    }

    private void getKeyAnswer() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question");
        query.orderByChild("Answers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println("key de la pregunta : "+snapshot.getKey());
                        DataSnapshot dataSnapshot1 = snapshot.child("Answers");
                        //keyQuestion = snapshot.getKey();
                        for (DataSnapshot snapshot1 :dataSnapshot1.getChildren()){
                            Answers nuevarespuesta  =snapshot1.getValue(Answers.class);
                            System.out.println("key respuse pregunta"+snapshot1.getKey());
                            System.out.println("respuesta: "+ snapshot1.child("respuesta").getValue());
                            if (snapshot1.child("respuesta").getValue().toString().equals(questionEditText.getText().toString())){
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                                System.out.println("es estaaaa "+snapshot1.child("respuesta").getValue());
                                System.out.println("es estaaaa "+snapshot.child("titulo").getValue());

                                keyAnswer = snapshot1.getKey().toString();
                                keyQuestion = snapshot.getKey().toString();

                                loadDataQuestion();
                                // Query query1 = reference1.up("Question");
                               //query1.equalTo(snapshot.getKey()).;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadDataQuestion(){

        System.out.println("ahoraaa "+keyAnswer);
        System.out.println("ahoraaa "+keyQuestion);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Question").child(keyQuestion);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("existeeeee "+dataSnapshot.getKey());

                    textViewPuntos.setText(String.valueOf(dataSnapshot.child("puntuacion").getValue().toString()));
                    textViewTitulo.setText(dataSnapshot.child("titulo").getValue().toString());
                    textViewPregunta.setText(dataSnapshot.child("pregunta").getValue().toString());
                    textViewUser.setText(dataSnapshot.child("usuario").getValue().toString());

                    StorageReference storageImages = FirebaseStorage.getInstance().getReference("images/"+textViewUser.getText().toString());
                    try {
                        File localfile = File.createTempFile("tempfile",".jpg");
                        storageImages.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        imgItem.setImageBitmap(bitmap);
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

                   /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        textViewPuntos.setText("sadf");
                        textViewTitulo.setText("sadf");
                        textViewPregunta.setText("sadf");
                        textViewUser.setText("sadf");
                       /* textViewPuntos.setText(String.valueOf(snapshot.child("puntuacion").getValue().toString()));
                        textViewTitulo.setText(snapshot.child("titulo").getValue().toString());
                        textViewPregunta.setText(snapshot.child("pregunta").getValue().toString());
                        textViewUser.setText(snapshot.child("usuario").getValue().toString());*

                        //keyQuestion = snapshot.getKey();
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addValuetoAnswer(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Question").child(keyQuestion).child("Answers").child(keyAnswer);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    System.out.println("siuu "+dataSnapshot.getKey());

                    Answers questionp = dataSnapshot.getValue(Answers.class);
                    questionp.setPuntuacion(String.valueOf(Integer.valueOf(questionp.getPuntuacion())+1));

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                    reference2.child("Question").child(keyQuestion).child("Answers").child(keyAnswer).child("puntuacion").setValue(questionp.getPuntuacion());

                    tv_Puntuacion.setText(questionp.getPuntuacion());
                    Toast.makeText(DisplayedAnswer.this,"Se ha añadido +1 a la respuesta", Toast.LENGTH_LONG).show();
                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference();
                    Query query2 = reference3.child("Usuarios");
                    query2.orderByChild("nick").equalTo(textViewUser.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    String keyquestion = snapshot.getKey();
                                    Usuarios questionp = snapshot.getValue(Usuarios.class);
                                    questionp.setPuntuacion(questionp.getPuntuacion()+1);

                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Usuarios");
                                    reference2.child(keyquestion).child("puntuacion").setValue(questionp.getPuntuacion());

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}