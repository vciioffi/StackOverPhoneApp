package com.example.stackoverphone;

public class Answers implements Comparable<Answers> {
    private String usuario;
    private String respuesta;
    private String codigo;
    private String fecha;
    private String puntuacion;

    public Answers() {

    }

    public Answers(String usuario, String respuesta, String codigo, String fecha, String puntuacion) {
        this.usuario = usuario;
        this.respuesta = respuesta;
        this.codigo = codigo;
        this.fecha = fecha;
        this.puntuacion = puntuacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public int compareTo(Answers answers) {
        if (Integer.parseInt(answers.getPuntuacion())>Integer.parseInt(puntuacion)){
            System.out.println("añañin "+Integer.parseInt(answers.getPuntuacion()));
            return -1;
        }else if (Integer.parseInt(answers.getPuntuacion())>Integer.parseInt(puntuacion)){
            System.out.println("añañin "+Integer.parseInt(answers.getPuntuacion()));

            return 0;
        }else {

            return 1;
        }
    }
}


