package dtos;

import java.util.ArrayList;
import java.util.List;

public class UpdateTripDTO {
    private Integer id;
    private String name;
    private String dateTime;
    private String location;
    private int duration;
    private String guideId;
    private List<PackingItemDTO> packingItems = new ArrayList<>();

    public UpdateTripDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public List<PackingItemDTO> getPackingItems() {
        return packingItems;
    }

    public void setPackingItems(List<PackingItemDTO> packingItems) {
        this.packingItems = packingItems;
    }
}
