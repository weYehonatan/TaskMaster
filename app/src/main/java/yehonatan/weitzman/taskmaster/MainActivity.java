package yehonatan.weitzman.taskmaster;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseCallback {
        private TextView tvName;
        List<ItemTask> productList;
        ArrayList<String> ArrayCategory;
        RecyclerView recyclerView;
        Dialog d;
        ImageButton btnNewTaskToDialog,btnSettingToDialog;
        EditText etNewTask,etAddCategory,etDescription ;
        Button btnSaveTask,btnDate,btnSaveCategory;
        FirebaseController firebaseController;
        User user;
        ItemTask itemTask;
        Spinner spinner;
        SharedPreferences sp;
        int Dday,Dmonth,Dyear;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                tvName = findViewById(R.id.tvAppName);

                //                   ~~~~ recyclerView ~~~~
                //getting the recyclerview from xml
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));


                //             ~~~~ firebasecController ~~~~
                firebaseController = new FirebaseController(this);
                firebaseController.readTask(this);
                firebaseController.readUser(this);
               // firebasecController.readShereTask(this);
                user = new User();

                //initializing the productlist
                productList = new ArrayList<>();
                ItemTask t1 = new ItemTask("task_1",null, "home", 11, 11, 2011);
                //phase 2 - add to array list
                productList = new ArrayList<ItemTask>();
                productList.add(t1);


                // ~~~~ Dialog ~~~~
                btnNewTaskToDialog = (ImageButton) findViewById(R.id.btnPlus);
                btnNewTaskToDialog.setOnClickListener(this);


                // ~~~ Sharedpreference  ~~~

                ArrayCategory = new ArrayList<String>();
                sp=getSharedPreferences("taskMaster",0);
                SharedPreferences.Editor editor=sp.edit();
                String oldCategory = sp.getString("category",null);
                if(oldCategory == null) {
                        editor.putString("category", "Home" + "/" + "Work" + "/" + "Other" + "/");
                }
                editor.commit();

                String[] parts = oldCategory.split("/");
                for (String part : parts) {
                        ArrayCategory.add(part);
                }





//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(this, MyReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//                // חלופה אחרת: אם לא יעבוד ב9:00
//                //PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),1,intent,PendingIntent.FLAG_IMMUTABLE);
//                // הגדר שעון מעורר שיפעל כל יום בשעה 9:00
//                long alarmTime = System.currentTimeMillis() + 1000 * 60 * 60 * (9 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pendingIntent);


                // הגדרת ההתראה לשעה 9:00
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 00);
                calendar.set(Calendar.SECOND, 0);
                // בדיקה אם השעה כבר עברה היום
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1); // הוסף יום אם השעה כבר עברה
                }
                // יצירת Intent ל-BroadcastReceiver
                Intent intent = new Intent(this, MyReceiver.class);
                intent.putExtra("MyReceiver","Good Morning");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // הגדרת AlarmManager
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                }



        }
        public void addCategory(String newCategory){
                String str1 = sp.getString("category",null);
                String str2 = newCategory +"/" ;
                String str3 = str1 + str2;
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("category", str3);
                ArrayCategory.add(newCategory);
                editor.commit();
        }




        @Override
        public void onClick(View v) {
                if( v == btnNewTaskToDialog)
                {
                        CreatNewTaskDialod();
                }
                if ( v == btnSaveTask)
                {
                        //saveTask to Firebase :
                        itemTask = new ItemTask(etNewTask.getText().toString(),etDescription.getText().toString(),SpinnerControler.getSelected(),Dday,Dmonth,Dyear);
                        firebaseController.saveTask(itemTask);
                        Toast.makeText(this,"The task has been added",Toast.LENGTH_LONG).show();
                        d.dismiss();

                        Intent intent = new Intent(this,MyReceiver.class);
                        intent.putExtra("idTask",itemTask.getIdTask());
                        intent.putExtra("MyReceiver","New Task");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),1,intent,PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC,System.currentTimeMillis()+4000,pendingIntent);

                }
                else if (v==btnSettingToDialog) {
                        CreatSettingDialod();
                }
                else if (v==btnDate) {
                        Calendar systemCalender = Calendar.getInstance();
                        int year = systemCalender.get(Calendar.YEAR);
                        int month = systemCalender.get(Calendar.MONTH);
                        int day = systemCalender.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(this,new SetDate(),year,month,day);
                        datePickerDialog.show();
                }
                else if (v==btnSaveCategory){
                        addCategory(etAddCategory.getText().toString());
                        Toast.makeText(this,"Category saved",Toast.LENGTH_LONG).show();
                        d.dismiss();

                }

        }


        // Dialod:
        public void CreatNewTaskDialod(){
                d = new Dialog(this);
                d.setContentView(R.layout.dialog_add_task);
                d.setTitle("New TASK");
                d.setCancelable(true);
                etNewTask = (EditText) d.findViewById(R.id.tvEditTask);
                etDescription = (EditText) d.findViewById(R.id.etDescription);
                btnSaveTask = (Button) d.findViewById(R.id.btnSaveEditTask);
                btnSaveTask.setOnClickListener(this);
                btnDate =d.findViewById(R.id.btnEditDate);
                btnDate.setOnClickListener(this);

//                //  ~~~~~ spinner ~~~~~
                spinner = (Spinner) d.findViewById(R.id.spinnerEdit);
                new SpinnerControler(this, spinner, ArrayCategory);

                // ~~~~
                d.show();
        }


                public void CreatSettingDialod(){
                d = new Dialog(this);
                d.setContentView(R.layout.dialog_setting);
                d.setTitle("setting");
                d.setCancelable(true);
                etAddCategory = d.findViewById(R.id.etAddCategory);
                btnSaveCategory = d.findViewById(R.id.btnSaveNewCategory);
                btnSaveCategory.setOnClickListener(this);

                d.show();
        }



        // read from firebase:
        @Override
        public void callbackUser(String user) {

                tvName.setText(user);
        }

        @Override
        public void callbackTask(ArrayList<ItemTask> taskList) {
                productList = taskList;
                //creating recyclerview adapter
                Taskadapter adapter = new Taskadapter(this, productList);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
        }




        public  class SetDate implements DatePickerDialog.OnDateSetListener
        {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear +1;
                        Toast.makeText(MainActivity.this,"You selected :" + dayOfMonth + "/" + monthOfYear +"/" + year,Toast.LENGTH_LONG).show();
                        Dyear = year;
                        Dmonth = monthOfYear;
                        Dday =dayOfMonth;
                }
        }


        // create Menu:
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.menu_main,menu);
                return  true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                if (id == R.id.action_friends) {
                        Intent intent = new Intent(MainActivity.this, FriandsActivity.class);
                        String shereCategory = sp.getString("category",null);
                        intent.putExtra("shereCategory",shereCategory);
                        startActivity(intent);
                        Toast.makeText(this,"Shere Task Activity",Toast.LENGTH_LONG).show();
                }
                else if(id == R.id.action_setting)
                {
                        Toast.makeText(this,"you selected setting",Toast.LENGTH_LONG).show();
                        CreatSettingDialod();
                } else if (id == R.id.action_SignOut) {
//                        firebasecController.LogOut();
                        Intent intent=new Intent(this,SignInActivity.class);
                        this.startActivity(intent);
                }
                if(id == R.id.action_SignOut){
                        firebaseController.LogOut();
                }
                return true;
        }





}





