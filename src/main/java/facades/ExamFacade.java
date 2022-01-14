package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dtos.AddTripDTO;
import dtos.GuideDTO;
import dtos.TripDTO;
import dtos.UpdateTripDTO;
import entities.Guide;
import entities.PackingItem;
import entities.Trip;
import entities.User;
import errorhandling.API_Exception;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.Utility;
import utils.api.MakeOptions;

/**
 *
 * @author mikke
 */
public class ExamFacade {
    private static EntityManagerFactory emf;
    private static ExamFacade instance;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ExamFacade() {
    }

    public static ExamFacade getExamFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ExamFacade();
        }
        return instance;
    }

    public int getUsers(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return users.size();
        } finally {
            em.close();
        }
    }

    public List<Trip> getAllTrips() throws API_Exception {
        List<Trip> trips;
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Trip> query = em.createQuery("SELECT t from Trip t", Trip.class);
            trips = query.getResultList();
            return trips;
        } catch (Exception e){
            throw new API_Exception("Error while fetching trips!");
        } finally{
            em.close();
        }
    }

    public void addUserToTrip(int tripId, String username) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class,username);
            Trip trip = em.find(Trip.class,tripId);
            if(user == null || trip == null){
                throw new API_Exception("Trip or User doesn't exist in database");
            }
            if(trip.getUsers().contains(user)){
                throw new API_Exception("User is already added to the trip!");
            }
            user.addTrip(trip);
            em.getTransaction().commit();
        } catch(Exception e){
            throw new API_Exception(e.getMessage());
        } finally {
            em.close();
        }
    }

    public int deleteTrip(int id) throws API_Exception{
        EntityManager em = emf.createEntityManager();
        Trip t = em.find(Trip.class, id);
        try {
            em.getTransaction().begin();

            if (t == null) {
                throw new API_Exception("Could not find trip with id: " + id);
            }

            t.getUsers().forEach(user -> {
                user.removeTrip(t);
            });

            t.getPackingItemList().forEach(item -> {
                em.remove(item);
            });
            em.remove(t);
            em.getTransaction().commit();
            return id;
        } finally {
            em.close();
        }
    }

    public Trip addTrip(AddTripDTO inputDTO) throws Exception {
        EntityManager em = emf.createEntityManager();
        Trip trip = inputDTO.getTripEntity();
        try {
            em.getTransaction().begin();
            trip.getPackingItemList().forEach(pi ->{
                em.persist(pi);
            });
            em.persist(trip);
            em.getTransaction().commit();
            return trip;
        } catch(Exception e){
            throw new API_Exception(e.getMessage());
        } finally {
            em.close();
        }
    }

    public Guide addGuide(GuideDTO inputDTO) throws Exception {
        EntityManager em = emf.createEntityManager();
        Guide guide = inputDTO.getEntity();
        try {
            em.getTransaction().begin();
            em.persist(guide);
            em.getTransaction().commit();
            return guide;
        } catch(Exception e){
            throw new API_Exception(e.getMessage());
        } finally {
            em.close();
        }
    }

    public Trip getTripById(int id) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        try{
            Trip trip = em.find(Trip.class,id);
            if(trip == null){
                throw new API_Exception("Trip with id: "+id+" not found!");
            }
            return trip;
        } catch (Exception e){
            throw new API_Exception("Error while fetching trip!");
        } finally{
            em.close();
        }
    }

    public List<Guide> getAllGuides() throws API_Exception {
        List<Guide> guides;
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Guide> query = em.createQuery("SELECT g from Guide g", Guide.class);
            guides = query.getResultList();
            return guides;
        } catch (Exception e) {
            throw new API_Exception("Error while fetching guides!");
        } finally {
            em.close();
        }
    }

        public Trip updateGuideOnTrip(UpdateTripDTO updateTripDTO) throws API_Exception {
            EntityManager em = emf.createEntityManager();
            try{
                em.getTransaction().begin();
                Trip trip = em.find(Trip.class,updateTripDTO.getId());
                Guide guide = em.find(Guide.class,Integer.parseInt(updateTripDTO.getGuideId()));
                guide.addTrip(trip);
                em.getTransaction().commit();
                return trip;
            } catch (Exception e){
                throw new API_Exception("Error while updating trip");
            } finally{
                em.close();
            }

    }




}
