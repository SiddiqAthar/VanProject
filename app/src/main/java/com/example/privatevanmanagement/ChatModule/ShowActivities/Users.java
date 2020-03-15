package com.example.privatevanmanagement.ChatModule.ShowActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.privatevanmanagement.utils.Objects;
import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.UserDetails;
import com.example.privatevanmanagement.R;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.privatevanmanagement.utils.Objects.UserType;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al;
    //    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Button signout = (Button) findViewById(R.id.sign_out);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    pd.dismiss();
//                    Utils.intentWithClear(Users.this, Login.class);
//                } else {
//                    UserDetails.userID = user.getUid();
//                    UserDetails.userEmail = user.getEmail();
//                }
//            }
//        };

//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users");
        DatabaseReference userRef = null;
        if (UserType.equals("Student")) {
            userRef = FirebaseDatabase.getInstance().getReference("Allocated_to_Driver").child(Objects.getStudentDetailInstance().getStudent_id());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    al = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (!Objects.equals(child.getValue(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        al.add(snapshot.getValue().toString());
//                    }
                    }
                    if (al.isEmpty()) {
                        noUsersText.setVisibility(View.VISIBLE);
                        usersList.setVisibility(View.GONE);
                    } else {
                        noUsersText.setVisibility(View.GONE);
                        usersList.setVisibility(View.VISIBLE);
                        usersList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_list, al));
                    }
                    pd.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else if (UserType.equals("Driver")) {
            userRef = FirebaseDatabase.getInstance().getReference("Allocated_to_Student").child(Objects.getDriverDetailInstance().getDriver_van_id());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    al = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // no need of this line in future
                        al.add(snapshot.child("Student_id").getValue().toString());


                         String id=snapshot.child("Student_id").getValue().toString();

                         DatabaseReference nameRefern = FirebaseDatabase.getInstance().getReference("StudentDetails").child(id);

                         nameRefern.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                 al.add(dataSnapshot.child("StudentName").getValue().toString());

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });

                     }
                    if (al.isEmpty()) {
                        noUsersText.setVisibility(View.VISIBLE);
                        usersList.setVisibility(View.GONE);
                    } else {
                        noUsersText.setVisibility(View.GONE);
                        usersList.setVisibility(View.VISIBLE);
                        usersList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_list, al));
                    }
                    pd.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);


        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String checkChild = al.get(position);
                UserDetails.chatwithEmail = checkChild;
                Intent intent = new Intent(Users.this, Chat.class);
                intent.putExtra("chatwithEmail", checkChild);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetails.chatwithEmail = "";
                UserDetails.userType = "";
                UserDetails.userEmail = "";
                UserDetails.chatwithID = "";
                UserDetails.userID = "";
                UserDetails.chatRef = null;
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }
}