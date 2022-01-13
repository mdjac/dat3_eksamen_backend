package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guide")
@NamedQuery(name = "Guide.deleteAllRows", query = "DELETE from Guide")
public class Guide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "profile")
    private String profile;

    @Column(name = "image_url")
    private String image;

    @OneToMany(mappedBy = "guide")
    private List<Trip> trips = new ArrayList<>();

    public Guide() {
    }

    public Guide(String name, String gender, int birthYear, String profile, String image) {
        this.name = name;
        this.gender = gender;
        this.birthYear = birthYear;
        this.profile = profile;
        this.image = image;
    }


    public List<Trip> getTrips() {
        return trips;
    }

    public void addTrip(Trip trip) {
        if(trip != null){
            this.trips.add(trip);
            trip.setGuide(this);
        }
    }
    public void removeTrip(Trip trip) {
        if(trip != null){
            this.trips.remove(trip);
            trip.setGuide(null);
        }
    }


}
