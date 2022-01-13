package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trip")
@NamedQuery(name = "Trip.deleteAllRows", query = "DELETE from Trip")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "dateTime")
    private String dateTime;

    @Column(name = "location")
    private String location;

    @Column(name = "duration")
    private int duration;

    @ManyToMany()
    @JoinTable( // This is now the owner side of the relationsship
            name = "users_trips",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<PackingItem> packingItemList = new ArrayList<>();

    @JoinColumn(name = "guide_id", referencedColumnName = "id")
    @ManyToOne
    private Guide guide;

    public Trip() {
    }

    public Trip(String name, String dateTime, String location, int duration) {
        this.name = name;
        this.dateTime = dateTime;
        this.location = location;
        this.duration = duration;
    }

    //For PackingItem relation
    public List<PackingItem> getPackingItemList() {
        return packingItemList;
    }
    public void addPackingItemList(PackingItem packingItemList) {
        if(packingItemList != null){
            this.packingItemList.add(packingItemList);
            packingItemList.setTrip(this);
        }
    }
    public void removePackingItemList(PackingItem packingItemList) {
        if(packingItemList != null){
            this.packingItemList.remove(packingItemList);
            packingItemList.setTrip(null);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
        if(!user.getTrips().contains(this)){
            user.addTrip(this);
        }
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
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

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setPackingItemList(List<PackingItem> packingItemList) {
        this.packingItemList = packingItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
