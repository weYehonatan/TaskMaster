package yehonatan.weitzman.taskmaster;

import java.util.ArrayList;


public class User {
    private String name;
    private String Id;
    private String Email;
    private ArrayList<ItemTask> taskArrayList;

    public User() {
    }

    public User(String name, String uId, String uEmail) {
        this.name = name;
        this.Id = uId;
        this.Email = uEmail;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getUEmail() {
        return Email;
    }

    public void setUEmail(String UEmail) {
        this.Email = UEmail;
    }
    public ArrayList<ItemTask> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<ItemTask> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }
}
