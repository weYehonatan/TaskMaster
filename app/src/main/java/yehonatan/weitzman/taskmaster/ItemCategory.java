package yehonatan.weitzman.taskmaster;

/**
 * The type Item category.
 */
public class ItemCategory {
    private String title;
    private int taskCount;

    /**
     * Instantiates a new Item category.
     *
     * @param title     the title
     * @param taskCount the task count
     */
    public ItemCategory(String title, int taskCount) {
        this.title = title;
        this.taskCount = taskCount;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets task count.
     *
     * @return the task count
     */
    public int getTaskCount() {
        return taskCount;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets task count.
     *
     * @param taskCount the task count
     */
    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}


