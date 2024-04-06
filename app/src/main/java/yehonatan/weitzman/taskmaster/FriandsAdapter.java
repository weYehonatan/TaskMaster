package yehonatan.weitzman.taskmaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FriandsAdapter extends ArrayAdapter<ItemTask> {

    FirebasecController firebasecController;
    Context context;
    List<ItemTask> objects;
    public FriandsAdapter(Context context, int resource, int textViewResourceId, List<ItemTask> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
        firebasecController = new FirebasecController();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.recycleritem_friands,parent,false);
        ItemTask temp = objects.get(position);

        TextView TitelTask = view.findViewById(R.id.tvTitle_shereTask);
        TitelTask.setText(temp.getName());

        TextView Category = view.findViewById(R.id.tvCategory_shereTask);
        Category.setText(temp.getCategory());

        TextView Late = view.findViewById(R.id.tvLate_shereTask);

        CheckBox Finish = view.findViewById(R.id.checkBox_IsFinish);
        Finish.setChecked(false);

        Finish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // קבלת הפריט הנוכחי
                    ItemTask itemTask = objects.get(position);

                    // מחיקת הפריט מ-Firebase

                    firebasecController.deleteTask(itemTask.getIdTask());

                    // מחיקת הפריט מהרשימה המקומית
                    objects.remove(position);

                    // עדכון RecyclerView
                    notifyDataSetChanged();

                    // הודעת אישור (אופציונלי)
                    Toast.makeText(context, "משימה נמחקה!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(temp.isLate()){
            Late.setBackgroundColor(Color.parseColor("#FF0000"));
            Late.setText(" Late" );
        }
        else{
            Late.setBackgroundColor(Color.parseColor("#008000"));
            Late.setText( "" );
        }


        return view;
    }
}

