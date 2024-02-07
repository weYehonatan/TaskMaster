package yehonatan.weitzman.taskmaster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FriandsAdapter extends ArrayAdapter<ItemFriands> {

    Context context;
    List<ItemFriands> objects;
    public FriandsAdapter(Context context, int resource, int textViewResourceId, List<ItemFriands> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.recycleritem_friands,parent,false);

        TextView FriandsName = (TextView)view.findViewById(R.id.tvFriandsName);
        TextView FriandsId = (TextView)view.findViewById(R.id.tvFriandsId);
//        ImageView ivProduct=(ImageView)view.findViewById(R.id.ivProduct);
        ItemFriands temp = objects.get(position);


      //  ivProduct.setImageBitmap(temp.getBitmap());
        FriandsName.setText(String.valueOf(temp.getName()));
        FriandsId.setText(temp.getId());


        return view;
    }
}

