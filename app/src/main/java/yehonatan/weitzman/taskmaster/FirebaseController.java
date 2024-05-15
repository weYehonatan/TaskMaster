package yehonatan.weitzman.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
                            Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveTask(ItemTask itemTask) {
        itemTask.setIdCreatUser(getAuth().getCurrentUser().getUid());
        getReference().child(getAuth().getCurrentUser().getUid()).child("task").push().setValue(itemTask);
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

    public void saveShereTask(String creatorID, String itemTaskID) {
        getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(creatorID).push().setValue(itemTaskID);
    }

    public void readShereTask(FirebaseCallback firebaseCallback,ArrayList<String> idUserList) {
        ArrayList<ItemTask> taskArrayList = new ArrayList<>();
        for (int i = 0; i<idUserList.size();i++) {
            String userId = idUserList.get(i);
            getReference().child(getAuth().getCurrentUser().getUid()).child("shereTask").child(idUserList.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot data : snapshot.getChildren()) {
                        String keyTask = data.getValue(String.class); // get ID task
                        getReference().child(getAuth().getUid()).child("task").child(keyTask).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    ItemTask itemTask = snapshot.getValue(ItemTask.class);
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













}
