package yehonatan.weitzman.taskmaster;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    FirebaseController firebaseController;
    static Context mCtx;
    private List<ItemCategory> productList;

    public CategoryAdapter(Context context, List<ItemCategory> productList)
    {
        this.mCtx = context;
        this.productList = productList;
        firebaseController = new FirebaseController();
    }

    @Override

    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_category, null);
        return new CategoryViewHolder(view);

    }
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        //getting the product of the specified position
        ItemCategory product = productList.get(position);
        //binding the data with the viewholder views
        holder.tvTitle.setText(product.getTitle());
        holder.tvCategoryNumber.setText(product.getTaskCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gdgd
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,
        tvCategoryNumber;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCategoryTitle);
            tvCategoryNumber = itemView.findViewById(R.id.tvCategoryNumber);
        }

    }
}

