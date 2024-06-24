package yehonatan.weitzman.taskmaster;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SpinnerControler {
        private Spinner spinner;
        private ArrayList<String> items;
        private static String selected;


    public SpinnerControler(Context context, Spinner spinner, ArrayList<String> items) {
            this.spinner = spinner;
            this.items = items;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(context, "You selected " + selected, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    public static String getSelected() {
        return selected;
    }



}



