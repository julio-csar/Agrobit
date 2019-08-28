package com.agrobit.classes;

public class ModelHome {

    private int image;
    private String name;
    private String desc;
    private String hora;
    private int status;

    public ModelHome(int image, String name, String desc, String hora, int status) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.hora = hora;
        this.status = status;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
