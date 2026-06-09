package Persist;

import Models.Direccion;
import Models.Cliente;
import Persist.exceptions.NonexistentEntityException;


// IMPORTS: Todos unificados bajo javax.persistence
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DireccionJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public DireccionJpaController() {
        // Inicialización vinculada a tu Unidad de Persistencia TpiPU
        this.emf = Persistence.createEntityManagerFactory("TpiPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ==========================================
    //                MÉTODO: CREATE
    // ==========================================
    public void create(Direccion direccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            // Verifica si tiene un cliente asociado para mantener la consistencia en memoria
            Cliente cli = direccion.getCli();
            if (cli != null) {
                cli = em.getReference(cli.getClass(), cli.getIdCliente());
                direccion.setCli(cli);
            }
            
            em.persist(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // ==========================================
    //                 MÉTODO: EDIT
    // ==========================================
    public void edit(Direccion direccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            // Manejo de la actualización del objeto referenciado
            Cliente cliNew = direccion.getCli();
            if (cliNew != null) {
                cliNew = em.getReference(cliNew.getClass(), cliNew.getIdCliente());
                direccion.setCli(cliNew);
            }
            
            direccion = em.merge(direccion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = direccion.getIdDireccion();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("La dirección con el id " + id + " ya no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // ==========================================
    //               MÉTODO: DESTROY
    // ==========================================
    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getIdDireccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La dirección con el id " + id + " ya no existe.", enfe);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // ==========================================
    //             MÉTODOS DE CONSULTA
    // ==========================================
    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Direccion findDireccion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}