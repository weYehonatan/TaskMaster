package yehonatan.weitzman.taskmaster;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseCallback {
        TextView tvName;
        List<ItemTask> productList;
        ArrayList<String> ArrayCategory;
        RecyclerView recyclerView;
        Dialog d;
        ImageButton btnNewTaskToDialog,btnSettingToDialog;
        EditText etNewTask,etAddCategory ;
        Button btnSaveTask,btnDate,btnSaveCategory;
        FirebasecController firebasecController;
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
                firebasecController = new FirebasecController(this);
                firebasecController.readTask(this);
                firebasecController.readUser(this);
                user = new User();

                //initializing the productlist
                productList = new ArrayList<>();
                ItemTask t1 = new ItemTask("task_1", "home", 11, 11, 2011);
                //phase 2 - add to array list
                productList = new ArrayList<ItemTask>();
                productList.add(t1);


                // ~~~~ Dialog ~~~~
                btnNewTaskToDialog = (ImageButton) findViewById(R.id.btnPlus);
                btnNewTaskToDialog.setOnClickListener(this);

//                ArrayCategory = new ArrayList<String>();
//                ArrayCategory.add("Home");
//                ArrayCategory.add("Work");
//                ArrayCategory.add("Other");

                //        מטרה: שמירת הקטגוריות בSharedPreferences ככה שאם המסתמש יוסיף בהגדרות זה ישמר ולא יתאפס.
                // ~~~ Sharedpreference  ~~~
                ArrayCategory = new ArrayList<String>();
                sp=getSharedPreferences("details1",0);
                SharedPreferences.Editor editor=sp.edit();
                String oldCategory = sp.getString("category",null);

                editor.putString("category","Home" + "/" + "Work" + "/" + "Other" +"/");
                editor.commit();

                String[] parts = oldCategory.split("/");
                for (String part : parts) {
                        ArrayCategory.add(part);
                }



        }
        public void addCategory(String newCategory){
                String str1 = sp.getString("category",null);
                String str2 = "/" +newCategory;
                String str3 = str1 + str2;
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("category", str3);
                ArrayCategory.add(newCategory);
                //         כרגע מוסיף קטגוריה זמנית, אבל לא שומר לפעם הבאה


        }



        @Override
        public void onClick(View v) {
                if( v == btnNewTaskToDialog)
                {
                        CreatNewTaskDialod();
                }
                else if ( v == btnSaveTask)
                {
                        //saveTask to Firebase :
                        itemTask = new ItemTask(etNewTask.getText().toString(),SpinnerControler.getSelected(),Dday,Dmonth,Dyear);
                        firebasecController.saveTask(itemTask);
                        Toast.makeText(this,"The task has been added",Toast.LENGTH_LONG).show();
                        d.dismiss();
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
                etNewTask = (EditText) d.findViewById(R.id.etNewTask_Dialog);
                btnSaveTask = (Button) d.findViewById(R.id.btnSaveNewTask_Dialog);
                btnSaveTask.setOnClickListener(this);
                btnDate =d.findViewById(R.id.btnDate);
                btnDate.setOnClickListener(this);

//                //  ~~~~~ spinner ~~~~~
                spinner = (Spinner) d.findViewById(R.id.spinner);
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
        public void callbackUser(User user) {

                tvName.setText(user.getName());
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
                        startActivity(intent);
                        Toast.makeText(this,"you selected friends",Toast.LENGTH_LONG).show();
                }
                else if(id == R.id.action_setting)
                {
                        Toast.makeText(this,"you selected setting",Toast.LENGTH_LONG).show();
                        CreatSettingDialod();
                } else if (id == R.id.action_SignOut) {
//                        firebasecController.LogOut();
                        Intent intent=new Intent(this,OpeningActivity.class);
                        this.startActivity(intent);
                }
                if(id == R.id.action_SignOut){
                        firebasecController.LogOut();
                }
                return true;
        }





        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }



}





