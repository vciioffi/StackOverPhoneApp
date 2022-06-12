package com.example.stackoverphone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class personAdapterAnswers extends FirebaseRecyclerAdapter<
        Answers, personAdapterAnswers.personsViewholder> {

    public personAdapterAnswers(
            @NonNull FirebaseRecyclerOptions<Answers> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder,
                     int position, @NonNull Answers model)
    {

        holder.textViewRespuesta.setText(model.getRespuesta());
        holder.textViewPuntos.setText(String.valueOf(model.getPuntuacion()));
        holder.textViewUser.setText(model.getUsuario());
        //Bitmap bitmap = model.getImageUri();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ppppp"+model.getRespuesta());
                Intent intent = new Intent(view.getContext(), DisplayedQuestion.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("usuario",model.getUsuario());
                bundle.putString("respuesta",model.getRespuesta());
                bundle.putString("puntuacion",String.valueOf(model.getPuntuacion()));
                bundle.putString("codigo",String.valueOf(model.getCodigo()));
               // bundle.putParcelable("imagen",model.getImageUri());


                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

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
                .inflate(R.layout.item_answer, parent, false);
        return new personsViewholder(view);
    }





    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    static class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView textViewRespuesta, textViewPuntos,textViewUser;

        public personsViewholder(@NonNull View itemView) {
            super(itemView);

            textViewPuntos = (TextView) itemView.findViewById(R.id.textViewPuntos);
            textViewUser = (TextView) itemView.findViewById(R.id.textViewUser);
            textViewRespuesta = (TextView)itemView.findViewById(R.id.textViewRespuesta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAbsoluteAdapterPosition());
                    System.out.println("onclickk");

                }
            });
        }
        //https://stackoverflow.com/questions/38263336/firebase-ui-recyclerview-onclick
        private ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }
        public void setOnClickListener(ClickListener clickListener) {
            mClickListener = clickListener;


        }
    }
    public void getcosas(){

        
    }
}
