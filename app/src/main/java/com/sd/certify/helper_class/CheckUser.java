package com.sd.certify.helper_class;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sd.certify.admin.activitys.Admin_Home_Page;
import com.sd.certify.user.activitys.User_Home_page;

import java.util.HashMap;

public class CheckUser {

    Context context;

    public CheckUser(Context context) {
        this.context = context;
    }

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;



    public void AuthenticateEmile(Intent Data)
    {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(Data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

            auth.signInWithCredential(credential)
                    .addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful())
                        {
                            user = auth.getCurrentUser();
                            if(user != null) checkAdmin(user.getEmail());
                        }
                        else
                        {
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
    // check Admin
    public void checkAdmin(String email) {

        DocumentReference reference = db.collection("admin").document(email);

        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(new Intent(context, Admin_Home_Page.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .startActivities();
            }
            else checkUser(email);
        });
    }

    // check user
    private void checkUser(String email) {

        DocumentReference reference = db.collection("user").document(email);
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists())
            {
                TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(new Intent(context, User_Home_page.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        .startActivities();
            }
            else addUser(email);
        });
    }
    // add new user
    private void addUser(String email) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("user").document(email)
                .set(new HashMap<String,Object>()
                {
                    {
                        put("email",email);
                    }

                })
                .addOnSuccessListener(unused -> TaskStackBuilder.create(context)
                            .addNextIntentWithParentStack(new Intent(context, User_Home_page.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            .startActivities());
    }
}
