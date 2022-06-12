package com.example.stackoverphone;

public class Usuarios {
    private String nick, mail;
    private int puntuacion,preguntas,respuestas;

    public Usuarios(String mail,String nick, int puntuacion,int preguntas,int respuestas) {
        this.nick = nick;
        this.mail = mail;
        this.puntuacion = puntuacion;
        this.preguntas = preguntas;
        this.respuestas = respuestas;
    }
    public Usuarios() {
    }

    public int getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(int preguntas) {
        this.preguntas = preguntas;
    }

    public int getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(int respuestas) {
        this.respuestas = respuestas;
    }



    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}
