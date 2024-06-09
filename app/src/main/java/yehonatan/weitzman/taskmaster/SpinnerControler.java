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

/**
 * The type Spinner controler.
 */
public class SpinnerControler {
        private Spinner spinner;
        private ArrayList<String> items;
        private static String selected;


    /**
     * Instantiates a new Spinner controler.
     *
     * @param context the context
     * @param spinner the spinner
     * @param items   the items
     */
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
                    Toast.makeText(context, "You selected " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    /**
     * Gets selected.
     *
     * @return the selected
     */
    public static String getSelected() {
        return selected;
    }



}



