package com.free.firebasejsonproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Dataupload extends AppCompatActivity {

    private LinearLayout layoutKeyValuePairs;
    private Button btnAddField, btnSubmit;
    private DatabaseReference databaseReference;
    private Map<String, String> dataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataupload);

        layoutKeyValuePairs = findViewById(R.id.layoutKeyValuePairs);
        btnAddField = findViewById(R.id.btnAddField);
        btnSubmit = findViewById(R.id.btnSubmit);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData");
        dataMap = new HashMap<>();

        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addField();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void addField() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_key_value_pair, null);
        EditText editTextKey = view.findViewById(R.id.editTextKey);
        EditText editTextValue = view.findViewById(R.id.editTextValue);
        Button btnRemove = view.findViewById(R.id.btnRemove);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutKeyValuePairs.removeView(view);
            }
        });

        layoutKeyValuePairs.addView(view);
    }

    private void submitData() {
        // Generate a unique key for each submission
        String submissionKey = databaseReference.push().getKey();
        if (submissionKey != null) {
            for (int i = 0; i < layoutKeyValuePairs.getChildCount(); i++) {
                View view = layoutKeyValuePairs.getChildAt(i);
                EditText editTextKey = view.findViewById(R.id.editTextKey);
                EditText editTextValue = view.findViewById(R.id.editTextValue);

                String key = editTextKey.getText().toString().trim();
                String value = editTextValue.getText().toString().trim();

                if (!key.isEmpty() && !value.isEmpty()) {
                    // Store each data entry under the submission key node
                    databaseReference.child(submissionKey).child(key).setValue(value);
                }
            }

            // Optionally, clear the input fields and dataMap after submission
            layoutKeyValuePairs.removeAllViews();
            dataMap.clear();
        }
    }

}

