package yehonatan.weitzman.taskmaster;

public class ItemCategory {
    private String title;
    private int taskCount;

    public ItemCategory(String title, int taskCount) {
        this.title = title;
        this.taskCount = taskCount;
    }

    public String getTitle() {
        return title;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}


