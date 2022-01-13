package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dtos.TripDTO;
import entities.Role;
import entities.Trip;
import entities.User;
import facades.ExamFacade;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.StartDataSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ExamEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static ExamFacade facade;



    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ExamFacade.getExamFacade(emf);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST

    @BeforeEach
    public void setUp() {
        StartDataSet.setupInitialData(emf);
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;


    //Utility method to login and set the returned securityToken
    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void test_VerifyConnection() {
        login(StartDataSet.user.getUserName(), "testUser");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().get("/exam/verify")
                .then()
                .statusCode(200)
                .body("number", equalTo(StartDataSet.users.size()));
    }

    @Test
    public void testGetTrips_tripContainsSpecific() {
        login(StartDataSet.user.getUserName(), "testUser");
        List<TripDTO> trips = given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().get("/exam/trips")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("$", TripDTO.class);
        assertThat(trips, hasItems(
                new TripDTO(StartDataSet.trip1),
                new TripDTO(StartDataSet.trip2)
        ));
    }

    @Test
    public void testGetTrips_tripsHasSize() {
        login(StartDataSet.user.getUserName(), "testUser");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().get("/exam/trips")
                .then()
                .statusCode(200)
                .body("$", hasSize(StartDataSet.trips.size()));
    }

    @Test
    public void testAddUserToTrip() {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("tripId", StartDataSet.trip3.getId());
        login(StartDataSet.user.getUserName(),"testUser");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(jsonBody.toString())
                .when().post("/exam/trips")
                .then()
                .body("message", equalTo("Added!"));
    }

    @Test
    public void testAddUserToTrip_AlreadyAddedToTrip() {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("tripId", StartDataSet.trip1.getId());
        login(StartDataSet.user.getUserName(),"testUser");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(jsonBody.toString())
                .when().post("/exam/trips")
                .then()
                .body("message", equalTo("User is already added to the trip!"));
    }

    @Test
    public void testDeleteTrip_CheckForSuccessfullResponse() {
        int idToDelete = StartDataSet.trip1.getId();
        login(StartDataSet.admin.getUserName(),"testAdmin");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().delete("/exam/trips/"+idToDelete)
                .then()
                .body("message", equalTo("Deleted trip with ID: "+idToDelete));
    }

    @Test
    public void testDeleteTrip_CheckThatOnlyAdminCanDelete() {
        int idToDelete = StartDataSet.trip1.getId();
        login(StartDataSet.user.getUserName(),"testUser");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().delete("/exam/trips/"+idToDelete)
                .then()
                .body("message", equalTo("You are not authorized to perform the requested operation"));
    }






}
