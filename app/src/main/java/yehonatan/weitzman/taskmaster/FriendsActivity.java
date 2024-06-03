package yehonatan.weitzman.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity implements View.OnClickListener, FirebaseCallback {
    ListView lv;
    ArrayList<ItemTask> fraindsList;
    ArrayList<String> idUserList;
    FriendsAdapter friendsAdapter;
    FirebaseController firebaseController;
    Button btnSerchDialog,btnSearchTask;
    Dialog d;
    EditText etSearchTask;
    SharedPreferences sp;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        firebaseController = new FirebaseController(this);
        btnSerchDialog = findViewById(R.id.btnSerchDialog);
        btnSerchDialog.setOnClickListener(this);
        readShereTask();
    }


    @Override
    public void onClick(View v) {
        if(v==btnSerchDialog){
            CreatSearchTaskDialod();
        }
        if (v == btnSearchTask) {
            String id = etSearchTask.getText().toString();
            String[] parts = id.split("/");
            String idUser = parts[0];
            String idTask = parts[1];
            firebaseController.saveShereTask(idUser,idTask);
            d.dismiss();
            SharedPreferences.Editor editor=sp.edit();
            String str= sp.getString("taskMaster",null);
            editor.putString("idUserList",str + "/" + idUser);
            editor.commit();
            friendsAdapter.notifyDataSetChanged(); // ריענון אדפטר
        }
    }

    private void readShereTask() {
        idUserList = new ArrayList<String>();
        sp=getSharedPreferences("taskMaster",0);
        String idUser = sp.getString("idUserList",null);
        if(idUser != null) {
            String[] parts0 = idUser.split("/");
            for (String part : parts0) {
                idUserList.add(part);
            }
            firebaseController.readShereTask(this, idUserList);
        }
        ItemTask t1 = new ItemTask("task_1",null, "home", 11, 11, 2011);
        fraindsList = new ArrayList<ItemTask>();
        fraindsList.add(t1);
        // - create adapter
        friendsAdapter =new FriendsAdapter(this,1,1,fraindsList);
        //phase 4 reference to listview
        lv=findViewById(R.id.lv);
    }

    private void CreatSearchTaskDialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_search_task);
        d.setTitle("Shearch TASK");
        d.setCancelable(true);
        etSearchTask = d.findViewById(R.id.etSearchTask);
        btnSearchTask = d.findViewById(R.id.btnSearchTask);
        btnSearchTask.setOnClickListener(this);
        d.show();
    }
    @Override
    public void callbackUser(String user) {}
    @Override
    public void callbackTask(ArrayList<ItemTask> taskList) {
        if(taskList != null) {
            friendsAdapter = new FriendsAdapter(this, 0, 0, taskList);
            lv.setAdapter(friendsAdapter);
        }
    }


}


