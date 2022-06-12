package com.example.stackoverphone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class personAdapter extends FirebaseRecyclerAdapter<
        Question, personAdapter.personsViewholder> {

    public personAdapter(
            @NonNull FirebaseRecyclerOptions<Question> options)
    {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder,
                     int position, @NonNull Question model)
    {

        holder.textViewTitulo.setText(model.getTitulo());
        holder.textViewPregunta.setText(model.getPregunta());
        holder.textViewPuntos.setText(String.valueOf(model.getPuntuacion()));
        holder.textViewUser.setText(model.getUsuario());
        //Bitmap bitmap = model.getImageUri();
        if (model.getUsuario()==null){

            //default image profile
          //holder.imgItem.setImageDrawable(R.drawable.defaultprofile);
        }else {
            holder.imgItem.setImageBitmap(model.getImageUri());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("onclick");
                Intent intent = new Intent(view.getContext(), DisplayedQuestion.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("usuario",model.getUsuario());
                bundle.putString("titulo", model.getTitulo());
                bundle.putString("pregunta",model.getPregunta());
                bundle.putString("puntuacion",String.valueOf(model.getPuntuacion()));
                bundle.putString("codigo",String.valueOf(model.getCodigo()));
               // bundle.putParcelable("imagen",model.getImageUri());


                intent.putExtras(bundle);

                view.getContext().startActivity(intent);

            }
        });

        holder.textViewFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("Question");
                query.orderByChild("titulo").equalTo(holder.textViewTitulo.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                System.out.println("key de la usuareio : "+snapshot.getKey());

                                String keyquestion = snapshot.getKey();
                                Question questionp = snapshot.getValue(Question.class);
                                questionp.setPuntuacion(questionp.getPuntuacion()+1);

                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Question");
                                reference2.child(keyquestion).child("puntuacion").setValue(questionp.getPuntuacion());

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

               // holder.textViewPuntos.setText(questionp.getPuntuacion());
                Toast.makeText(view.getContext(),"Se ha añadido +1 puntos a la pregunta",Toast.LENGTH_LONG).show();

                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                Query query2 = reference2.child("Usuarios");
                query2.orderByChild("nick").equalTo(holder.textViewUser.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                System.out.println("key de la usuareio : "+snapshot.getKey());

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

        });
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public personsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist, parent, false);
        return new personAdapter.personsViewholder(view);
    }





    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    static class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewPregunta, textViewPuntos,textViewUser,textViewFlecha;
        ImageView imgItem;

        public personsViewholder(@NonNull View itemView) {
            super(itemView);

            textViewTitulo = (TextView) itemView.findViewById(R.id.textViewTitulo);
            textViewPregunta = (TextView) itemView.findViewById(R.id.textViewPregunta);
            textViewPuntos = (TextView) itemView.findViewById(R.id.textViewPuntos);
            textViewUser = (TextView) itemView.findViewById(R.id.textViewUser);
            textViewFlecha = (TextView) itemView.findViewById(R.id.textViewFlecha);

            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAbsoluteAdapterPosition());
                    System.out.println("onclickk");

                }
            });
        }
        //https://stackoverflow.com/questions/38263336/firebase-ui-recyclerview-onclick
        private personsViewholder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }
        public void setOnClickListener(personsViewholder.ClickListener clickListener) {
            mClickListener = clickListener;


        }
    }
    public void getcosas(){

        
    }
}
