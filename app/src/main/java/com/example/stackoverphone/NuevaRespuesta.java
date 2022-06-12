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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NuevaRespuesta extends AppCompatActivity {

    private TextView textViewTitulo, textViewPregunta,textViewUser,textViewFlecha,textViewPuntos;
    private CircleImageView imgItem;

    private EditText questionEditText,codeEditText;
    private Button submitQuestionButton;
    public int preguntas;

    private static FirebaseUser user;
    private static String userID;
    private static StorageReference storageReference;
    private static String userNickname;

    private String keyQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_respuesta);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.azul));
        }

        Bundle bundle = this.getIntent().getExtras();
        getNickname();

        textViewTitulo = (TextView) findViewById(R.id.textViewTitulo);
        textViewPregunta = (TextView) findViewById(R.id.textViewPregunta);
        textViewUser = (TextView) findViewById(R.id.textViewUser);
        textViewFlecha = (TextView) findViewById(R.id.textViewFlecha);
        textViewPuntos = (TextView) findViewById(R.id.textViewPuntos);

        imgItem = (CircleImageView) findViewById(R.id.imgItem);

        questionEditText = (EditText) findViewById(R.id.questionEditText);
        codeEditText = (EditText) findViewById(R.id.codeEditText);

        submitQuestionButton = (Button) findViewById(R.id.submitQuestionButton);

        textViewTitulo.setText(bundle.getString("titulo"));
        textViewPregunta.setText(bundle.getString("pregunta"));
        textViewUser.setText(bundle.getString("usuario"));
        textViewPuntos.setText(bundle.getString("puntuacion"));
        //imgItem.setImageBitmap(bundle.getParcelable("imagen"));

imgDale();

        submitQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (codeEditText.getText().toString().isEmpty() || questionEditText.getText().toString().isEmpty()){

                    Toast.makeText(NuevaRespuesta.this, "Complete título y pregunta", Toast.LENGTH_LONG).show();

                }else{
                    addAnswerToQuestion();

                }
            }
        });

    }

    private void addAnswerToQuestion(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Question");
        query.orderByChild("titulo").equalTo(textViewTitulo.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println("key de la pregunta : "+snapshot.getKey());
                        keyQuestion = snapshot.getKey();
                        Usuarios userP = snapshot.getValue(Usuarios.class);

                        Map<String, Object> data = new HashMap<>();
                        data.put("respuesta", questionEditText.getText().toString());
                        data.put("codigo", codeEditText.getText().toString());
                        data.put("puntuacion", "0");
                        data.put("usuario", userNickname);
                        //!!! el usuario es otro, el que escribe la respuesta, no la prgunta


                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Question");
                        reference2.child(snapshot.getKey()).child("Answers").child(generateKey()).setValue(data);

                        getQuestionValue();
                        Toast.makeText(NuevaRespuesta.this,"Respuesta publicada correctamente",Toast.LENGTH_SHORT).show();

                       /* FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Question").document(textViewTitulo.getText().toString()).collection("Answers")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        System.out.println("respuesta añadida bien");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("respuesta añadida mal");
                                    }
                                });*/

                    }

                }
                //System.out.println("vacio");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NuevaRespuesta.this,"Error al publicar respuesta",Toast.LENGTH_SHORT).show();

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
                        userP.setRespuestas(userP.getRespuestas()+1);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Usuarios");
                        reference2.child(keyuser).child("respuestas").setValue(userP.getRespuestas());

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
    public String generateKey() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public void imgDale(){
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

    }

}