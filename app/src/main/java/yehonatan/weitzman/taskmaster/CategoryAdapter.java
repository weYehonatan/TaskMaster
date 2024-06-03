package yehonatan.weitzman.taskmaster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<ItemCategory> {

    FirebaseController firebaseController;
    Context context;
    List<ItemCategory> objects;

    public CategoryAdapter(Context context, int resource, int textViewResourceId, List<ItemCategory> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
        firebaseController = new FirebaseController();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_category,parent,false);
        ItemCategory temp = objects.get(position);

        TextView TitelCategory = view.findViewById(R.id.tvCategoryTitle);
        TitelCategory.setText(temp.getTitle());

        TextView CategoryNum = view.findViewById(R.id.tvCategoryNumber);
        CategoryNum.setText(temp.getTaskCount());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseController.readTasksByCategory(temp.getTitle(), (FirebaseCallback) context);
            }
        });


        return view;
    }
}

