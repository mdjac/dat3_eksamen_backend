package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dtos.user.UserDTO;
import entities.Trip;
import entities.User;
import errorhandling.API_Exception;
import groovyjarjarantlr4.v4.runtime.atn.StarLoopEntryState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.StartDataSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade userFacade;
    private static ExamFacade facade;





    public FacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ExamFacade.getExamFacade(emf);
        userFacade = UserFacade.getUserFacade(emf);
    }


    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        StartDataSet.setupInitialData(emf);
    }


    // TODO: Delete or change this method
    @Test
    public void testTrue() throws Exception {
        EntityManager em = emf.createEntityManager();
        User _both;
        try {
            _both = em.find(User.class, StartDataSet.both.getUserName());

        } finally{
            em.close();
        }


        assertEquals(_both.getUserName(), StartDataSet.both.getUserName());
    }



    @Test
    public void testCreateUser() throws Exception {
        String username = "TEST_NEW_USER";
        JsonObject inputJson = new JsonObject();
        inputJson.addProperty("username", username);
        inputJson.addProperty("password", "testUser");
        JsonArray jsonArray = new JsonArray();
        JsonObject roleObject = new JsonObject();
        roleObject.addProperty("rolename", "user");
        jsonArray.add(roleObject);
        inputJson.add("roles", jsonArray);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        UserDTO userDTO = gson.fromJson(inputJson, UserDTO.class);
        userFacade.createUser(userDTO);

        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        Assertions.assertNotNull(user);
    }

    @Test
    public void testGetAllTrips() throws Exception {
        List<Trip> fetchedTrips = facade.getAllTrips();
        assertEquals(fetchedTrips.size(),StartDataSet.trips.size());
    }

    @Test
    public void testAddUserToTrip_newTrip() throws Exception {
        int expected = StartDataSet.trip3.getUsers().size()+1;
        String username = StartDataSet.user.getUserName();
        int tripid = StartDataSet.trip3.getId();
        facade.addUserToTrip(tripid,username);
        EntityManager em = emf.createEntityManager();
        Trip trip = em.find(Trip.class, tripid);
        int actual = trip.getUsers().size();
        assertEquals(expected,actual);
    }

    @Test
    public void testAddUserToTrip_alreadyAddedToTrip() {
        int tripid = StartDataSet.trip1.getId();
        String username = StartDataSet.user.getUserName();
        API_Exception error = Assertions.assertThrows(API_Exception.class, () -> {
            facade.addUserToTrip(tripid,username);
        });
        assertEquals("User is already added to the trip!", error.getMessage());
    }

    @Test
    public void testDeleteTrip() throws API_Exception {
        int deleteId = StartDataSet.trip1.getId();
        facade.deleteTrip(deleteId);
        EntityManager em = emf.createEntityManager();
        Trip deletedTrip = em.find(Trip.class,StartDataSet.trip1.getId());
        Assertions.assertNull(deletedTrip);
    }

    @Test
    public void testDeleteTrip_UserOnlyGetTheSpecificTripDeleted() throws API_Exception {
        int expected = StartDataSet.user.getTrips().size()-1;
        int deleteId = StartDataSet.trip1.getId();
        facade.deleteTrip(deleteId);
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class,StartDataSet.user.getUserName());
        int actual = user.getTrips().size();
        assertEquals(expected,actual);
    }





}
