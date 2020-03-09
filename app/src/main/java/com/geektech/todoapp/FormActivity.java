package com.geektech.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geektech.todoapp.model.Work;

import java.util.WeakHashMap;

public class FormActivity extends AppCompatActivity {



    private EditText editTitle;
    private EditText editDesc;

    Work work = new Work();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);

        getIncomingIntent();
    }

    public void onClick(View view) {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString();
        Work work = new Work(title,desc);
        App.getDatabase().workDao().insert(work); // Запись обьекта в базу данных
        Intent intent = new Intent();
        intent.putExtra("title", title);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void getIncomingIntent() {
        Intent intent = getIntent();
        work = (Work) intent.getSerializableExtra("work");
        if (work != null) {
            editTitle.setText(work.getTitle());
            editDesc.setText(work.getDesc());
        }
    }

}
