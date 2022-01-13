package dtos;

import entities.Guide;

public class GuideDTO {
    private int id;
    private String name;
    private String gender;
    private int birthYear;
    private String image;

    public GuideDTO(Guide guide) {
        if(guide.getId() != null){
            this.id = guide.getId();
        }
        this.name = guide.getName();
        this.gender = guide.getGender();
        this.birthYear = guide.getBirthYear();
        this.image = guide.getImage();
    }
}
