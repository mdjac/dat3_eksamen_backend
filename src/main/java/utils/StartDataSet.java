package utils;

import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StartDataSet {

    public static User user,admin,both,newUser;
    public static Role userRole,adminRole;
    public static Trip trip1,trip2,trip3,trip4,trip5,trip6;
    public static PackingItem pI1,pI2,pI3,pI4,pI5,pI6,pI7;
    public static Guide guide1,guide2,guide3;
    public static List<Trip> trips;
    public static List<User> users;

    public static void main(String[] args) {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        setupInitialData(emf);
    }

    //Entity managerFactory is deciding whether the data is to test or prod database.
    //Is called both from rest and test cases
    public static void setupInitialData(EntityManagerFactory _emf){
        EntityManager em = _emf.createEntityManager();
        trips = new ArrayList<>();
        users = new ArrayList<>();
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

            trip1 = new Trip("testname","testTime","testLocation",1);
            trip2 = new Trip("testname","testTime","testLocation",2);
            trip3 = new Trip("testname","testTime","testLocation",3);
            trip4 = new Trip("testname","testTime","testLocation",4);
            trip5 = new Trip("testname","testTime","testLocation",5);
            trip6 = new Trip("testname","testTime","testLocation",6);
            trips.add(trip1);
            trips.add(trip2);
            trips.add(trip3);
            trips.add(trip4);
            trips.add(trip5);
            trips.add(trip6);


            pI1 = new PackingItem("Name1");
            pI2 = new PackingItem("Name2");
            pI3 = new PackingItem("Name3");
            pI4 = new PackingItem("Name4");
            pI5 = new PackingItem("Name5");
            pI6 = new PackingItem("Name6");
            pI7 = new PackingItem("Name7");

            guide1 = new Guide("name","male",1,"profile","image");
            guide2 = new Guide("name","male",2,"profile","image");
            guide3 = new Guide("name","male",3,"profile","image");

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
            trip2.addUser(user);
            admin.addTrip(trip3);
            trip4.addUser(both);

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
