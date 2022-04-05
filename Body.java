package com.twon.soundview.data;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.twon.soundview.gen.Mapper;
import com.twon.soundview.gen.Organizer;
import com.twon.soundview.gen.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Body {

    @NonNull
    private final Source source;

    @NonNull
    private final Organizer organizer;

    @NonNull
    private final Mapper mapper;

    public Body(@NonNull Source source, @NonNull Organizer organizer, @NonNull Mapper mapper) {
        this.source = source;
        this.organizer = organizer;
        this.mapper = mapper;
    }

    @NonNull
    public final Source getSource() {return source;}

    @NonNull
    public final Organizer getOrganizer() {return organizer;}

    @NonNull
    public final Mapper getMapper() {return mapper;}

    public final Record getRecord() {return getRecord();}

    public static Body getBody(Record record) {
        String sourceRecord = record.getSourceRecord(),
                organizerRecord = record.getOrganizerRecord(),
                mapperRecord = record.getMapperRecord();

        var source = Source.parseSource(sourceRecord);
        var organizer = Organizer.parseOrganizer(organizerRecord);
        var mapper = Mapper.parseMapper(mapperRecord);

        return new Body(source, organizer, mapper);
    }

    public static List<Body> getBodies(List<Record> records) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return records
                    .stream()
                    .map(Body::getBody)
                    .collect(Collectors.toList());
        } else {
            var iterator = records.listIterator();
            List<Body> bodyList = new ArrayList<>();

            while (iterator.hasNext()) {
                bodyList.add(Body.getBody(iterator.next()));
            }

            return bodyList;
        }
    }

    @Entity(tableName = "body", primaryKeys = {"source", "organizer", "mapper"})
    public static final class Record {

        @NonNull
        @ColumnInfo(name = "source")
        private final String sourceRecord;

        @NonNull
        @ColumnInfo(name = "organizer")
        private final String organizerRecord;

        @NonNull
        @ColumnInfo(name = "mapper")
        private final String mapperRecord;

        public Record(@NonNull String sourceRecord, @NonNull String organizerRecord,
                      @NonNull String mapperRecord) {
            this.sourceRecord = sourceRecord;
            this.organizerRecord = organizerRecord;
            this.mapperRecord = mapperRecord;
        }

        @NonNull
        public final String getSourceRecord() {return sourceRecord;}

        @NonNull
        public final String getOrganizerRecord() {return organizerRecord;}

        @NonNull
        public final String getMapperRecord() {return mapperRecord;}
    }
}
