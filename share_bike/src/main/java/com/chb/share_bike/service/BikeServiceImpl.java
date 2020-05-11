package com.chb.share_bike.service;

import com.chb.share_bike.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeServiceImpl implements BikeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addBike(Bike bike) {
        long bikeNo = (long) ((Math.random() + 1) * 1000000000);
        bike.setBikeNo(bikeNo);
        mongoTemplate.insert(bike);
    }

    @Override
    public List<GeoResult<Bike>> findNear(double longitude, double latitude) {

        NearQuery nearQuery = NearQuery.near(longitude, latitude)
                .maxDistance(0.5, Metrics.KILOMETERS)
                .query(new Query(Criteria.where("status").is(0)))
                .limit(20);
        GeoResults<Bike> geoResults = mongoTemplate.geoNear(nearQuery, Bike.class);
//        GeoResults<Bike> geoResults = mongoTemplate.query(Bike.class)
//                .near(NearQuery.near(new GeoJsonPoint(longitude, latitude), Metrics.KILOMETERS))
//                .all();
        return geoResults.getContent();
    }
}
