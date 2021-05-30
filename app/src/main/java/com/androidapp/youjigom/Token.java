package com.androidapp.youjigom;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Token extends AppCompatActivity {
    FirebaseFirestore fStore;
    FirebaseDatabase fDatabase;

    EditText mFullName;
    EditText mEmail;
    EditText mCountry;

    private DatabaseReference mDatabase;
    String[] Data;

    public Token() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Token(EditText mCountry, EditText mEmail, EditText mFullName) {
        this.mFullName = mFullName;
        this.mEmail= mEmail;
        this.mCountry= mCountry;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fStore = FirebaseFirestore.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = fDatabase.getInstance().getReference().child("users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notification manager 생성!
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                String id = "message_push"; // 채널의 id 지정!
                CharSequence name = getString(R.string.channel_name); //사용자가 볼 수 있는 채널 이름!
                String description = getString(R.string.channel_description); // 채널에 대한 설명!

                int importance = NotificationManager.IMPORTANCE_DEFAULT; // 중요도를 default로 설정!

                // 채널을 생성해줍니다!
                NotificationChannel push_channel = new NotificationChannel(id, name, importance);

                push_channel.setDescription(description);

                notificationManager.createNotificationChannel(push_channel); //채널을 등록해줍니다!

                int notifyID = 1; //알림의 ID
                String CHANNEL_ID = "message_push";

                //알림 채널에 push라는 알림을 만들어 연결합니다!
                Notification push = new Notification.Builder(Token.this, CHANNEL_ID)
                        .setContentTitle("New Message")
                        .setContentText("장지민님으로부터 사진 도착!!") //데이터베이스에 저장된 사용자의 이름과 카테고리로 수정 필요
                        .setSmallIcon(R.drawable.bell)
                        .setChannelId(CHANNEL_ID)
                        .build();

                notificationManager.notify(notifyID, push);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    
    public void Read(){
        DatabaseReference reference = fDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                value = value.replace("{","");
                value = value.replace("}","");

                Data = value.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}