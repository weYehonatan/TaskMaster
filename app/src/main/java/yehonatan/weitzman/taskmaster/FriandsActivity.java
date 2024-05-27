package yehonatan.weitzman.taskmaster;

import static yehonatan.weitzman.taskmaster.FirebaseController.getAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class FriandsActivity extends AppCompatActivity implements View.OnClickListener, FirebaseCallback {
    ListView lv;
    ArrayList<ItemTask> fraindsList;
    ArrayList<String> idUserList;
    FriandsAdapter friandsAdapter;
    TextView tvMyName, tvMyID,tvShowID;
    FirebaseController firebaseController;
    Button btnNewShereTask,btnDate_shereTask,btnSerchDialog,btnCloseDialog,btnSearchTask;
    Dialog d;
    EditText etNewTask_Dialog_shereTask,etSearchTask;
    Spinner spinner_shereTask;
    SharedPreferences sp;
    ArrayList<String> ArrayCategory;
    int Dday,Dmonth,Dyear;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friands);
        firebaseController = new FirebaseController(this);

        initializationView();
        readShereTask();
    }


    @Override
    public void onClick(View v) {
        if (v == btnDate_shereTask) {
            Calendar systemCalender = Calendar.getInstance();
            int year = systemCalender.get(Calendar.YEAR);
            int month = systemCalender.get(Calendar.MONTH);
            int day = systemCalender.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,new FriandsActivity.SetDate(),year,month,day);
            datePickerDialog.show();
        }
        if (v == btnCloseDialog) {
            d.dismiss();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,tvShowID.getText().toString());
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            startActivity(intent);
        }
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
            friandsAdapter.notifyDataSetChanged(); // ריענון אדפטר

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
        friandsAdapter=new FriandsAdapter(this,0,0,fraindsList);
        //phase 4 reference to listview
        lv=(ListView)findViewById(R.id.lv);
    }

    private void initializationView() {
        tvMyName = findViewById(R.id.tvMyName);
        tvMyID = findViewById(R.id.tvMyID);
        tvMyID.setText("Your ID: " + getAuth().getCurrentUser().getUid());

        btnNewShereTask = findViewById(R.id.btnNewShereTask);
        btnNewShereTask.setOnClickListener(this);
        btnSerchDialog = findViewById(R.id.btnSerchDialog);
        btnSerchDialog.setOnClickListener(this);
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



    public  class SetDate implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear +1;
            Toast.makeText(FriandsActivity.this,"You selected :" + dayOfMonth + "/" + monthOfYear +"/" + year,Toast.LENGTH_LONG).show();
            Dyear = year;
            Dmonth = monthOfYear;
            Dday =dayOfMonth;
        }
    }



    @Override
    public void callbackUser(String user) {}
    @Override
    public void callbackTask(ArrayList<ItemTask> taskList) {
        if(taskList != null) {

            friandsAdapter = new FriandsAdapter(this, 0, 0, taskList);
            lv.setAdapter(friandsAdapter);
        }
    }

    @Override
    public void callbackCategory(ArrayList<CategoryItemRecyclerView> categoryList) {

    }


}


