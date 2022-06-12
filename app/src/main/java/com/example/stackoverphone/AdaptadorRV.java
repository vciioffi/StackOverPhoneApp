package com.example.stackoverphone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//https://www.youtube.com/watch?v=X-hYIQcmXUw

public class AdaptadorRV
        extends RecyclerView.Adapter<AdaptadorRV.ViewHolderPreguntas>
        implements View.OnClickListener{

    ArrayList<DatosRV> listaPersonajes;
    Context mContext;

    public AdaptadorRV(ArrayList<DatosRV> listaPersonajes, Context mContext) {
        this.listaPersonajes = listaPersonajes;
        this.mContext = mContext;
    }

    private View.OnClickListener listener;

    public AdaptadorRV(ArrayList<DatosRV> listaPersonajes) {
        this.listaPersonajes = listaPersonajes;
    }

    @Override
    public ViewHolderPreguntas onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout=0;

            layout=R.layout.itemlist;


        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        view.setOnClickListener(this);

        return new ViewHolderPreguntas(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPreguntas holder, int position) {
        holder.textViewTitulo.setText(listaPersonajes.get(position).getTitulo());
        holder.textViewPregunta.setText(listaPersonajes.get(position).getPregunta());
        holder.textViewPuntos.setText(listaPersonajes.get(position).getPuntuacion());

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

        TextView textViewTitulo,textViewPregunta,textViewPuntos;


        public ViewHolderPreguntas(View itemView) {
            super(itemView);
            textViewTitulo= (TextView) itemView.findViewById(R.id.textViewTitulo);
            textViewPregunta= (TextView) itemView.findViewById(R.id.textViewPregunta);
            textViewPuntos= (TextView) itemView.findViewById(R.id.textViewPuntos);

        }
    }
}
