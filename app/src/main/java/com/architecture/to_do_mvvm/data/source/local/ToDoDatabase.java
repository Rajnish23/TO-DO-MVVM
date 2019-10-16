package com.architecture.to_do_mvvm.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.architecture.to_do_mvvm.data.Task;

/**
 * The Room Database that contains the Task Table
 */
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class ToDoDatabase extends RoomDatabase {

    private static ToDoDatabase INSTANCE;

    public abstract TasksDao getTaskDao();

    private static final Object sLock = new Object();

    public static ToDoDatabase getInstance(Context context){
        synchronized (sLock){
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabase.class,
                        "Tasks.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
