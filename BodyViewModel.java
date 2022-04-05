package com.twon.soundview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.twon.soundview.data.Body;
import com.twon.soundview.data.Repository;
import com.twon.soundview.gen.Mapper;
import com.twon.soundview.gen.Organizer;
import com.twon.soundview.gen.Source;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class BodyViewModel extends AndroidViewModel {

    private final Repository repository;

    public BodyViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public void insert(Body body) {repository.insert(body);}

    public void insertList(List<Body> bodies) {repository.insertList(bodies);}

    public Flowable<Body> getBody(Body body) {return repository.getBody(body);}

    public Flowable<List<Body>> getBodiesBySource(Source source) {
        return repository.getBodiesBySource(source);
    }

    public Flowable<List<Body>> getBodiesByOrganizer(Organizer organizer) {
        return repository.getBodiesByOrganizer(organizer);
    }

    public Flowable<List<Body>> getBodiesByMapper(Mapper mapper) {
        return repository.getBodiesByMapper(mapper);
    }
}
