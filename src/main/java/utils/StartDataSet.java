package utils;

import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartDataSet {

    public static User user,admin,both,newUser;
    public static Role userRole,adminRole;
    public static Trip trip1,trip2,trip3,trip4,trip5,trip6;
    public static PackingItem pI1,pI2,pI3,pI4,pI5,pI6,pI7;
    public static Guide guide1,guide2,guide3;
    public static List<Trip> trips;
    public static List<User> users;
    public static List<Guide> guides;

    public static void main(String[] args) throws Exception {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        setupInitialData(emf);
    }

    //Entity managerFactory is deciding whether the data is to test or prod database.
    //Is called both from rest and test cases
    public static void setupInitialData(EntityManagerFactory _emf) throws Exception {
        EntityManager em = _emf.createEntityManager();
        trips = new ArrayList<>();
        users = new ArrayList<>();
        guides = new ArrayList<>();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("PackingItem.deleteAllRows").executeUpdate();
            em.createNamedQuery("Trip.deleteAllRows").executeUpdate();
            em.createNamedQuery("Guide.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNativeQuery("ALTER TABLE trip AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE packing_item AUTO_INCREMENT = 1").executeUpdate();
            em.createNativeQuery("ALTER TABLE guide AUTO_INCREMENT = 1").executeUpdate();



            user = new User("user", "testUser");
            admin = new User("admin", "testAdmin");
            both = new User("user_admin", "testBoth");
            newUser = new User("new_user", "testNew");
            users.add(user);
            users.add(admin);
            users.add(both);
            users.add(newUser);

            userRole = new Role("user");
            adminRole = new Role("admin");


            String date_time = "13-01-2022 05:35";
            Date date = Utility.stringToDateFormatter(date_time);


            trip1 = new Trip("USA",date,"Miami",1);
            trip2 = new Trip("Denmark",date,"Copenhagen",2);
            trip3 = new Trip("Denmark",date,"Roskilde",3);
            trip4 = new Trip("USA",date,"LA",4);
            trip5 = new Trip("USA",date,"Washington",5);
            trip6 = new Trip("England",date,"London",6);
            trips.add(trip1);
            trips.add(trip2);
            trips.add(trip3);
            trips.add(trip4);
            trips.add(trip5);
            trips.add(trip6);


            pI1 = new PackingItem("Clothes");
            pI2 = new PackingItem("Shoes");
            pI3 = new PackingItem("Knifes");
            pI4 = new PackingItem("Fork");
            pI5 = new PackingItem("Hat");
            pI6 = new PackingItem("Googles");
            pI7 = new PackingItem("Gloves");

            guide1 = new Guide("Jens","male",1995,"https://i.stack.imgur.com/l60Hf.png");
            guide2 = new Guide("Adam","male",1990,"https://i.stack.imgur.com/l60Hf.png");
            guide3 = new Guide("Niels","male",185,"https://i.stack.imgur.com/l60Hf.png");
            guides.add(guide1);
            guides.add(guide2);
            guides.add(guide3);

            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            newUser.addRole(userRole);

            trip1.addPackingItemList(pI1);
            trip2.addPackingItemList(pI2);
            trip3.addPackingItemList(pI3);
            trip4.addPackingItemList(pI4);
            trip5.addPackingItemList(pI5);
            trip6.addPackingItemList(pI6);
            trip1.addPackingItemList(pI7);

            user.addTrip(trip1);
            user.addTrip(trip2);
            admin.addTrip(trip3);
            both.addTrip(trip4);
            admin.addTrip(trip1);

            guide1.addTrip(trip1);
            guide2.addTrip(trip2);
            guide3.addTrip(trip3);
            guide3.addTrip(trip4);
            guide1.addTrip(trip5);
            guide1.addTrip(trip6);

            em.persist(userRole);
            em.persist(adminRole);

            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);

            em.persist(pI1);
            em.persist(pI2);
            em.persist(pI3);
            em.persist(pI4);
            em.persist(pI5);
            em.persist(pI6);
            em.persist(pI7);

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);
            em.persist(trip6);

            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.persist(newUser);



            em.getTransaction().commit();


        } finally {
            em.close();
        }


    }
}
