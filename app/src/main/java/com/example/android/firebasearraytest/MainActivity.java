package com.example.android.firebasearraytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView mPushContentsTextView;
    EditText mPushItemEditText;
    Firebase mFirebase;

    TextView mArrayContentsTextView;
    EditText mArrayItemEditText;

    ArrayList<String> mArrayList;

    // TODO Make a new app and put its URL here
    public static final String FIREBASE_URL = "https://testingarray.firebaseio.com/";
    public static final String FIREBASE_MESSAGE_PATH = "messages_push";
    public static final String FIREBASE_ARRAY_PATH = "messages_array";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPushContentsTextView = (TextView) findViewById(R.id.push_contents);
        mPushItemEditText = (EditText) findViewById(R.id.to_push);
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);

        /**
         * This is the code to display what's in the list key'd with push IDs
         */
        mFirebase.child(FIREBASE_MESSAGE_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    /* This prints out a "default" ordering of what's in the database*/
                    mPushContentsTextView.setText(dataSnapshot.getValue().toString());

                    /* This tries to take the code out as a simple Map */
                    /* String messages = "";
                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                    for (String message : map.values()) {
                        messages += message + "\n";
                    }
                    mPushContentsTextView.setText(messages);*/


                    /* Try turning the code into a TreeMap to see what it does to ordering */
                    /*String messages = "";
                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                    TreeMap<String,String> treeMap = new TreeMap<String, String>(map);
                    for (String message : treeMap.values()) {
                        messages += message + "\n";
                    }
                    mPushContentsTextView.setText(messages);*/

                } else {
                    mPushContentsTextView.setText("");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        mArrayList = new ArrayList<>();
        mArrayContentsTextView = (TextView)findViewById(R.id.array_contents);
        mArrayItemEditText = (EditText)findViewById(R.id.to_array);
        /**
         * This is the code to display what's in the ArrayList that was pushed to Firebase
         */
        mFirebase.child(FIREBASE_ARRAY_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null ) {
                    /* Grabs the ArrayList from Firebase and prints out its' contents in order */
                    String arrayList = "";
                    mArrayList = dataSnapshot.getValue(ArrayList.class);
                    for(String item : mArrayList) {
                        arrayList += item + "\n";
                    }
                    mArrayContentsTextView.setText(arrayList);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void onPushItToFirebaseClicked(View view) {
        /* Pushes the message the user put to the database */
        mFirebase.child(FIREBASE_MESSAGE_PATH).push().setValue(mPushItemEditText.getText().toString());

    }

    public void onAddItToArrayClicked(View view) {
        /* Add the message the user put to a growing array list and then push
        the ArrayList to Firebase. */
        mArrayList.add(mArrayItemEditText.getText().toString());
        mFirebase.child(FIREBASE_ARRAY_PATH).setValue(mArrayList);

    }
}
