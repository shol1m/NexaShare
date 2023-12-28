package com.example.nexashare.FCM;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

    }
    private void updateToken(String refreshToken){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Token token= new Token(refreshToken);

    }
}
