package com.sd.certify.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sd.certify.R;
import com.sd.certify.helper_class.CheckUser;

public class SigninActivity extends AppCompatActivity {

    SignInButton signIn;
    GoogleSignInOptions options;
    GoogleSignInClient client;
    FirebaseAuth auth;
    ImageView logo;
    ProgressBar progressBar;
    FirebaseUser currentUser;
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 100;

    public void onStart() {
        super.onStart();
        idFind();

        if(currentUser != null)
        {
            new CheckUser(getApplicationContext()).checkAdmin(currentUser.getEmail());
            progressBar.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.GONE);
            signIn.setClickable(false);
            logo.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CheckInternet();
        idFind();

        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        client = GoogleSignIn.getClient(getApplicationContext(),options);

        signIn.setOnClickListener(view -> {

            if(!CheckInternet())
            {
                Toast.makeText(getApplicationContext(), "Check Internet", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = client.getSignInIntent();
                Log.d("login data ",Uri.parse(String.valueOf(intent.getData())).toString());
                startActivityForResult(intent,GOOGLE_SIGN_IN_REQUEST_CODE);
            }

        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("login data ","done1");
        Log.d("login data ", Uri.parse(String.valueOf(data.getData())).toString());

        if(requestCode == GOOGLE_SIGN_IN_REQUEST_CODE && data != null && resultCode == RESULT_OK)
        {
            Log.d("login data ","done");

            new CheckUser(getApplicationContext()).AuthenticateEmile(data);
            progressBar.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.GONE);
            signIn.setClickable(false);
            logo.setVisibility(View.GONE);
        }
    }

    private void idFind() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        progressBar = findViewById(R.id.progressBar);
        signIn = findViewById(R.id.google_sign);
        logo = findViewById(R.id.app_image);
    }


    public boolean CheckInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}