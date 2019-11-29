package com.iamrajendra.googlekeep.model;

public class Model implements Comparable{
    private int id;
    private  String title;
    private boolean selected;

    public Model() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Model( String title) {

        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Object o) {
        Model model = (Model) o;
        return this.title.compareTo(model.title);
    }
}
