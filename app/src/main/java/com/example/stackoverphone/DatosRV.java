package com.example.stackoverphone;

public class DatosRV {
    private String titulo,pregunta,puntuacion;

    public DatosRV(String titulo, String pregunta, String puntuacion) {
        this.titulo = titulo;
        this.pregunta = pregunta;
        this.puntuacion = puntuacion;
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

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
}
