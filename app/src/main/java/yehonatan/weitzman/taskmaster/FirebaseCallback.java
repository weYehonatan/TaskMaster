package yehonatan.weitzman.taskmaster;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * The interface Firebase callback.
 */
public interface FirebaseCallback {

    /**
     * Callback user.
     *
     * @param user the user
     */
    void callbackUser(String user);

    /**
     * Callback task.
     *
     * @param taskList the task list
     */
    void callbackTask(ArrayList<ItemTask> taskList);

}
