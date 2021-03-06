package facades;

import dtos.user.UserDTO;
import entities.User;
import entities.Role;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    public UserDTO createUser(UserDTO userDTO) throws Exception {
       User user = userDTO.getEntity();
       EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            //Throws error if username exists
            if(em.find(User.class, user.getUserName()) != null){
                throw new API_Exception("Username already exists",404);
            }
            //Makes sure roles is managed objects and checks that it exist
            for (int i = 0; i < user.getRoleList().size(); i++) {
                Role role = user.getRoleList().get(i);
                role = em.find(Role.class, role.getRoleName());
                if(role == null){
                    throw new NotFoundException("Role doesn't exist");
                }
            }
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

}
