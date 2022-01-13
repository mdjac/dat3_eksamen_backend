package dtos;

import entities.PackingItem;

public class PackingItemDTO {
    private int id;
    private String name;

    public PackingItemDTO(PackingItem packingItem) {
        if(packingItem.getId() != null){
            this.id = packingItem.getId();
        }
        this.name = packingItem.getName();
    }
}
