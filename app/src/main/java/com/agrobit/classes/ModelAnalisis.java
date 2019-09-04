package com.agrobit.classes;

public class ModelAnalisis {

    private int image;
    private String name;
    private String area;
    private String tec;
    private String fecha;
    private String hora;

    public ModelAnalisis(int image, String name, String area, String tec, String fecha, String hora) {
        this.image = image;
        this.name = name;
        this.area = area;
        this.tec = tec;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTec() {
        return tec;
    }

    public void setTec(String tec) {
        this.tec = tec;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
