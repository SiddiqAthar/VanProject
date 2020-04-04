package com.example.privatevanmanagement.ChatModule.ShowActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.UserDetails;
import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.Utils;
import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.activities.UserActivity;
import com.example.privatevanmanagement.models.DriverDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_chat_list extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<DriverDetail_Model> al;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog pd;
    DatabaseReference userRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        getSupportActionBar().setTitle("Chat Room");

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        mAuth = Objects.getInstance();

        al = new ArrayList<>();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    pd.dismiss();
                    Utils.intentWithClear(Student_chat_list.this, UserActivity.class);
                } else {
                    UserDetails.userID = user.getUid();
                    UserDetails.userEmail = user.getEmail();
                }
            }
        };

        DatabaseReference userRef = Objects.getFirebaseInstance().getReference("DriverDetails").child(Objects.getStudentDetailInstance().getDriver_id());
        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

/*
                for (DataSnapshot child : dataSnapshot.getChildren()) {*/

                DriverDetail_Model listDataRef = dataSnapshot.getValue(DriverDetail_Model.class);

                al.add(listDataRef);

//                al.add(dataSnapshot.child("driver_name").getValue().toString());

                /*                }*/
                if (al.isEmpty()) {
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                } else {
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);
                    ArrayList<String> arrayList= new ArrayList<>();
                    arrayList.add(al.get(0).getDriver_name());
                    usersList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_list,  arrayList));
                    userRef2 = com.example.privatevanmanagement.utils.Objects.getFirebaseInstance().getReference().child("users").child(al.get(0).getDriver_id());
                    userRef2.setValue(al.get(0).getDriver_name());
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String checkChild = al.get(position).getDriver_id();
                final String checkName = al.get(position).getDriver_name();
                UserDetails.chat_Uname = checkName;
                UserDetails.chatwithEmail = checkChild;
                Intent intent = new Intent(Student_chat_list.this, Chat.class);
                intent.putExtra("name", checkName);
                intent.putExtra("chatwithEmail", checkChild);
                startActivity(intent);
//                startActivity(new Intent(Student_chat_list.this, Chat.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}