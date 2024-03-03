package yehonatan.weitzman.taskmaster;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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

    public static FirebaseAuth getAuth()
    {
 //       if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }



    // user connect?
    public boolean currentUser()
    {
        FirebaseUser isFbUser= getAuth().getCurrentUser();
        if(isFbUser!=null)
            return true;
        return false;
    }

    public void LogOut()
    {
        getAuth().signOut();
        context.startActivity(new Intent(context, OpeningActivity.class));
    }

    public void creatUser(String email, String password, String userName) {
        getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(userName, task.getResult().getUser().getUid() ,email);
                            getReference().child(user.getId()).setValue(user); // save in Realtime Database

                            Intent intent=new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                        } else {;
                            Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void saveTask(ItemTask itemTask ){
            getReference().child(getAuth().getCurrentUser().getUid()).child("task").push().setValue(itemTask);
    }
    public void deleteTask(String taskId) {
        DatabaseReference myRef = getReference().child(getAuth().getCurrentUser().getUid()).child("task").child(taskId);
        myRef.removeValue();
    }




    public void signInUser (String email, String password){
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                        } else {;
                            Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // readUser:
    public void readUser(FirebaseCallback firebaseCallback){
        getReference().child(getAuth().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                firebaseCallback.callbackUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readTask(FirebaseCallback firebaseCallback){
        getReference().child(getAuth().getCurrentUser().getUid()).child("task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<ItemTask> taskArrayList = new ArrayList<>();

                for(DataSnapshot data:snapshot.getChildren()){
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

    public void creatShereTask(ItemTask itemTask ){
        getReference().child(getAuth().getCurrentUser().getUid()).child("mySereTask").push().setValue(itemTask);
    }

    public void saveShereTask(String creatorID,String itemTaskID ){
        getReference().child(getAuth().getCurrentUser().getUid()).child("sereTask").child(creatorID).push().setValue(itemTaskID);
    }


}
