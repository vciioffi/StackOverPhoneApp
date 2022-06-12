package com.example.stackoverphone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FirebaseUser user;
    private ImageView imgProfile;
    private TextView tvNickname,tv_Nivel,tv_Preguntas,tv_Respuestas;
    private DatabaseReference dbreference;
    private String userID;
    Uri imageUri;
    StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNickname = (TextView) getView().findViewById(R.id.tvNickname);
        imgProfile = (ImageView) getView().findViewById(R.id.imgProfile);
        tv_Nivel = (TextView) getView().findViewById(R.id.tv_Nivel);
        tv_Respuestas = (TextView) getView().findViewById(R.id.tv_Respuestas);
        tv_Preguntas = (TextView) getView().findViewById(R.id.tv_Preguntas);

        // get datos del usuario activo
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
                        tvNickname.setText(userP.getNick());
                        tv_Nivel.setText(String.valueOf(userP.getPuntuacion()));
                        tv_Preguntas.setText(String.valueOf(userP.getPreguntas()));
                        tv_Respuestas.setText(String.valueOf(userP.getRespuestas()));
                        getImage();
                    }

                }
                //System.out.println("vacio");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //System.out.println(tvNickname.getText());

        //ver metodo
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }
    //cargar la imagen de la bbdd que tenga el nombre del nickname
    private void getImage() {
        StorageReference storageImages = FirebaseStorage.getInstance().getReference("images/"+tvNickname.getText().toString());
        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageImages.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            imgProfile.setImageBitmap(bitmap);
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


    //actualiza la imagen seleccionada en la galeria
    private void uploadImage() {
        storageReference = FirebaseStorage.getInstance().getReference("images/"+(tvNickname.getText()));
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(),"Foto actualizada",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(getActivity(),"No se pudo actualizar",Toast.LENGTH_SHORT).show();


            }
        });

    }

    //selecciona una imagen de la galeria del dispositivo
    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imageUri = data.getData();
                        uploadImage();
                        imgProfile.setImageURI(imageUri);

                    }
                }
            });
}