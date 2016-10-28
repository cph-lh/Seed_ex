package facades;

import security.IUserFacade;
import entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    public IUser addUser(User user)
    {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        //user.password(PasswordStorage.createHash(password));
        em.persist(user);
        em.getTransaction().commit();
        if (user != null)
        {
            em.close();
            return user;
        } else
        {
            return null;
        }
    }

    @Override
    public List<String> authenticateUser(String userName, String password)
    {
        IUser user = new User("", "");
        return user != null && user.getPassword().equals(password) ? user.getRolesAsStrings() : null;
    }

}
