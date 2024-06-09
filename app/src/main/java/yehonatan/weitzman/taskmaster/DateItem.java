package yehonatan.weitzman.taskmaster;

/**
 * The type Date item.
 */
public class DateItem {
    //   מטרת העצם היא לשימוש תאריכי היעד של המשימות
    private int year;
    private int month;
    private int day;

    /**
     * Instantiates a new Date item.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public DateItem(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets month.
     *
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets day.
     *
     * @return the day
     */
    public int getDay() {
        return day;
    }



}




