package yehonatan.weitzman.taskmaster;

import static yehonatan.weitzman.taskmaster.FirebasecController.getAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class FriandsActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv;
    ArrayList<ItemFriands> fraindsList;
    FriandsAdapter friandsAdapter;
    TextView tvMyName, tvMyID,tvShowID;
    User user;
    FirebasecController firebasecController;
    Button btnNewShereTask,btnDate_shereTask,btnSaveNewTask_Dialog_shereTask,btnSerchDialog,btnCloseDialog,btnSearchTask;
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

        tvMyName = findViewById(R.id.tvMyName);
        tvMyID = findViewById(R.id.tvMyID);
        tvMyID.setText("Your ID: " + getAuth().getCurrentUser().getUid());

        btnNewShereTask = findViewById(R.id.btnNewShereTask);
        btnNewShereTask.setOnClickListener(this);
        btnSerchDialog = findViewById(R.id.btnSerchDialog);
        btnSerchDialog.setOnClickListener(this);

        firebasecController = new FirebasecController(this);





        ItemFriands F1 = new ItemFriands("friand 1" ,"cjtu6u65u");
        ItemFriands F2 = new ItemFriands("friand 2","hsr6uutrh");
        ItemFriands F3 = new ItemFriands("friand 3","5755hhf");
        ItemFriands F4 = new ItemFriands("friand 4","hfj7676");
        ItemFriands F5 = new ItemFriands("friand 5","jkmcg 76");
        ItemFriands F6 = new ItemFriands("friand 6","jddfj6555");
        ItemFriands F7 = new ItemFriands("friand 7","jtjtj555675745");
        ItemFriands F8 = new ItemFriands("friand 8","fhnfjk7677");
        ItemFriands F9 = new ItemFriands("friand 9","drsseg5");
        ItemFriands F10 = new ItemFriands("friand 10","drs545seg5");
        ItemFriands F11 = new ItemFriands("friand 11","bf666");
        ItemFriands F12 = new ItemFriands("friand 12","bzddbrt5");
        ItemFriands F13 = new ItemFriands("friand 13","bzdhd");
        ItemFriands F14 = new ItemFriands("friand 14","bbnfxn");

        //phase 2 - add to array list
        fraindsList = new ArrayList<ItemFriands>();
        fraindsList.add(F1);fraindsList.add(F2);fraindsList.add(F3);
        fraindsList.add(F4);fraindsList.add(F5);fraindsList.add(F6);
        fraindsList.add(F7);fraindsList.add(F8);fraindsList.add(F9);
        fraindsList.add(F10);fraindsList.add(F11);fraindsList.add(F12);
        fraindsList.add(F13);fraindsList.add(F14);

        //phase 3 - create adapter
        friandsAdapter=new FriandsAdapter(this,0,0,fraindsList);
        //phase 4 reference to listview
        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(friandsAdapter);


//         spiner:
        ArrayCategory = new ArrayList<String>();
        Intent intent=getIntent();
        String shereCategory = intent.getExtras().getString("shereCategory");
        String[] parts = shereCategory.split("/");
        for (String part : parts) {
            ArrayCategory.add(part);
        }



    }

    @Override
    public void onClick(View v) {
        if (v == btnNewShereTask){
            CreatNewTaskDialod();
        } else if (v == btnDate_shereTask) {
            Calendar systemCalender = Calendar.getInstance();
            int year = systemCalender.get(Calendar.YEAR);
            int month = systemCalender.get(Calendar.MONTH);
            int day = systemCalender.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,new FriandsActivity.SetDate(),year,month,day);
            datePickerDialog.show();
        } else if (v== btnSaveNewTask_Dialog_shereTask) {
            ItemTask itemTask = new ItemTask(etNewTask_Dialog_shereTask.getText().toString(),SpinnerControler.getSelected(),Dday,Dmonth,Dyear);
            String idTask = firebasecController.creatShereTask(itemTask);
            Toast.makeText(this,"The task has been added! Don't forget to copy and share the task:)",Toast.LENGTH_LONG).show();
            tvShowID.setText(getAuth().getCurrentUser().getUid() + "/" + idTask);
        } else if (v == btnCloseDialog) {
            d.dismiss();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,tvShowID.getText().toString());
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            startActivity(intent);
        }
        else if(v==btnSerchDialog){
            CreatSearchTaskDialod();
        } else if (v == btnSearchTask) {
          String id = etSearchTask.getText().toString();
            String[] parts = id.split("/");
            String idUser = parts[0];
            String idTask = parts[1];
            firebasecController.saveShereTask(idUser,idTask);
            d.dismiss();
        }

    }


    private void CreatNewTaskDialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_shere_task);
        d.setTitle("Shere TASK");
        d.setCancelable(true);
        etNewTask_Dialog_shereTask = (EditText) d.findViewById(R.id.etNewTask_Dialog_shereTask);
        btnDate_shereTask = (Button) d.findViewById(R.id.btnDate_shereTask);
        btnDate_shereTask.setOnClickListener(this);
        btnSaveNewTask_Dialog_shereTask = (Button) d.findViewById(R.id.btnSaveNewTask_Dialog_shereTask);
        btnSaveNewTask_Dialog_shereTask.setOnClickListener(this);
        tvShowID = d.findViewById(R.id.tvShowID);
        btnCloseDialog = d.findViewById(R.id.btnCloseDialog);
        btnCloseDialog.setOnClickListener(this);



               //  ~~~~~ spinner ~~~~~
        spinner_shereTask = (Spinner) d.findViewById(R.id.spinner_shereTask);
        new SpinnerControler(this, spinner_shereTask, ArrayCategory);
        d.show();

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


}


