package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
}
