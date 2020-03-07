package com.geektech.todoapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geektech.todoapp.model.Work;

@Database(entities = {Work.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WorkDao workDao();
}


