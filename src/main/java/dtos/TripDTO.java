package dtos;

import entities.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripDTO {
    private int id;
    private String name;
    private String dateTime;
    private String location;
    private int duration;
    private List<PackingItemDTO> packingItems = new ArrayList<>();
    private GuideDTO guide;

    public TripDTO(Trip trip) {
        if(trip.getId() != null){
            this.id = trip.getId();
        }
        this.name = trip.getName();
        this.dateTime = trip.getDateTime().toString();
        this.location = trip.getLocation();
        this.duration = trip.getDuration();
        trip.getPackingItemList().forEach(packingItem -> this.packingItems.add(new PackingItemDTO(packingItem)));
        if(trip.getGuide() != null){
            this.guide = new GuideDTO(trip.getGuide());
        }
    }

    public static List<TripDTO> getTripDTOs (List<Trip> trips){
        List<TripDTO> tripDTOS = new ArrayList<>();
        trips.forEach(t ->{
            tripDTOS.add(new TripDTO(t));
        });
        return tripDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripDTO)) return false;
        TripDTO tripDTO = (TripDTO) o;
        if(id == tripDTO.id){
            return true;
        }
        return false;
    }


}
