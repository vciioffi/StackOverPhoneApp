package com.example.stackoverphone.firebaseMessaging;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
