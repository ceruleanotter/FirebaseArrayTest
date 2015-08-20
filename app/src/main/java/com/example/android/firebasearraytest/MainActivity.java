package com.example.android.firebasearraytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity {

    TextView mArrayContentsTextView;
    EditText mPushItemEditText;
    Firebase mFirebase;

    public static final String FIREBASE_URL = "https://testingarray.firebaseio.com/";
    public static final String FIREBASE_MESSAGE_PATH = "messages";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArrayContentsTextView = (TextView) findViewById(R.id.array_contents);
        mPushItemEditText = (EditText) findViewById(R.id.to_push);
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL).child(FIREBASE_MESSAGE_PATH);
        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    /*mArrayContentsTextView.setText(dataSnapshot.getValue().toString());*/


                    //i don't know what order things are getting put in
                   /* String messages = "";
                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                    for (String message : map.values()) {
                        messages += message + "\n";
                    }
                    mArrayContentsTextView.setText(messages);*/

                    //TreeMap - will sort by the key, so will be by order
                    String messages = "";
                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                    TreeMap<String,String> treeMap = new TreeMap<String, String>(map);
                    for (String message : treeMap.values()) {
                        messages += message + "\n";
                    }
                    mArrayContentsTextView.setText(messages);



                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPushClicked(View view) {
        mFirebase.push().setValue(mPushItemEditText.getText().toString());

    }
}
