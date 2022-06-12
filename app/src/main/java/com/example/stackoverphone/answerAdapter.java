package com.example.stackoverphone;

import android.content.Intent;
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
public class answerAdapter extends FirebaseRecyclerAdapter<
        Question, answerAdapter.personsViewholder> {

    public answerAdapter(
            @NonNull FirebaseRecyclerOptions<Question> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
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
                .inflate(R.layout.itemlist, parent, false);
        return new answerAdapter.personsViewholder(view);
    }






    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    static class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewPregunta, textViewPuntos,textViewUser;
        ImageView imgItem;

        public personsViewholder(@NonNull View itemView) {
            super(itemView);

            textViewTitulo = (TextView) itemView.findViewById(R.id.textViewTitulo);
            textViewPregunta = (TextView) itemView.findViewById(R.id.textViewPregunta);
            textViewPuntos = (TextView) itemView.findViewById(R.id.textViewPuntos);
            textViewUser = (TextView) itemView.findViewById(R.id.textViewUser);
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
}
