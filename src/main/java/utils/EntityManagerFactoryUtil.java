package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryUtil {
    private EntityManagerFactory emf;
    private EntityManager em;

    public EntityManagerFactoryUtil() {
        emf = Persistence.createEntityManagerFactory("mssql-pu");
        em = emf.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void close() {
        em.close();
        emf.close();
    }
}
