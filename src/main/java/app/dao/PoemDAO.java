package app.dao;


import app.entities.Poem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;
import app.config.HibernateConfig;

public class PoemDAO {

    private final EntityManagerFactory emf;

    private static PoemDAO instance;

    private PoemDAO() {
        // *** HER ER RETTELSEN ***
        // Metodenavnet er ændret til at matche den nye HibernateConfig-fil.
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    public static PoemDAO getInstance() {
        if (instance == null) {
            instance = new PoemDAO();
        }
        return instance;
    }

    public List<Poem> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Poem> query = em.createQuery("SELECT p FROM Poem p", Poem.class);
            return query.getResultList();
        }
    }

    public Poem getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Poem.class, id);
        }
    }

    public Poem create(Poem poem) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(poem);
            em.getTransaction().commit();
            return poem;
        }
    }

    public Poem update(Poem poem) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Poem updatedPoem = em.merge(poem);
            em.getTransaction().commit();
            return updatedPoem;
        }
    }

    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Poem poem = getById(id);
            if (poem != null) {
                em.remove(em.merge(poem));
            }
            em.getTransaction().commit();
        }
    }
}
