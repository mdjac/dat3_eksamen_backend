package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "packing_item")
@NamedQuery(name = "PackingItem.deleteAllRows", query = "DELETE from PackingItem")
public class PackingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;


    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    @ManyToOne
    private Trip trip;


    public PackingItem() {
    }

    public PackingItem(String name) {
        this.name = name;
    }

    //For trip relation
    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
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
}
