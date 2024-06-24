package yehonatan.weitzman.taskmaster;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseCallback {
    private TextView tvName;
    List<ItemTask> productList;
    ArrayList<String> ArrayCategory;
    RecyclerView recyclerView, recyclerViewCategory;
    Taskadapter taskadapter;
    Dialog d;
    ImageButton btnNewTaskToDialog;
    EditText etNewTask, etAddCategory, etDescription, etRename;
    Button btnSaveTask, btnDate, btnSaveSetting, btnRestartCategory, btnSearchByCategory,btnSpeechToText;
    FirebaseController firebaseController;
    User user;
    ItemTask itemTask;
    Spinner spinner, spinnerRemove, spinnerReadCategory;
    SharedPreferences sp;
    int Dday, Dmonth, Dyear;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000; //SpeechToText



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //             ~~~~ firebasecController ~~~~
        firebaseController = new FirebaseController(this);
        firebaseController.readTasksByCategory("all", this);
        firebaseController.readUser(this);
        firebaseController.readCategory(this);
        user = new User();

        createNotification();
        initializationView();
        createRecyclerView();
        initializationCategory();
//                createrecyclerViewCategory();

    }

    @Override
    public void onClick(View v) {
        if (v == btnNewTaskToDialog) {
            CreatNewTaskDialod();
        }
        if (v == btnSaveTask) {
            //saveTask to Firebase :
            itemTask = new ItemTask(etNewTask.getText().toString(), etDescription.getText().toString(), SpinnerControler.getSelected(), Dday, Dmonth, Dyear);
            firebaseController.saveTask(itemTask);
            Toast.makeText(this, "The task has been added", Toast.LENGTH_LONG).show();
            d.dismiss();

            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("idTask", itemTask.getIdTask());
            intent.putExtra("MyReceiver", "New Task");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 4000, pendingIntent);

        }
        if (v == btnDate) {
            Calendar systemCalender = Calendar.getInstance();
            int year = systemCalender.get(Calendar.YEAR);
            int month = systemCalender.get(Calendar.MONTH);
            int day = systemCalender.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new SetDate1(), year, month, day);
            datePickerDialog.show();
        }
        if (v == btnSaveSetting) {
            if (!etRename.getText().toString().trim().isEmpty()) {
                firebaseController.changeUserName(etRename.getText().toString());
            }
            if (!etAddCategory.getText().toString().trim().isEmpty()) {
                //addCategory(etAddCategory.getText().toString());
                String s = etAddCategory.getText().toString();
                firebaseController.saveCategory(s);
                // Toast.makeText(this,"Category saved",Toast.LENGTH_LONG).show();
                Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            }
            if (!SpinnerControler.getSelected().isEmpty()) {
                //removeCategory(SpinnerControler.getSelected());
                firebaseController.deleteCategory(SpinnerControler.getSelected());
                Toast.makeText(this, "Category removed!", Toast.LENGTH_SHORT).show();
            }
            d.dismiss();

        }
        if (v == btnRestartCategory) {
            d.dismiss();
            ArrayCategory.clear();

            String str = "Home" + "/" + "Work" + "/" + "Other" + "/";
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("category", str);
            editor.commit();

            String[] parts = str.split("/");
            for (String part : parts) {
                ArrayCategory.add(part);
            }
            Toast.makeText(this, "Restart Category", Toast.LENGTH_SHORT).show();


        }
        if (v == btnSearchByCategory && SpinnerControler.getSelected() != null) {
            firebaseController.readTasksByCategory(SpinnerControler.getSelected(), this);
        }
        if(v==btnSpeechToText){
            speak();
        }
    }




    private void initializationView() {
        tvName = findViewById(R.id.tvUserName);
        // ~~~~ Dialog ~~~~
        btnNewTaskToDialog = (ImageButton) findViewById(R.id.btnPlus);
        btnNewTaskToDialog.setOnClickListener(this);
    }

    private void initializationCategory() {
        firebaseController.saveCategory("Home");
        firebaseController.saveCategory("School");
        firebaseController.saveCategory("Other");


    }



    private void createRecyclerView() {
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initializing the productlist
        productList = new ArrayList<>();


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
        intent.putExtra("MyReceiver", "Good Morning");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // הגדרת AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void addCategory(String newCategory) {
        String str1 = sp.getString("category", null);
        String str2 = newCategory + "/";
        String str3 = str1 + str2;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("category", str3);
        ArrayCategory.add(newCategory);
        editor.commit();
    }

    public void removeCategory(String oldCategory) {
//                String str = "";
//                for(int i=0;i<ArrayCategory.size();i++){
//                        if(ArrayCategory.get(i).equals(oldCategory)){
//                                ArrayCategory.remove(i);
//                        }
//                        else {
//                                str = str+ ArrayCategory.get(i) + "/";
//                        }
//                }
//                SharedPreferences.Editor editor=sp.edit();
//                editor.putString("category",str);
//                editor.commit();
    }


    // Dialod:
    public void CreatNewTaskDialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_add_task);
        d.setTitle("New TASK");
        d.setCancelable(true);
        etNewTask = (EditText) d.findViewById(R.id.etNewTask);
        etDescription = (EditText) d.findViewById(R.id.etDescription);
        btnSaveTask = (Button) d.findViewById(R.id.btnSavNewTask);
        btnSaveTask.setOnClickListener(this);
        btnDate = d.findViewById(R.id.btnNewDate);
        btnDate.setOnClickListener(this);

//                //  ~~~~~ spinner ~~~~~
        spinner = (Spinner) d.findViewById(R.id.spinnerCaegory);
        new SpinnerControler(this, spinner, ArrayCategory);
        // ~~~~
        d.show();

        //SpeechToText
        btnSpeechToText = d.findViewById(R.id.btnSpeechToText);
        btnSpeechToText.setOnClickListener(this);
    }

    private void speak() {
        // יצירת אינטנט לפעולה של זיהוי דיבור
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            // הפעלת האינטנט לקבלת תוצאה
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                // קבלת התוצאות מהאינטנט
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // הצגת התוצאה בטקסט
                etNewTask.setText(result.get(0));
            }
        }
    }

    public void CreatSettingDialod() {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_setting);
        d.setTitle("setting");
        d.setCancelable(true);
        etAddCategory = d.findViewById(R.id.etAddCategory);
        etRename = d.findViewById(R.id.etRename);
        btnSaveSetting = d.findViewById(R.id.btnSaveNewCategory);
        btnSaveSetting.setOnClickListener(this);
        btnRestartCategory = d.findViewById(R.id.btnRestartCategory);
        btnRestartCategory.setOnClickListener(this);
        spinnerRemove = (Spinner) d.findViewById(R.id.spinnerRemoveCategory);
        new SpinnerControler(this, spinnerRemove, ArrayCategory);
        d.show();
    }


    // read from firebase:
    @Override
    public void callbackUser(String user) {
        if (user != null)
            tvName.setText(user);
    }

    @Override
    public void callbackTask(ArrayList<ItemTask> taskList) {
        productList = taskList;
        //creating recyclerview adapter
        taskadapter = new Taskadapter(this, productList);
        //setting adapter to recyclerview
        recyclerView.setAdapter(taskadapter);

    }

    @Override
    public void callbackUserID(ArrayList<String> idUserList) {

    }

    @Override
    public void callbackCategory(ArrayList<String> categoryList) {
        ArrayCategory = new ArrayList<String>();
        ArrayCategory.clear();
        ArrayCategory = categoryList;
        spinnerReadCategory = (Spinner) findViewById(R.id.spinnerReadCategory);
        ArrayList<String> arrayCategoryRead = new ArrayList<>();
        arrayCategoryRead.addAll(ArrayCategory);
        arrayCategoryRead.add(0, "all");
        new SpinnerControler(this, spinnerReadCategory, arrayCategoryRead);
        btnSearchByCategory = findViewById(R.id.btnSearchByCategory);
        btnSearchByCategory.setOnClickListener(this);
    }


    public class SetDate1 implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            Toast.makeText(MainActivity.this, "You selected :" + dayOfMonth + "/" + monthOfYear + "/" + year, Toast.LENGTH_LONG).show();
            Dyear = year;
            Dmonth = monthOfYear;
            Dday = dayOfMonth;
        }
    }


    // create Menu:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shere_task) {
            Intent intent = new Intent(MainActivity.this, ShereTaskActivity.class);
            //String shereCategory = sp.getString("category",null);
            //  intent.putExtra("shereCategory",shereCategory);
            startActivity(intent);
            Toast.makeText(this, "Shere Task Activity", Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_setting) {
            Toast.makeText(this, "you selected setting", Toast.LENGTH_LONG).show();
            CreatSettingDialod();
        } else if (id == R.id.action_SignOut) {
//                        firebasecController.LogOut();
            Intent intent = new Intent(this, SignInActivity.class);
            this.startActivity(intent);
        }
        if (id == R.id.action_SignOut) {
            firebaseController.LogOut();
        }
        return true;
    }


}





