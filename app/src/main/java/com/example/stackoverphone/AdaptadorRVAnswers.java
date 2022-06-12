package com.example.stackoverphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//https://www.youtube.com/watch?v=X-hYIQcmXUw

public class AdaptadorRVAnswers
        extends RecyclerView.Adapter<AdaptadorRVAnswers.ViewHolderPreguntas>
        implements View.OnClickListener{

    ArrayList<Answers> listaPersonajes;
    Context mContext;

    public AdaptadorRVAnswers(ArrayList<Answers> listaPersonajes, Context mContext) {
        this.listaPersonajes = listaPersonajes;
        this.mContext = mContext;
    }

    private View.OnClickListener listener;

    public AdaptadorRVAnswers(ArrayList<Answers> listaPersonajes) {
        this.listaPersonajes = listaPersonajes;
    }

    @Override
    public ViewHolderPreguntas onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout=0;

            layout=R.layout.item_answer;


        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        view.setOnClickListener(this);

        return new ViewHolderPreguntas(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPreguntas holder, int position) {
        holder.textViewUser.setText(listaPersonajes.get(position).getUsuario());
        holder.textViewRespuesta.setText(listaPersonajes.get(position).getRespuesta());
        holder.textViewPuntos.setText(listaPersonajes.get(position).getPuntuacion());

//        holder.foto = listaPersonajes.get(position).getImagen();

        Bundle bundle = new Bundle();
        bundle.putString("usuario",listaPersonajes.get(position).getUsuario());
        bundle.putString("puntuacion",String.valueOf(listaPersonajes.get(position).getPuntuacion()));
        bundle.putString("codigo",listaPersonajes.get(position).getCodigo());
        bundle.putString("respuesta",listaPersonajes.get(position).getRespuesta());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("onclick");
                Intent intent = new Intent(view.getContext(), DisplayedAnswer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // bundle.putParcelable("imagen",model.getImageUri());


                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPersonajes.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderPreguntas extends RecyclerView.ViewHolder {

        TextView textViewUser,textViewRespuesta,textViewPuntos;


        public ViewHolderPreguntas(View itemView) {
            super(itemView);
            textViewUser= (TextView) itemView.findViewById(R.id.textViewUser);
            textViewRespuesta= (TextView) itemView.findViewById(R.id.textViewRespuesta);
            textViewPuntos= (TextView) itemView.findViewById(R.id.textViewPuntos);

        }
    }
}
