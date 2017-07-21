package com.anmol.anmolchats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Chat_topic extends AppCompatActivity {

    Button send;
    TextView msgdisp;
    EditText msg;
    DatabaseReference roottopicname;
    String topicname;
    String username;
    String chatusername;
    String chatmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_topic);
        send = (Button)findViewById(R.id.send);
        msgdisp = (TextView)findViewById(R.id.messagedisp);
        msg = (EditText)findViewById(R.id.message);
        topicname = getIntent().getExtras().get("topic_room").toString();
        username = getIntent().getExtras().get("username").toString();
        setTitle(topicname);
        roottopicname = FirebaseDatabase.getInstance().getReference().getRoot().child(topicname);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childroot = roottopicname.push();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("name",username);
                map.put("message",msg.getText().toString());
                childroot.updateChildren(map);
            }
        });
        roottopicname.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                update_msg(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                update_msg(dataSnapshot);
            }



            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void update_msg(DataSnapshot dataSnapshot) {
        chatmessage = (String)dataSnapshot.child("message").getValue();
        chatusername = (String)dataSnapshot.child("name").getValue();

        msgdisp.append(chatusername + ":" + chatmessage + "\n\n");
    }
}
