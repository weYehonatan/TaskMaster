package yehonatan.weitzman.taskmaster;
import java.lang.String;
import java.util.ArrayList;

/**
 * The type Item task.
 */
public class ItemTask {
    private String name;
    private String description;
    private String category;
    private String idTask;
    private int dayDate;
    private int monthDate;
    private int yearDate;
    private String idCreatUser;

    /**
     * Instantiates a new Item task.
     */
    public ItemTask(){

     }

    /**
     * Instantiates a new Item task.
     *
     * @param name        the name
     * @param description the description
     * @param category    the category
     * @param dayDate     the day date
     * @param monthDate   the month date
     * @param yearDate    the year date
     */
    public ItemTask(String name,String description, String category, int dayDate, int monthDate, int yearDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.dayDate = dayDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
    }

    /**
     * Instantiates a new Item task.
     *
     * @param idCreatUser the id creat user
     * @param idTask      the id task
     */
    public ItemTask(String idCreatUser, String idTask){
        this.idCreatUser =idCreatUser;
        this.idTask = idTask;
    }

    /**
     * Instantiates a new Item task.
     *
     * @param name      the name
     * @param category  the category
     * @param dayDate   the day date
     * @param monthDate the month date
     * @param yearDate  the year date
     */
    public ItemTask(String name, String category, int dayDate, int monthDate, int yearDate) {
        this.name = name;
        this.category = category;
        this.dayDate = dayDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
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
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets id creat user.
     *
     * @return the id creat user
     */
    public String getIdCreatUser() {
        return idCreatUser;
    }

    /**
     * Sets id creat user.
     *
     * @param idCreatUser the id creat user
     */
    public void setIdCreatUser(String idCreatUser) {
        this.idCreatUser = idCreatUser;
    }


    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets id task.
     *
     * @return the id task
     */
    public String getIdTask() {
        return idTask;
    }

    /**
     * Sets id task.
     *
     * @param idTask the id task
     */
    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    /**
     * Gets day date.
     *
     * @return the day date
     */
    public int getDayDate() {
        return dayDate;
    }

    /**
     * Sets day date.
     *
     * @param dayDate the day date
     */
    public void setDayDate(int dayDate) {
        this.dayDate = dayDate;
    }

    /**
     * Gets month date.
     *
     * @return the month date
     */
    public int getMonthDate() {
        return monthDate;
    }

    /**
     * Sets month date.
     *
     * @param monthDate the month date
     */
    public void setMonthDate(int monthDate) {
        this.monthDate = monthDate;
    }

    /**
     * Gets year date.
     *
     * @return the year date
     */
    public int getYearDate() {
        return yearDate;
    }

    /**
     * Sets year date.
     *
     * @param yearDate the year date
     */
    public void setYearDate(int yearDate) {
        this.yearDate = yearDate;
    }


}

