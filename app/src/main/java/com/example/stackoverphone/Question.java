package com.example.stackoverphone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Question {

    private String usuario;
    private String preguntaID;
    private String titulo;
    private String pregunta;



    private String codigo;
    private String fecha;
    private int puntuacion;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap= null;



    public Question() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Question(String usuario, String titulo, String pregunta, String codigo, String fecha, int puntuacion) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.pregunta = pregunta;
        this.codigo = codigo;
        this.fecha = fecha;
        this.puntuacion = puntuacion;

    }





    public Question(String usuario, String titulo, String pregunta, String fecha, int puntuacion) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.pregunta = pregunta;
        this.fecha = fecha;
        this.puntuacion = puntuacion;
        setDefaultUri();
    }
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getPreguntaID() {
        return preguntaID;
    }

    public void setPreguntaID(String preguntaID) {
        this.preguntaID = preguntaID;
    }



    public Bitmap getImageUri() {
        if ( usuario==null){

           /* StorageReference storageImages = FirebaseStorage.getInstance().getReference("images/defaultprofile.png");
            try {
                File localfile = File.createTempFile("tempfile",".jpg");
                storageImages.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                System.out.println(bitmap);
                                // imgProfile.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("no se ha podido tener la foto default");

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }else {
            StorageReference storageImages = FirebaseStorage.getInstance().getReference("images/" + usuario);
            try {
                File localfile = File.createTempFile("tempfile", ".jpg");
                storageImages.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                                // imgProfile.setImageBitmap(bitmap);
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
        return bitmap;
    }
    public void setDefaultUri() {
        StorageReference storageImages = FirebaseStorage.getInstance().getReference("images/defaultprofile");
        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            storageImages.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            // imgProfile.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("no se ha podido tener la foto default");

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
