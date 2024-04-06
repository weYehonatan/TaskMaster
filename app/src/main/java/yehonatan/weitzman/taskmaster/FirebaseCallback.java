package yehonatan.weitzman.taskmaster;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public interface FirebaseCallback {

    void callbackUser(String user);
    void callbackTask(ArrayList<ItemTask> taskList);
    void callbackShereTask(ArrayList<ItemTask> taskList);

    void onItemSelected(AdapterView<?> parent, View view, int position, long id);

    void onNothingSelected(AdapterView<?> parent);
}
