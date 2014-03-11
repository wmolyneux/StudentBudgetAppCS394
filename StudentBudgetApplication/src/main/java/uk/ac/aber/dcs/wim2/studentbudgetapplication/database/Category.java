package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;


import java.io.Serializable;

/**
 * Created by wim2 on 05/02/2014.
 */
public class Category implements Serializable{

    private int id;
    private String name;
    private String color;

    public Category(){}

    public Category(String category, String color){
        super();
        this.name = category;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Account [id="+id+", name="+name+", color="+color+"]";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
