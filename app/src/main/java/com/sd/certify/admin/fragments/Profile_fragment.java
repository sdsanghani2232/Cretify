package com.sd.certify.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sd.certify.R;
import com.sd.certify.login.SigninActivity;

public class Profile_fragment extends Fragment {

    Button SignOut;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    public Profile_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
//        SignOut = view.findViewById(R.id.sing_out);
//
//        if(user == null)
//        {
//            startActivity(new Intent(requireContext(), SigninActivity.class));
//            requireActivity().finish();
//        }
//
//        SignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });
//
//
//        return view;
//    }
//    public void signOut() {
//        FirebaseAuth.getInstance().signOut();
//        GoogleSignInClient client = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
//        client.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                startActivity(new Intent(requireContext(),  SigninActivity.class));
//                requireActivity().finish();
//            }
//        });
//    }
        return view;
    }
}