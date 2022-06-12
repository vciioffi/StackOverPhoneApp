package com.example.stackoverphone;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class personsViewholder
        extends RecyclerView.ViewHolder {
    TextView textViewTitulo, textViewPregunta, textViewPuntos;

    public personsViewholder(@NonNull View itemView) {
        super(itemView);

        textViewTitulo = (TextView) itemView.findViewById(R.id.textViewTitulo);
        textViewPregunta = (TextView) itemView.findViewById(R.id.textViewPregunta);
        textViewPuntos = (TextView) itemView.findViewById(R.id.textViewPuntos);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
    }
    //https://stackoverflow.com/questions/38263336/firebase-ui-recyclerview-onclick
    private personAdapter.personsViewholder.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener {
        public void onItemClick(View view, int position);

        public void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(personAdapter.personsViewholder.ClickListener clickListener) {
        mClickListener = clickListener;

    }
}