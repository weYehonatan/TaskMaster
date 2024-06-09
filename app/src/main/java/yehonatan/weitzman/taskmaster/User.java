package yehonatan.weitzman.taskmaster;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The type User.
 */
public class User implements Serializable {
    private String name;
    private String id;
    private String email;
    private ArrayList<ItemTask> taskArrayList;
    private ArrayList<ItemTask> myShereTask;
    private ArrayList<String> shereTask;

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param name   the name
     * @param uId    the u id
     * @param uEmail the u email
     */
    public User(String name, String uId, String uEmail) {
        this.name = name;
        this.id = uId;
        this.email = uEmail;
    }

    /**
     * Instantiates a new User.
     *
     * @param name        the name
     * @param uId         the u id
     * @param uEmail      the u email
     * @param myShereTask the my shere task
     */
    public User(String name, String uId, String uEmail,ArrayList<ItemTask> myShereTask ) {
        this.name = name;
        this.id = uId;
        this.email = uEmail;
        this.myShereTask = myShereTask;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets u email.
     *
     * @return the u email
     */
    public String getUEmail() {
        return email;
    }

    /**
     * Sets u email.
     *
     * @param UEmail the u email
     */
    public void setUEmail(String UEmail) {
        this.email = UEmail;
    }

    /**
     * Gets task array list.
     *
     * @return the task array list
     */
    public ArrayList<ItemTask> getTaskArrayList() {
        return taskArrayList;
    }

    /**
     * Sets task array list.
     *
     * @param taskArrayList the task array list
     */
    public void setTaskArrayList(ArrayList<ItemTask> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    /**
     * Gets my shere task.
     *
     * @return the my shere task
     */
    public ArrayList<ItemTask> getMyShereTask() {
        return myShereTask;
    }

    /**
     * Sets my shere task.
     *
     * @param myShereTask the my shere task
     */
    public void setMyShereTask(ArrayList<ItemTask> myShereTask) {
        this.myShereTask = myShereTask;
    }

    /**
     * Gets shere task.
     *
     * @return the shere task
     */
    public ArrayList<String> getShereTask() {
        return shereTask;
    }

    /**
     * Sets shere task.
     *
     * @param shereTask the shere task
     */
    public void setShereTask(ArrayList<String> shereTask) {
        this.shereTask = shereTask;
    }

}
