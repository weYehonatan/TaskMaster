package yehonatan.weitzman.taskmaster;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public interface FirebaseCallback {

    void callbackUser(String user);

    void callbackTask(ArrayList<ItemTask> taskList);
    void callbackUserID(ArrayList<String> arrayUSerID);
    void callbackCategory(ArrayList<String> categoryList);

}
