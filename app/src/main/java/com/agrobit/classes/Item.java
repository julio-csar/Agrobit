package com.agrobit.classes;

public class Item{
    int tipo;
    Object item;
    public int sectionPosition;
    public int listPosition;

    public Item(int tipo, Object item) {
        this.tipo = tipo;
        this.item = item;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
