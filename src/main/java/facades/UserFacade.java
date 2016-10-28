package facades;

import security.IUserFacade;
import entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade
{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("SEED_PU");

    /*When implementing your own database for this seed, you should NOT touch any of the classes in the security folder
    Make sure your new facade implements IUserFacade and keeps the name UserFacade, and that your Entity User class implements 
    IUser interface, then security should work "out of the box" with users and roles stored in your database */
//  private final  Map<String, IUser> users = new HashMap<>();
    public UserFacade()
    {
//    //Test Users
//    User user = new User("user","test");
//    user.addRole("User");
//    users.put(user.getUserName(),user );
//    User admin = new User("admin","test");
//    admin.addRole("Admin");
//    users.put(admin.getUserName(),admin);
//    
//    User both = new User("user_admin","test");
//    both.addRole("User");
//    both.addRole("Admin");
//    users.put(both.getUserName(),both );
    }

    @Override
    public IUser getUserByUserId(String id)
    {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User u = em.find(User.class, id);
        em.getTransaction().commit();
        if (u != null)
        {
            em.close();
            return u;
        } else
        {
            return null;
        }
    }

    @Override
    public IUser getUserByUserName(String userName)
    {
        EntityManager em = emf.createEntityManager();
        TypedQuery tq = em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class);
        tq.setParameter("name", userName);
        return (User) tq.getSingleResult();
    }

    @Override
    public IUser addUser(User user)
    {
        try
        {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
            em.persist(user);
            em.getTransaction().commit();
            em.close();
        } catch (PasswordStorage.CannotPerformOperationException ex)
        {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    @Override
    public List<String> authenticateUser(String userName, String password)
    {
        try
        {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            IUser user = getUserByUserName(userName);
            String correctPW = user.getPassword();
            boolean pw = PasswordStorage.verifyPassword(password, correctPW);
            if (pw)
            {
                return user.getRolesAsStrings();
            }
        } catch (PasswordStorage.CannotPerformOperationException ex)
        {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PasswordStorage.InvalidHashException ex)
        {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
