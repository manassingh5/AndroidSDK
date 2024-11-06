package com.example.splash1;

public class Contact {
    private String name;
    private String number;
    private boolean isSelected;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
