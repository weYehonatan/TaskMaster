package yehonatan.weitzman.taskmaster;

import static yehonatan.weitzman.taskmaster.FirebasecController.getAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriandsActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv;
    ArrayList<ItemFriands> fraindsList;
    FriandsAdapter friandsAdapter;
    TextView tvMyName, tvMyID;
    User user;
    FirebasecController firebasecController;
    Button btnNewShereTask;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friands);

        tvMyName = findViewById(R.id.tvMyName);
        tvMyID = findViewById(R.id.tvMyID);
        tvMyID.setText("Your ID: " + getAuth().getCurrentUser().getUid());

        btnNewShereTask = findViewById(R.id.btnNewShereTask);
        btnNewShereTask.setOnClickListener(this);





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

    }

    @Override
    public void onClick(View v) {
        if (v == btnNewShereTask){

        }
    }
}