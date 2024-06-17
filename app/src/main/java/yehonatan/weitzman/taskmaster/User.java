package yehonatan.weitzman.taskmaster;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {
    private String name;
    private String id;
    private String email;
    private ArrayList<ItemTask> taskArrayList;
    private ArrayList<ItemTask> myShereTask;
    private ArrayList<String> shereTask;

    public User() {
    }

    public User(String name, String uId, String uEmail) {
        this.name = name;
        this.id = uId;
        this.email = uEmail;
    }

    public User(String name, String uId, String uEmail,ArrayList<ItemTask> myShereTask ) {
        this.name = name;
        this.id = uId;
        this.email = uEmail;
        this.myShereTask = myShereTask;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUEmail() {
        return email;
    }

    public void setUEmail(String UEmail) {
        this.email = UEmail;
    }

    public ArrayList<ItemTask> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<ItemTask> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public ArrayList<ItemTask> getMyShereTask() {
        return myShereTask;
    }

    public void setMyShereTask(ArrayList<ItemTask> myShereTask) {
        this.myShereTask = myShereTask;
    }

    public ArrayList<String> getShereTask() {
        return shereTask;
    }

    public void setShereTask(ArrayList<String> shereTask) {
        this.shereTask = shereTask;
    }

}
