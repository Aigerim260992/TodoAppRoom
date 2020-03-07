package com.geektech.todoapp;

import android.app.Application;

import androidx.room.Room;

import com.geektech.todoapp.room.AppDatabase;


public class App extends Application {

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build(); //создание базы данных
    }

    public static AppDatabase getDatabase() {

        return database;
    }
}
