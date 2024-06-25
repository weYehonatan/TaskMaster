package yehonatan.weitzman.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FirebaseController {
    private static FirebaseAuth mAuth;
    private Context context;
    private static FirebaseDatabase DATABASE;
    private static DatabaseReference REFERENCE;

    public FirebaseController() {

    }

    public FirebaseController(Context context) {
        this.context = context;
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


    public static FirebaseAuth getAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }


    // user connect?
     public boolean currentUser() {
        if (getAuth().getCurrentUser() != null)
            return true;
        return false;
    }

    public void LogOut() {
        getAuth().signOut();
        context.startActivity(new Intent(context, SignInActivity.class));
    }

    public void changeUserName(String newUserName){
        if (!newUserName.trim().isEmpty()) {
            if (currentUser()) {
                getReference().child(getAuth().getCurrentUser().getUid()).child("name").setValue(newUserName)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "שם המשתמש עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "שגיאה בעדכון שם המשתמש", Toast.LENGTH_SHORT).show();
                                    Log.e("Firebase", "Error updating user name", task.getException());
                                }
                            }
                        });
            } else {
                Toast.makeText(context, "משתמש אינו מחובר", Toast.LENGTH_SHORT).show();
            }
        }
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
                            saveCategory("Home");
                            saveCategory("School");
                            saveCategory("Other");
                        } else {
                            Toast.makeText(context, "eror" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveTask(ItemTask itemTask) {
        itemTask.setIdCreatUser(getAuth().getCurrentUser().getUid());
        getReference().child(getAuth().getCurrentUser().getUid()).child("task").push().setValue(itemTask);
    }
    public void saveCategory(String category) {
        getReference().child(getAuth().getCurrentUser().getUid()).child("category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean bool = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String str = data.getValue(String.class);
                    if(str.equals(category)){
                        bool = true;
                    }
                }
                if(!bool){
                    getReference().child(getAuth().getCurrentUser().getUid()).child("category").child(category).setValue(category);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void readCategory(FirebaseCallback firebaseCallback){
        getReference().child(getAuth().getCurrentUser().getUid()).child("category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> categoryArrayList = new ArrayList<>();

                for(DataSnapshot data:snapshot.getChildren()){
                    String category = data.getValue(String.class);
                    categoryArrayList.add(category);
                }
                firebaseCallback.callbackCategory(categoryArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void deleteCategory(String category){
        getReference().child(getAuth().getCurrentUser().getUid()).child("category").child(category).removeValue();
    }

    public void deleteTask(String taskId) {
        DatabaseReference myRef = getReference().child(getAuth().getCurrentUser().getUid()).child("task").child(taskId);
        myRef.removeValue();
    }

    public void deleteShereTask(String idUser,String idTask){
       getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(idUser).child(idTask).removeValue();
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

    public void readUserID(FirebaseCallback firebaseCallback) {
        getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child("userID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> arrayUSerID = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String str = data.getValue(String.class);
                    arrayUSerID.add(str);
                }
                firebaseCallback.callbackUserID(arrayUSerID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void saveShereTask(String creatorID, String itemTaskID) {
        // ~~~~ save "itemTaskID" ~~~~
        getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(creatorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean bool = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String str = data.getValue(String.class);
                    if(str.equals(itemTaskID)){
                        bool = true;
                    }
                }
                if(!bool){
                    getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(creatorID).child(itemTaskID).setValue(itemTaskID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // ~~~~ save "creatorID" ~~~~
        getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child("userID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean bool = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String str = data.getValue(String.class);
                    if(str.equals(creatorID)){
                        bool = true;
                    }
                }
                if(!bool){
                    getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child("userID").push().setValue(creatorID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readShereTask(FirebaseCallback firebaseCallback,ArrayList<String> idUserList) {
        ArrayList<ItemTask> taskArrayList = new ArrayList<>();
        for (int i = 0; i<idUserList.size();i++) {
            String userId = idUserList.get(i);
            getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taskArrayList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String keyTask = data.getValue(String.class); // get ID task
                        getReference().child(userId).child("task").child(keyTask).addListenerForSingleValueEvent (new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    ItemTask itemTask = snapshot.getValue(ItemTask.class);
                                    itemTask.setIdTask(keyTask);
                                    taskArrayList.add(itemTask);
                                    firebaseCallback.callbackTask(taskArrayList);
                                }
                                else {
                                    deleteShereTask(userId,keyTask);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
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

    public static boolean checkDay(ItemTask task) {
        int year = task.getYearDate();
        int month = task.getMonthDate();
        int day = task.getDayDate();

        Calendar systemCalender = Calendar.getInstance();
        DateItem currentDate = new DateItem(systemCalender.get(Calendar.YEAR),systemCalender.get(Calendar.MONTH)+1,systemCalender.get(Calendar.DAY_OF_MONTH));
        DateItem taskDate = new DateItem(year,month,day);
        // יש דילי של חודש בדיוק בcurrentDate לכן עשיתי "+1"

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

    public void readTasksByCategory(String category, FirebaseCallback firebaseCallback) {
        Query query;
        if( category.equals("all")){
            query = getReference().child(getAuth().getCurrentUser().getUid()).child("task");
        }
        else {
            query = getReference().child(getAuth().getCurrentUser().getUid()).child("task")
                    .orderByChild("category").equalTo(category);
        }
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<ItemTask> tasks = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            ItemTask task = data.getValue(ItemTask.class);
                            if (task != null) {
                                task.setIdTask(data.getKey());
                                tasks.add(task);
                            }
                        }
                        firebaseCallback.callbackTask(tasks);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error fetching tasks by category", error.toException());
                    }
                });
    }



}
