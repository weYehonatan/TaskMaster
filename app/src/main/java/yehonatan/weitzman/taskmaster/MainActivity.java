package yehonatan.weitzman.taskmaster;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    RecyclerView recyclerView;
    Taskadapter taskadapter;
    Dialog d;
    ImageButton btnNewTaskToDialog,btnSoundUp,btnSoundOff;
    EditText etNewTask, etAddCategory, etDescription, etRename;
    Button btnSaveTask, btnDate, btnSaveSetting, btnSearchByCategory,btnSpeechToText;
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

        initializationView();
        createRecyclerView();
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
            d.dismiss();
            startNotificationService("your task has been added");

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
                String str = etAddCategory.getText().toString();
                firebaseController.saveCategory(str);
                Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            }
            if (!SpinnerControler.getSelected().isEmpty()) {
                firebaseController.deleteCategory(SpinnerControler.getSelected());
                Toast.makeText(this, "Category removed!", Toast.LENGTH_SHORT).show();
            }
            d.dismiss();
        }
        if (v == btnSearchByCategory && SpinnerControler.getSelected() != null) {
            firebaseController.readTasksByCategory(SpinnerControler.getSelected(), this);
        }
        if(v==btnSpeechToText){
            speak();
        }
        if(v==btnSoundUp){
            startSound();

        }
        if(v==btnSoundOff){
            stopSound();

            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("sound",false); // שמירת ההגדרות לפעם הבאה
            editor.commit();
        }
    }


    private void startSound() {
        Intent serviceIntent = new Intent(this, SoundService.class);
        startService(serviceIntent);
    }
    private void stopSound() {
        Intent serviceIntent = new Intent(this, SoundService.class);
        stopService(serviceIntent);
    }

    private void startNotificationService(String notificationText){
        Intent serviceIntent = new Intent(this, MyNotificationService.class);
        serviceIntent.putExtra(MyNotificationService.EXTRA_NOTIFICATION_TEXT, notificationText);
        startService(serviceIntent);  // הפעלת ה-Service
    }


    private void initializationView() {
        tvName = findViewById(R.id.tvUserName);
        // ~~~~ Dialog ~~~~
        btnNewTaskToDialog = (ImageButton) findViewById(R.id.btnPlus);
        btnNewTaskToDialog.setOnClickListener(this);

        sp=getSharedPreferences("taskMaster01",0);
        boolean b = sp.getBoolean("sound",true);
        if(b==true)
            startSound();
        else
            stopSound();
    }

    private void createRecyclerView() {
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initializing the productlist
        productList = new ArrayList<>();
    }

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
        spinnerRemove = (Spinner) d.findViewById(R.id.spinnerRemoveCategory);
        new SpinnerControler(this, spinnerRemove, ArrayCategory);
        btnSoundUp = d.findViewById(R.id.btnSoundUp);
        btnSoundUp.setOnClickListener(this);
        btnSoundOff = d.findViewById(R.id.btnSoundOff);
        btnSoundOff.setOnClickListener(this);
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
            startActivity(intent);
            Toast.makeText(this, "Shere Task Activity", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.action_setting) {
            Toast.makeText(this, "you selected setting", Toast.LENGTH_LONG).show();
            CreatSettingDialod();
        }
        else if (id == R.id.action_SignOut) {
            Intent intent = new Intent(this, SignInActivity.class);
            this.startActivity(intent);
        }
        if (id == R.id.action_SignOut) {
            firebaseController.LogOut();
        }
        return true;
    }


}





