package com.twon.soundview.data;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.twon.soundview.gen.Mapper;
import com.twon.soundview.gen.Organizer;
import com.twon.soundview.gen.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Flowable;

public class BodyRepository {

    private BodyDao bodyDao;

    public BodyRepository(Application application) {
        SoundviewDatabase database = SoundviewDatabase.getDatabase(application);

        bodyDao = database.bodyDao();
    }

    public void insert(Body body) {
        var record = body.getRecord();
        bodyDao.insert(record);
    }

    public void insertList(List<Body> bodies) {
        List<Body.Record> records = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
             records = bodies
                    .stream()
                    .map(Body::getRecord)
                    .collect(Collectors.toList());
        } else {
            var iterator = bodies.listIterator();

            while (iterator.hasNext()) {
                records.add(iterator.next().getRecord());
            }
        }
        bodyDao.insertList(records);
    }

    public Flowable<Body> getBody(Body body) {
        var record = body.getRecord();
        String sourceRecord = record.getSourceRecord(),
                organizerRecord = record.getOrganizerRecord(),
                mapperRecord = record.getMapperRecord();

        return bodyDao.load(sourceRecord, organizerRecord, mapperRecord)
                .map(Body::getBody);
    }

    public Flowable<List<Body>> getBodiesBySource(Source source) {
        String sourceRecord = source.toString();

        return bodyDao.loadSource(sourceRecord)
                .map(Body::getBodies);
    }

    public Flowable<List<Body>> getBodiesByOrganizer(Organizer organizer) {
        String organizerRecord = organizer.toString();

        return bodyDao.loadOrganizer(organizerRecord)
                .map(Body::getBodies);
    }

    public Flowable<List<Body>> getBodiesByMapper(Mapper mapper) {
        String mapperRecord = mapper.toString();

        return bodyDao.loadMapper(mapperRecord)
                .map(Body::getBodies);
    }
}
