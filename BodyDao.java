package com.twon.soundview.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface BodyDao {

    @Insert
    Completable insert(Body.Record record);

    @Insert
    Completable insertList(List<Body.Record> records);

    @Query("SELECT * FROM body " +
            "WHERE source = :sourceRecord AND organizer = :organizerRecord " +
            "AND mapper = :mapperRecord")
    Flowable<Body.Record> load(String sourceRecord, String organizerRecord, String mapperRecord);

    @Query("SELECT * FROM body WHERE source = :sourceRecord")
    Flowable<List<Body.Record>> loadSource(String sourceRecord);

    @Query("SELECT * FROM body WHERE organizer = :organizerRecord")
    Flowable<List<Body.Record>> loadOrganizer(String organizerRecord);

    @Query("SELECT * FROM body WHERE mapper = :mapperRecord")
    Flowable<List<Body.Record>> loadMapper(String mapperRecord);
    
}
