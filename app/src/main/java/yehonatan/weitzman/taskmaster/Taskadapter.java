        package yehonatan.weitzman.taskmaster;

        import static yehonatan.weitzman.taskmaster.FirebasecController.getAuth;
        import static yehonatan.weitzman.taskmaster.FirebasecController.getReference;

        import android.graphics.Color;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.TextView;
        import android.content.Context;
        import android.widget.Toast;

        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;
        public class Taskadapter extends RecyclerView.Adapter<Taskadapter.TaskViewHolder> {
            FirebasecController firebasecController;
                //this context we will use to inflate the layout
                private Context mCtx;
                //we are storing all the products in a list
                private List<ItemTask> productList;

                //getting the context and product list with constructor
                public Taskadapter(Context mCtx, List<ItemTask> productList)
                {
                    this.mCtx = mCtx;
                    this.productList = productList;
                    firebasecController = new FirebasecController();
                    //firebasecController.setContext(mCtx);

                }


            @Override
            public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //inflating and returning our view holder
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View view = inflater.inflate(R.layout.recycleritem_task, null);
                return new TaskViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TaskViewHolder holder, int position) {
                //getting the product of the specified position
                ItemTask product = productList.get(position);

                //product.getIdTask();

                //binding the data with the viewholder views
                holder.tvName.setText(product.getName());

                 holder.checkBox.setChecked(false);

                if(holder.checkBox.isChecked()) {

                }

                    // !!!!!!!!!!!!!!!!!!!
//                נסיון של מחיקת פריט ספציפי, בעיה בשורה 70. המסך קורס בלחציה
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // קבלת הפריט הנוכחי
                            ItemTask itemTask = productList.get(position);

                            // מחיקת הפריט מ-Firebase
                            firebasecController.deleteTask(itemTask.getIdTask());


                            // מחיקת הפריט מהרשימה המקומית
                            productList.remove(position);

                            // עדכון RecyclerView
                            notifyItemRemoved(position);

                            // הודעת אישור (אופציונלי)
                            Toast.makeText(mCtx, "משימה נמחקה!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                // !!!!!!!!!!!!!!!!!!!!!!!!


                if(product.isLate()){
                   // holder.tvName.setBackgroundColor(Color.parseColor("#FF0000"));
                    holder.tvLate.setBackgroundColor(Color.parseColor("#FF0000"));
                    holder.tvLate.setText(" Late" );
                }
                else{
                    holder.tvLate.setBackgroundColor(Color.parseColor("#008000"));
                    holder.tvLate.setText( "" );
                }




            }


            @Override
            public int getItemCount() {
                return productList.size();
            }


            class TaskViewHolder extends RecyclerView.ViewHolder {
                TextView tvName, tvLate;
                CheckBox checkBox;
                public TaskViewHolder(View itemView) {
                    super(itemView);
                    tvName = itemView.findViewById(R.id.tvName);
                    tvLate = itemView.findViewById(R.id.tvLate);
                    checkBox = itemView.findViewById(R.id.checkBox_IsFinish);
                    //checkBox.setOnCheckedChangeListener(mCtx);

                }


            }







            }
