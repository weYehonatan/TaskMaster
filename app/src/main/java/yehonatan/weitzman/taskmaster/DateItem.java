package yehonatan.weitzman.taskmaster;

public class DateItem {
    //   מטרת העצם היא לשימוש התאריכי יעד של המשימות

    private int year;
    private int month;
    private int day;

    public DateItem(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}




