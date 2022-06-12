package com.example.stackoverphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//https://www.youtube.com/watch?v=X-hYIQcmXUw

public class AdaptadorRV_Search
        extends RecyclerView.Adapter<AdaptadorRV_Search.ViewHolderPreguntas>
        implements View.OnClickListener{

    ArrayList<Question> listaPersonajes;
    Context mContext;

    public AdaptadorRV_Search(ArrayList<Question> listaPersonajes, Context mContext) {
        this.listaPersonajes = listaPersonajes;
        this.mContext = mContext;
    }

    private View.OnClickListener listener;

    public AdaptadorRV_Search(ArrayList<Question> listaPersonajes) {
        this.listaPersonajes = listaPersonajes;
    }

    @Override
    public ViewHolderPreguntas onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout=0;

            layout= R.layout.itemlist;


        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        view.setOnClickListener(this);

        return new ViewHolderPreguntas(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPreguntas holder, int position) {
        holder.textViewTitulo.setText(listaPersonajes.get(position).getTitulo());
        holder.textViewPregunta.setText(listaPersonajes.get(position).getPregunta());
        holder.textViewPuntos.setText(String.valueOf(listaPersonajes.get(position).getPuntuacion()));
        holder.textViewUser.setText(listaPersonajes.get(position).getUsuario());

        Bundle bundle = new Bundle();
        bundle.putString("usuario",listaPersonajes.get(position).getUsuario());
        bundle.putString("titulo", listaPersonajes.get(position).getTitulo());
        bundle.putString("pregunta",listaPersonajes.get(position).getPregunta());
        bundle.putString("puntuacion",String.valueOf(listaPersonajes.get(position).getPuntuacion()));
        bundle.putString("codigo",String.valueOf(listaPersonajes.get(position).getCodigo()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("onclick");
                Intent intent = new Intent(view.getContext(), DisplayedQuestion.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // bundle.putParcelable("imagen",model.getImageUri());


                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }
        });
//        holder.foto = listaPersonajes.get(position).getImagen();

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

        TextView textViewTitulo,textViewPregunta,textViewPuntos,textViewUser;


        public ViewHolderPreguntas(View itemView) {
            super(itemView);
            textViewTitulo = (TextView) itemView.findViewById(R.id.textViewTitulo);
            textViewPregunta = (TextView) itemView.findViewById(R.id.textViewPregunta);
            textViewPuntos = (TextView) itemView.findViewById(R.id.textViewPuntos);
            textViewUser = (TextView) itemView.findViewById(R.id.textViewUser);
            //imgItem = (ImageView) itemView.findViewById(R.id.imgItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              //      mClickListener.onItemClick(v, getAbsoluteAdapterPosition());
                    System.out.println("onclickk");

                }
            });

        }
    }
    public void filterList(ArrayList<Question> filteredList) {
        listaPersonajes = filteredList;
        notifyDataSetChanged();
    }
}
