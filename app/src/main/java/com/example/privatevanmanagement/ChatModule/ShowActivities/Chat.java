package com.example.privatevanmanagement.ChatModule.ShowActivities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.privatevanmanagement.ChatModule.Model.ChatModel;
import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.UserDetails;
import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.Utils;
import com.example.privatevanmanagement.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Chat extends AppCompatActivity {

    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference messageRef, userRef;
    boolean type = false;
    RecyclerView chatRecView;
    Toolbar toolbar;
    String chatwithEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatwithEmail = getIntent().getStringExtra("chatwithEmail");
        Toast.makeText(Chat.this, chatwithEmail, Toast.LENGTH_SHORT).show();
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        chatRecView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.chat_with_toolbar);

//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(UserDetails.chatwithEmail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecView.setHasFixedSize(true);
        chatRecView.setLayoutManager(layoutManager);

        messageRef = FirebaseDatabase.getInstance().getReference("/messages");
        userRef = FirebaseDatabase.getInstance().getReference("/users");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("message", messageText);
//                    map.put("user", UserDetails.userID);
                    map.put("user", com.example.privatevanmanagement.utils.Objects.UserID.Globaluser_ID);

                    DatabaseReference chatRef =FirebaseDatabase.getInstance().getReference("/messages");
                    chatRef.push().setValue(map);

                    messageArea.
                            setText("");
                    messageArea.
                            setHint("Message");
                }
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //int count = 0;
//        valueEventListener(userRef, chatwithEmail);
//
//    }

//    public static class ChatViewHolder extends RecyclerView.ViewHolder {
//
//        TextView chatMessage;
//        TextView userText;
//        LinearLayout linearLayout;
//
//        public ChatViewHolder(View itemView) {
//            super(itemView);
//            chatMessage = (TextView) itemView.findViewById(R.id.text_chat);
//            userText = (TextView) itemView.findViewById(R.id.user_chat);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.lin_lay);
//        }
//
//        public void setChatMessage(String message) {
//            chatMessage.setText(message);
//        }
//
//
//        public void setUserText(String userName, boolean type) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
//            if (type) {
//                //current logged in user
//                params.gravity = Gravity.END;
//                userText.setText("You");
//                linearLayout.setGravity(Gravity.END);
//                linearLayout.setBackgroundResource(R.drawable.bubble_in);
//            } else {
//                params.gravity = Gravity.START;
//                userText.setText(UserDetails.chatwithEmail);
//                linearLayout.setGravity(Gravity.START);
//                linearLayout.setBackgroundResource(R.drawable.bubble_out);
//            }
//            linearLayout.setLayoutParams(params);
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.intentWithClear(Chat.this, Users.class);
    }

//    public void valueEventListener(DatabaseReference dbref, final String checkChild) {
//        messageRef = FirebaseDatabase.getInstance().getReference("/messages");
//
//        dbref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    if (Objects.equals(child.getValue(), checkChild)) {
//                        UserDetails.chatwithID = child.getKey();
//                        final String type1, type2;
//                        type1 = UserDetails.userID + "_" + UserDetails.chatwithID;
//                        type2 = UserDetails.chatwithID + "_" + UserDetails.userID;
//
//                        messageRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                DataSnapshot child1 = dataSnapshot.child(type1);
//                                DataSnapshot child2 = dataSnapshot.child(type2);
//                                if (child1.exists()) {
//                                    UserDetails.userType = "type1";
//                                    UserDetails.chatRef = FirebaseDatabase.getInstance().getReference("/messages").child(type1);
//                                    //chatRef2 = messageRef.child(type1);
//                                } else if (child2.exists()) {
//                                    UserDetails.userType = "type2";
//                                    UserDetails.chatRef = FirebaseDatabase.getInstance().getReference("/messages").child(type2);
//
//                                } else {
//                                    UserDetails.userType = "type1";
//                                    messageRef.child(type1).setValue("none");
//                                    UserDetails.chatRef = FirebaseDatabase.getInstance().getReference("/messages").child(type1);
//                                    //chatRef2 = messageRef.child(type2);
//                                }
//                                if (UserDetails.chatRef != null) {
//                                    FirebaseRecyclerAdapter<ChatModel, ChatViewHolder> firebaseRecyclerAdapter =
//                                            new FirebaseRecyclerAdapter<ChatModel, ChatViewHolder>(
//                                                    ChatModel.class,
//                                                    R.layout.chat_row,
//                                                    ChatViewHolder.class,
//                                                    UserDetails.chatRef) {
//                                                @Override
//                                                protected void populateViewHolder(ChatViewHolder viewHolder, ChatModel model, int position) {
////                                                    final String chatKey = getRef(position).getKey();
//                                                    viewHolder.setChatMessage(model.getMessage());
//                                                    type = Objects.equals(model.getUser(), UserDetails.userID);
//                                                    viewHolder.setUserText(model.getUser(), type);
//
//                                                }
//                                            };
//                                    chatRecView.setAdapter(firebaseRecyclerAdapter);
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
//                                    Utils.intentWithClear(Chat.this, Chat.class);
//                                    finish();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
