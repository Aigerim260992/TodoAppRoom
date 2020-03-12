package com.geektech.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageView;
    Uri imageData;
//    private View context;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView = findViewById(R.id.image_profile);
        preferences = getSharedPreferences("ava", MODE_PRIVATE);
        String ava = preferences.getString("ava", "");
        Glide.with(this).load(ava).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an image"), 47);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 47 && resultCode == RESULT_OK && data != null) {

            try {
                imageData = data.getData();
                InputStream imageStrim = getContentResolver().openInputStream(imageData);
                Bitmap selectImage = BitmapFactory.decodeStream(imageStrim);
                preferences.edit().putString("ava", String.valueOf(imageData)).apply();
                Glide.with(this).load(imageData).into(imageView);
//                imageView.setImageURI(selectImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}