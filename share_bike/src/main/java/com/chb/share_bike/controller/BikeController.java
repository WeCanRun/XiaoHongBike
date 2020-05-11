package com.chb.share_bike.controller;

import com.chb.share_bike.pojo.Bike;
import com.chb.share_bike.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @RequestMapping("/bike/add")
    public boolean addBike(@RequestBody Bike bike) {
        boolean res = true;
        try {
            bikeService.addBike(bike);
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
        }

        return res;
    }

    @GetMapping("/bike/findNear")
    public List<GeoResult<Bike>> findNear(double longitude, double latitude) {
        List<GeoResult<Bike>> nearBikes = bikeService.findNear(longitude, latitude);
        return nearBikes;
    }
}
