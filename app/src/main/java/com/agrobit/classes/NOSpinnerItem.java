package com.agrobit.classes;

public class NOSpinnerItem {
    private String itemText;
    private int image;

    public NOSpinnerItem(String itemText, int image) {
        this.itemText = itemText;
        this.image = image;
    }

    public String getItemText() {
        return itemText;
    }

    public int getImage() {
        return image;
    }
}
