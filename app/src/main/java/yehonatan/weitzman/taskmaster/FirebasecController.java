package yehonatan.weitzman.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FirebasecController {
    private static FirebaseAuth mAuth;
    private Context context;
    private static FirebaseDatabase DATABASE;
    private static DatabaseReference REFERENCE;

    public FirebasecController() {

    }


    public static FirebaseDatabase getDatabase() {
        if (DATABASE == null)
            DATABASE = FirebaseDatabase.getInstance();
        return DATABASE;
    }

    public static DatabaseReference getReference() {
        REFERENCE = getDatabase().getReference("Users");
        return REFERENCE;
    }

    public FirebasecController(Context context) {
        this.context = context;
    }

    public static FirebaseAuth getAuth() {
        //       if(mAuth == null)
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }


    // user connect?
    public boolean currentUser() {
        FirebaseUser isFbUser = getAuth().getCurrentUser();
        if (isFbUser != null)
            return true;
        return false;
    }

    public void LogOut() {
        getAuth().signOut();
        context.startActivity(new Intent(context, OpeningActivity.class));
    }

    public void creatUser(String email, String password, String userName) {
        getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(userName, task.getResult().getUser().getUid(), email);
                            getReference().child(user.getId()).setValue(user); // save in Realtime Database

                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } else {
                            ;
                            Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveTask(ItemTask itemTask) {
        getReference().child(getAuth().getCurrentUser().getUid()).child("task").push().setValue(itemTask);
    }

    public void deleteTask(String taskId) {
        DatabaseReference myRef = getReference().child(getAuth().getCurrentUser().getUid()).child("task").child(taskId);
        myRef.removeValue();
    }


    public void signInUser(String email, String password) {
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } else {
                            ;
                            Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // readUser:
    public void readUser(FirebaseCallback firebaseCallback) {
        getReference().child(getAuth().getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user = snapshot.getValue(String.class);
                firebaseCallback.callbackUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readTask(FirebaseCallback firebaseCallback) {
        getReference().child(getAuth().getCurrentUser().getUid()).child("task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<ItemTask> taskArrayList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    ItemTask task1 = data.getValue(ItemTask.class);
                    task1.setIdTask(data.getKey());
                    taskArrayList.add(task1);
                }
                firebaseCallback.callbackTask(taskArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public String creatShereTask(ItemTask itemTask) {
        // כבר לא רלוונטי
        // save the sher task + return the idTask
       String idTask = getReference().child(getAuth().getCurrentUser().getUid()).child("myShereTask").push().getKey();
       getReference().child(getAuth().getCurrentUser().getUid()).child("myShereTask").child(idTask).setValue(itemTask);

        return idTask;
    }


    public void saveShereTask(String creatorID, String itemTaskID) {
        getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(creatorID).push().setValue(itemTaskID);
    }

    public void readShereTask(FirebaseCallback firebaseCallback) {
        // כבר לא רלוונטי

            getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<ItemTask> taskArrayList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    ItemTask task1 = data.getValue(ItemTask.class);
                    task1.setIdTask(data.getKey());
                    taskArrayList.add(task1);
                }
                firebaseCallback.callbackShereTask(taskArrayList);

            }
                @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static boolean checkDay(ItemTask task) {
        int year = task.getYearDate();
        int month = task.getMonthDate();
        int day = task.getDayDate();

        Calendar systemCalender = Calendar.getInstance();
        DateItem currentDate = new DateItem(systemCalender.get(Calendar.YEAR),systemCalender.get(Calendar.MONTH)+1,systemCalender.get(Calendar.DAY_OF_MONTH));
        DateItem taskDate = new DateItem(year,month,day);
        // היה דילי של חודש בדיוק בcurrentDate לכן עשיתי "+1" (שורה 197)

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


    public void updateTask(String taskId, ItemTask updatedTask,Context mCtx ) {
        DatabaseReference taskRef = getReference().child(getAuth().getCurrentUser().getUid()).child("task").child(taskId);
        // עדכון המשימה בבסיס הנתונים
        taskRef.setValue(updatedTask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mCtx, "המשימה עודכנה בהצלחה", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mCtx, "שגיאה בעדכון המשימה", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }













}
