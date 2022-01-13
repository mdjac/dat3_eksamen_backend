package dtos;

import entities.Guide;

public class GuideDTO {
    private int id;
    private String name;
    private String gender;
    private int birthYear;
    private String profile;
    private String image;

    public GuideDTO(Guide guide) {
        if(guide.getId() != null){
            this.id = guide.getId();
        }
        this.name = guide.getName();
        this.gender = guide.getGender();
        this.birthYear = guide.getBirthYear();
        this.profile = guide.getProfile();
        this.image = guide.getImage();
    }
}
