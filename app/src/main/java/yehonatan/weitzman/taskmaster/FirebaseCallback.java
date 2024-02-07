package yehonatan.weitzman.taskmaster;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public interface FirebaseCallback {
    //  ~~~~~ spinner ~~~~~
    void onItemSelected(AdapterView<?> parent, View view, int position, long id);

    void onNothingSelected(AdapterView<?> parent);

    void callbackUser(User user);
    void callbackTask(ArrayList<ItemTask> taskList);
}
