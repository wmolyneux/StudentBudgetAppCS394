package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;


import java.io.Serializable;

/**
 * This class contains the functionality for the Category object
 *
 * @author wim2
 * @version 1.0
 */
public class Category implements Serializable{

    private int id;
    private int position;
    private String color;

    public Category(){}

    public Category(int pos, String color){
        super();
        this.position = pos;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Account [id="+id+", position="+position+", color="+color+"]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
