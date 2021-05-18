package com.feng.concurrency.patterns.immutableobject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class VehicleTracker {
    private Map<String, ImmutableLocation> locMap = new ConcurrentHashMap<>();

    public void updateLocation(String vehicleId, ImmutableLocation newLocation) {
        locMap.put(vehicleId, newLocation);
    }
}
