package com.geektech.todoapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.geektech.todoapp.ui.onboard.OnBoardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.io.File;
import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {


    private final int RC_WRITE_EXTERNAL = 101;

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isShown = Prefs.getInstance(this).isShown();
        if (!isShown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, FormActivity.class), 100);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        initFile();
    }


    @AfterPermissionGranted(RC_WRITE_EXTERNAL)
    private void initFile() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (EasyPermissions.hasPermissions(this, permission)) {
            File folder = new File(Environment.getExternalStorageDirectory(), "ToDoApp");
            folder.mkdir();
            File file = new File(folder, "note.txt");
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, "Разреши!", RC_WRITE_EXTERNAL, permission);
        }
    }

//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions, grantResults, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
        }
        if (id == R.id.action_textsize) {
            startActivityForResult(new Intent(this, SizeActivity.class), 100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) ;
        {
            String title = data.getStringExtra("title");

        }
    }

   public void onClick(MenuItem item) {
        Prefs.getInstance(this).Delete();
       finish();

   }

    public void onClicksize(MenuItem item) {
       startActivity(new Intent(MainActivity.this, SizeActivity.class));
   }
}
