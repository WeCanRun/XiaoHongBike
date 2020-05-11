package com.chb.share_bike.service;

import com.chb.share_bike.pojo.Bike;
import org.springframework.data.geo.GeoResult;

import java.util.List;

public interface BikeService {
    void addBike(Bike bike);

    List<GeoResult<Bike>> findNear(double longitude, double latitude);
}
