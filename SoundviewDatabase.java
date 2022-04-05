package com.twon.soundview.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Body.Record.class}, version = 1, exportSchema = false)
public abstract class SoundviewDatabase extends RoomDatabase {

    public abstract BodyDao bodyDao();
    private static SoundviewDatabase INSTANCE;
    private static final String NAME = "soundview";

    static SoundviewDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SoundviewDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SoundviewDatabase.class, NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
