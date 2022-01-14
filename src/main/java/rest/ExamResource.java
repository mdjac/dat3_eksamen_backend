package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import dtos.AddTripDTO;
import dtos.GuideDTO;
import dtos.TripDTO;
import dtos.UpdateTripDTO;
import dtos.user.UserDTO;
import entities.Guide;
import entities.Trip;
import errorhandling.API_Exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("trips/{id}")
    @RolesAllowed("admin")
    public Response deleteTrip(@PathParam("id") int id) throws API_Exception {

        int deletedId = facade.deleteTrip(id);

        JsonObject outputJson = new JsonObject();
        outputJson.addProperty("message","Deleted trip with ID: "+ deletedId);

        return Response.ok().entity(gson.toJson(outputJson)).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("newtrip")
    @RolesAllowed("admin")
    public Response addTrip(String jsonString) throws Exception {
        AddTripDTO addTripDTO = gson.fromJson(jsonString, AddTripDTO.class);
        Trip trip = facade.addTrip(addTripDTO);
        TripDTO tripDTO = new TripDTO(trip);
        return Response.ok().entity(gson.toJson(tripDTO)).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("newguide")
    @RolesAllowed("admin")
    public Response addGuide(String jsonString) throws Exception {
        GuideDTO guideDTO = gson.fromJson(jsonString, GuideDTO.class);
        if(guideDTO.getImage().isEmpty()){
            guideDTO.setImage("https://i.stack.imgur.com/l60Hf.png");
        }
        Guide outputGuide = facade.addGuide(guideDTO);
        guideDTO = new GuideDTO(outputGuide);
        return Response.ok().entity(gson.toJson(guideDTO)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("trips/{id}")
    @RolesAllowed("admin")
    public Response getTrip(@PathParam("id") int id) throws API_Exception {
        Trip trip = facade.getTripById(id);
        TripDTO tripDTO = new TripDTO(trip);
        return Response.ok().entity(gson.toJson(tripDTO)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    @Path("guides")
    public Response getAllGuides() throws IOException, API_Exception {
        List<Guide> guides = facade.getAllGuides();
        List<GuideDTO> guideDtos = GuideDTO.getGuideDTOs(guides);
        return Response.ok().entity(gson.toJson(guideDtos)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("trips")
    @RolesAllowed("admin")
    public Response updateGuideOnTrip(String jsonString) throws API_Exception {
        System.out.println("HIT");
        System.out.println(jsonString);
        UpdateTripDTO updateTripDTO = gson.fromJson(jsonString,UpdateTripDTO.class);
        Trip trip = facade.updateGuideOnTrip(updateTripDTO);
        TripDTO tripDTO = new TripDTO(trip);
        return Response.ok().entity(gson.toJson(tripDTO)).build();
    }
}
