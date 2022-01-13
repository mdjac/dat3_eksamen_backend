package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import dtos.TripDTO;
import entities.Trip;
import errorhandling.API_Exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import facades.ExamFacade;
import utils.EMF_Creator;
import utils.Utility;


@Path("exam")
public class ExamResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final ExamFacade facade = ExamFacade.getExamFacade(EMF);
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user","admin"})
    @Path("verify")
    public Response VerifyConnection() throws IOException, API_Exception {
        //get username from token
        String username = securityContext.getUserPrincipal().getName();
        int number = facade.getUsers();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        jsonObject.addProperty("number",number);
        return Response.ok().entity(gson.toJson(jsonObject)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user","admin"})
    @Path("trips")
    public Response getAllTrips() throws IOException, API_Exception {
        List<Trip> inputTrips = facade.getAllTrips();
        List<TripDTO> trips = TripDTO.getTripDTOs(inputTrips);
        return Response.ok().entity(gson.toJson(trips)).build();
    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user","admin"})
    @Path("trips")
    public Response addUserToTrip(String jsonString) throws API_Exception {
        //Læg input JSON i json Object
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        //Hent username ud fra token
        String username = securityContext.getUserPrincipal().getName();
        int tripId = jsonObject.get("tripId").getAsInt();

        //Kald facade som tilføje brugeren
        facade.addUserToTrip(tripId,username);

        JsonObject outputJson = new JsonObject();
        outputJson.addProperty("message","Added!");
        return Response.ok().entity(gson.toJson(outputJson)).build();
    }
}
