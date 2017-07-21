package com.anmol.anmolchats;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button send;
    EditText msg;
    ListView chats;
    ArrayList<String> chatlist;
    ArrayAdapter<String>chatadapter;
    DatabaseReference databaseReference;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button)findViewById(R.id.send);
        msg = (EditText)findViewById(R.id.text);
        chats = (ListView)findViewById(R.id.list);
        chatlist = new ArrayList<String>();
        chatadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,chatlist);
        chats.setAdapter(chatadapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
        request_name();
    }
    private void request_name(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = editText.getText().toString();
                if(!TextUtils.isEmpty(username)){

                }else {
                    request_name();
                }
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                request_name();
            }
        });
        builder.show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(msg.getText().toString(),"");
                databaseReference.updateChildren(map);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<String>();
                while (iterator.hasNext()){
                    set.add((String)((DataSnapshot)iterator.next()).getKey());
                }
                chatlist.clear();
                chatlist.addAll(set);
                chatadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,Chat_topic.class);
                String s = ((TextView)view).getText().toString();
                String s1 = username;
                Bundle b = new Bundle();
                b.putString("topic_room",s);
                b.putString("username",s1);
                intent.putExtras(b);
                startActivity(intent);


            }
        });



    }

}
