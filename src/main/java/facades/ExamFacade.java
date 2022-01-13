package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
}
