package yehonatan.weitzman.taskmaster;

import android.graphics.Bitmap;

public class ItemFriands {
    private String name;
    private String id;
    private Bitmap bitmap;

    public ItemFriands(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public ItemFriands(String name, String id, Bitmap bitmap) {
        this.name = name;
        this.id = id;
        this.bitmap = bitmap;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
