package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dtos.user.UserDTO;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.Utility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade userFacade;
    private static ExamFacade facade;

    private static User user,admin,both;
    private static Role userRole,adminRole;



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
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();

            user = new User("user", "testUser");
            admin = new User("admin", "testAdmin");
            both = new User("user_admin", "testBoth");

            userRole = new Role("user");
            adminRole = new Role("admin");

            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);

            em.persist(userRole);
            em.persist(adminRole);

            em.persist(user);
            em.persist(admin);
            em.persist(both);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }


    // TODO: Delete or change this method
    @Test
    public void testTrue() throws Exception {
        EntityManager em = emf.createEntityManager();
        User _both;
        try {
            _both = em.find(User.class, both.getUserName());

        } finally{
            em.close();
        }


        assertEquals(_both.getUserName(), both.getUserName());
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


}
