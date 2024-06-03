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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
        List<ItemCategory> categoryListItem;
        ArrayList<String> ArrayCategory;
        RecyclerView recyclerView;
        ListView lvCategory;
        Dialog d;
        ImageButton btnNewTaskToDialog,btnSettingToDialog;
        EditText etNewTask,etAddCategory,etDescription,etRename ;
        Button btnSaveTask,btnDate, btnSaveSetting;
        FirebaseController firebaseController;
        User user;
        ItemTask itemTask;
        Spinner spinner,spinnerRemove;
        SharedPreferences sp;
        int Dday,Dmonth,Dyear;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
//                setContentView(R.layout.activity_test);


                //             ~~~~ firebasecController ~~~~
                firebaseController = new FirebaseController(this);
//                firebaseController.readTask(this);
                firebaseController.readTasksByCategory("all",this);
                firebaseController.readUser(this);
                // firebasecController.readShereTask(this);
                user = new User();

                initializationView();
                createRecyclerView();
                initializationCategory();
                createNotification();
//                createrecyclerViewCategory();

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
                        itemTask = new ItemTask(etNewTask.getText().toString(),etDescription.getText().toString(),
                                SpinnerControler.getSelected(),Dday,Dmonth,Dyear);
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
                if (v==btnSettingToDialog) {
                        CreatSettingDialod();
                }
                if (v==btnDate) {
                        Calendar systemCalender = Calendar.getInstance();
                        int year = systemCalender.get(Calendar.YEAR);
                        int month = systemCalender.get(Calendar.MONTH);
                        int day = systemCalender.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(this,new SetDate1(),year,month,day);
                        datePickerDialog.show();
                }
                if (v== btnSaveSetting){
                        if (!etRename.getText().toString().trim().isEmpty()) {
                                firebaseController.changeUserName(etRename.getText().toString());
                        }
                        if (!etAddCategory.getText().toString().trim().isEmpty()){
                                addCategory(etAddCategory.getText().toString());
                                Toast.makeText(this,"Category saved",Toast.LENGTH_LONG).show();
                        }
                        if (!SpinnerControler.getSelected().isEmpty()){
                                removeCategory(SpinnerControler.getSelected());
                                Toast.makeText(this,"Category removed!",Toast.LENGTH_SHORT).show();
                        }
                        d.dismiss();

                }

        }


        private void initializationView() {
                tvName = findViewById(R.id.tvUserName);
                // ~~~~ Dialog ~~~~
                btnNewTaskToDialog = (ImageButton) findViewById(R.id.btnPlus);
                btnNewTaskToDialog.setOnClickListener(this);
        }
        private void initializationCategory() {
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
        }
        private void createrecyclerViewCategory(){
                //getting the recyclerview from xml
                lvCategory = findViewById(R.id.recyclerViewCategoty);
                //setting layout manager to horizontal
               //*** LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                //initializing the category list and adapter
                categoryListItem = new ArrayList<ItemCategory>();
                for(int i =0; i<ArrayCategory.size(); i++){
                        //
                        categoryListItem.add(new ItemCategory(ArrayCategory.get(i), 0));


                }
//                categoryListItem.add(new ItemCategory("Home", 3));
//                categoryListItem.add(new ItemCategory("Other", 3));
                CategoryAdapter adapter = new CategoryAdapter(this,0,0, categoryListItem);
                lvCategory.setAdapter(adapter);
        }

        private void createRecyclerView() {
                //getting the recyclerview from xml
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                //initializing the productlist
                productList = new ArrayList<>();
                ItemTask t1 = new ItemTask("task_1",null, "home", 11, 11, 2011);
                //phase 2 - add to array list
                productList = new ArrayList<ItemTask>();
                productList.add(t1);
        }

        private void createNotification() {
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
        public void removeCategory(String oldCategory){
                String str = "";
                for(int i=0;i<ArrayCategory.size();i++){
                        if(ArrayCategory.get(i).equals(oldCategory)){
                                ArrayCategory.remove(i);
                        }
                        else {
                                str = str+ ArrayCategory.get(i) + "/";
                        }
                }
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("category",str);
                editor.commit();
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
                etRename = d.findViewById(R.id.etRename);
                btnSaveSetting = d.findViewById(R.id.btnSaveNewCategory);
                btnSaveSetting.setOnClickListener(this);
                spinnerRemove = (Spinner) d.findViewById(R.id.spinnerRemoveCategory);
                new SpinnerControler(this, spinnerRemove, ArrayCategory);
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
                for (int i =0;i<ArrayCategory.size();i++){
                        for (int j =0;j<productList.size();j++){
                                if(ArrayCategory.get(i).equals(productList.get(i).getCategory())){
                                        categoryListItem.get(i).setTaskCount(categoryListItem.get(i).getTaskCount()+1);
                                }
                        }
                }

                // category
                // productList
//                productList.get(j).getCategory() = ArrayCategory(i);
//                categoryList.get(i).setcunt++;
        }


        public class SetDate1 implements DatePickerDialog.OnDateSetListener
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
                        Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
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





