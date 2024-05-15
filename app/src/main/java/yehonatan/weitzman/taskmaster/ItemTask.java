package yehonatan.weitzman.taskmaster;
import java.lang.String;
import java.util.ArrayList;

public class ItemTask {
    private String name;
    private String description;
    private String category;
    private String idTask;
    private int dayDate;
    private int monthDate;
    private int yearDate;
    private String idCreatUser;

     public ItemTask(){

     }
    public ItemTask(String name,String description, String category, int dayDate, int monthDate, int yearDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.dayDate = dayDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
    }
    public ItemTask(String idCreatUser, String idTask){
        this.idCreatUser =idCreatUser;
        this.idTask = idTask;
    }

    public ItemTask(String name, boolean finish, boolean late, String category, int dayDate, int monthDate, int yearDate) {
        this.name = name;
        this.category = category;
        this.dayDate = dayDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdCreatUser() {
        return idCreatUser;
    }

    public void setIdCreatUser(String idCreatUser) {
        this.idCreatUser = idCreatUser;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public int getDayDate() {
        return dayDate;
    }

    public void setDayDate(int dayDate) {
        this.dayDate = dayDate;
    }

    public int getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(int monthDate) {
        this.monthDate = monthDate;
    }

    public int getYearDate() {
        return yearDate;
    }

    public void setYearDate(int yearDate) {
        this.yearDate = yearDate;
    }


}

