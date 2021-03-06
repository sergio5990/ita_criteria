package by.it.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {
    private static EntityManagerFactory emFactory = null;

    public static EntityManager getEntityManager() {
        if (emFactory == null) {
            emFactory = Persistence.createEntityManagerFactory("by.it.test");
        }
        return emFactory.createEntityManager();
    }

    public static Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }


    public static void closeEMFactory() {
        emFactory.close();
    }
}