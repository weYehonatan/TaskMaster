        package yehonatan.weitzman.taskmaster;

        import static yehonatan.weitzman.taskmaster.FirebaseController.getAuth;

        import android.app.Dialog;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.content.Context;
        import android.widget.Toast;

        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        public class Taskadapter extends RecyclerView.Adapter<Taskadapter.TaskViewHolder>  {
            FirebaseController firebaseController;
                //this context we will use to inflate the layout
                private Context mCtx;
                //we are storing all the products in a list
                private List<ItemTask> productList;





            //getting the context and product list with constructor
                public Taskadapter(Context mCtx, List<ItemTask> productList)
                {
                    this.mCtx = mCtx;
                    this.productList = productList;
                    firebaseController = new FirebaseController();


                }


            @Override
            public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //inflating and returning our view holder
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View view = inflater.inflate(R.layout.recycleritem_task, null);
                return new TaskViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TaskViewHolder holder,int position) {
                //getting the product of the specified position
                ItemTask product = productList.get(position);

                //binding the data with the viewholder views
                holder.tvTitle.setText(product.getName() );

                holder.tvCategory.setText(product.getCategory() + " " + product.getDayDate() + "/" + product.getMonthDate() +"/" + product.getYearDate());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         //showEditDialog(product);
                        showTaskDialog(product);
                    }
                });

                 holder.checkBox.setChecked(false);



                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // קבלת הפריט הנוכחי
                            ItemTask itemTask = productList.get(position);
                            // מחיקת הפריט מ-Firebase
                            firebaseController.deleteTask(itemTask.getIdTask());
                            // מחיקת הפריט מהרשימה המקומית
                            productList.remove(position);
                            // עדכון RecyclerView
                            notifyItemRemoved(position);
                            // הודעת אישור (אופציונלי)
                            Toast.makeText(mCtx, "משימה נמחקה!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(checkDay(product)){
                    holder.tvLate.setBackgroundColor(Color.parseColor("#FF0000"));
                    holder.tvLate.setText(R.string.late );

                }
                else{
                    holder.tvLate.setBackgroundColor(Color.parseColor("#008000"));
                    holder.tvLate.setText( R.string.check_date );
                }

            }


            @Override
            public int getItemCount() {
                return 0;
            }



            class TaskViewHolder extends RecyclerView.ViewHolder {
                TextView tvTitle, tvLate,tvCategory;
                CheckBox checkBox;
                public TaskViewHolder(View itemView) {
                    super(itemView);
                    tvTitle = itemView.findViewById(R.id.tvTitle);
                    tvCategory = itemView.findViewById(R.id.tvCategory);
                    tvLate = itemView.findViewById(R.id.tvLate);
                    checkBox = itemView.findViewById(R.id.checkBox_IsFinish);
                }


            }


            private void showTaskDialog(ItemTask item) {
                Dialog d = new Dialog(mCtx);
                d.setContentView(R.layout.dialog_task);

                TextView tvTitle = d.findViewById(R.id.tvEditTask);
                TextView tvDescription = d.findViewById(R.id.tvDescription);
                TextView tvDate = d.findViewById(R.id.tvDate);
                TextView tvCategory = d.findViewById(R.id.tvCategory);
                ImageButton btnShare = d.findViewById(R.id.btnShareTask);

                tvTitle.setText(item.getName());
                tvDescription.setText(item.getDescription());
                tvDate.setText(item.getDayDate() + "/"+item.getMonthDate()+"/"+item.getYearDate());
                tvCategory.setText(item.getCategory());

                Button btnEditTask = d.findViewById(R.id.btnEditTask);

                btnEditTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        showEditDialog(item);
                    }
                });
                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT,getAuth().getCurrentUser().getUid() +"/" + item.getIdTask());
                        intent.setType("text/plain");
                        intent.setPackage("com.whatsapp");
                        mCtx.startActivity(intent);
                    }
                });
                d.show();
            }



            private void showEditDialog(ItemTask item) {
                Dialog d = new Dialog(mCtx);
                d.setContentView(R.layout.dialog_edit_task);

                EditText etTitelTask = d.findViewById(R.id.tvEditTask);
                EditText etDescription = d.findViewById(R.id.tvDescription);
                Spinner spinnerCategory = d.findViewById(R.id.spinnerEdit);
                Button btnSave = d.findViewById(R.id.btnSaveEditTask);
                Button btnCancel = d.findViewById(R.id.btnCancelTask);

                etTitelTask.setText(item.getName());
                etDescription.setText(item.getDescription());

                //  ~spiner:~
                ArrayList<String> ArrayCategory = new ArrayList<String>();
                SharedPreferences sp = mCtx.getSharedPreferences("taskMaster",0);
                SharedPreferences.Editor editor=sp.edit();
                String oldCategory = sp.getString("category",null);
                if(oldCategory == null) {
                    editor.putString("category", "Home" + "/" + "Work" + "/" + "Other" + "/");
                }
                editor.commit();

                String[] parts = oldCategory.split("/");
                for (String part : parts) {
                    ArrayCategory.add(part);
                }
                new SpinnerControler(mCtx, spinnerCategory, ArrayCategory);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // עדכון הנתונים של הפריט ברשימת הפריטים
                        item.setName(etTitelTask.getText().toString());
                        item.setCategory(SpinnerControler.getSelected());
                        item.setDescription(etDescription.getText().toString());
                        // לעדכן את RecyclerView
                        notifyItemChanged(productList.indexOf(item));
                        firebaseController.updateTask(item.getIdTask(),item,mCtx);
                        d.dismiss();
                        // !!!!!!!!!!!!!!!לזכור לבצע עדכון לתאריך!!!!!!!!!!

                    }

                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }//dialog




            public static boolean checkDay(ItemTask task) {
                int year = task.getYearDate();
                int month = task.getMonthDate();
                int day = task.getDayDate();

                Calendar systemCalender = Calendar.getInstance();
                DateItem currentDate = new DateItem(systemCalender.get(Calendar.YEAR),systemCalender.get(Calendar.MONTH)+1,systemCalender.get(Calendar.DAY_OF_MONTH));
                DateItem taskDate = new DateItem(year,month,day);
                // היה דילי של חודש בדיוק בcurrentDate לכן עשיתי "+1"

                if(currentDate.getYear() > taskDate.getYear()){
                    return true;
                }
                else if (currentDate.getYear() < taskDate.getYear()) {
                    return false;
                }
                else {
                    if (currentDate.getMonth() > taskDate.getMonth()){
                        return true;
                    }
                    else if (currentDate.getMonth() < taskDate.getMonth()) {
                        return false;
                    }
                    else {
                        if (currentDate.getDay() > taskDate.getDay()){
                            return true;
                        }
                        else if (currentDate.getDay() < taskDate.getDay()) {
                            // בגדול לא צריך את זה
                            return false;
                        }
                    }
                }
                return false;
            }





            }// class
