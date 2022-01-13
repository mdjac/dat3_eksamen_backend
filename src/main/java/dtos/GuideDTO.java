package dtos;

import entities.Guide;

public class GuideDTO {
    private Integer id;
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

    public Guide getEntity(){
        Guide guide = new Guide(this.name,this.gender,this.birthYear,this.image);
        if(this.id != null){
            guide.setId(this.id);
        }
        return guide;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
