package yehonatan.weitzman.taskmaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private Context context;
    private List<CategoryItemRecyclerView> categoryList;

    public CategoryListAdapter(Context context, List<CategoryItemRecyclerView> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryItemRecyclerView categoryItem = categoryList.get(position);
        holder.tvCategoryTitle.setText(categoryItem.getTitle());
        holder.tvCategoryNumber.setText(categoryItem.getTaskCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseController firebaseController = new FirebaseController();
                firebaseController.readTasksByCategory("work", (FirebaseCallback) context);
            }
        });    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryTitle;
        TextView tvCategoryNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryTitle = itemView.findViewById(R.id.tvCategoryTitle);
            tvCategoryNumber = itemView.findViewById(R.id.tvCategoryNumber);
        }
    }
}
