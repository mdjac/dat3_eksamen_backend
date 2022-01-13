package dtos;

import entities.PackingItem;
import entities.Trip;
import errorhandling.API_Exception;
import utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class AddTripDTO {
    private String name;
    private String dateTime;
    private String location;
    private String duration;
    private List<String> packingItems = new ArrayList<>();

    public AddTripDTO() {

    }

    public Trip getTripEntity() throws Exception {
        try{
            Trip trip = new Trip(this.name, Utility.stringToDateFormatter(this.dateTime),this.location,Integer.parseInt(this.duration));
            if(this.packingItems.size() > 0){
                this.packingItems.forEach(pi -> {
                    trip.addPackingItemList(new PackingItem(pi));
                });
            }

            return trip;
        } catch (Exception e){
            throw new API_Exception("Error while creating trip, check dateTime format!");
        }
    }

    public List<String> getPackingItems() {
        return packingItems;
    }
}
