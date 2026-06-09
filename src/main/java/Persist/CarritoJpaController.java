package Persist;

import Models.Carrito;
import Models.itemCarrito;
import Persist.exceptions.NonexistentEntityException;


// IMPORTS CAMBIADOS: Todos apuntando a javax.persistence
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

public class CarritoJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public CarritoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public CarritoJpaController() {
        // Inicialización vinculada a tu Unidad de Persistencia TpiPU
        this.emf = Persistence.createEntityManagerFactory("TpiPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ==========================================
    //                MÉTODO: CREATE
    // ==========================================
    public void create(Carrito carrito) {
        if (carrito.getItem() == null) {
            carrito.setItem(new ArrayList<itemCarrito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            List<itemCarrito> attachedItem = new ArrayList<itemCarrito>();
            for (itemCarrito itemitemCarritoToAttach : carrito.getItem()) {
                itemitemCarritoToAttach.setCarrito(carrito);
                attachedItem.add(itemitemCarritoToAttach);
            }
            carrito.setItem(attachedItem);
            
            em.persist(carrito);
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
    public void edit(Carrito carrito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Carrito persistentCarrito = em.find(Carrito.class, carrito.getIdCarrito());
            List<itemCarrito> itemNew = carrito.getItem();

            List<itemCarrito> attachedItemNew = new ArrayList<itemCarrito>();
            for (itemCarrito itemNewItemCarritoToAttach : itemNew) {
                itemNewItemCarritoToAttach.setCarrito(carrito);
                attachedItemNew.add(itemNewItemCarritoToAttach);
            }
            itemNew = attachedItemNew;
            carrito.setItem(itemNew);

            carrito = em.merge(carrito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = carrito.getIdCarrito();
                if (findCarrito(id) == null) {
                    throw new NonexistentEntityException("El carrito con el id " + id + " ya no existe.");
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
    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrito carrito;
            try {
                carrito = em.getReference(Carrito.class, id);
                carrito.getIdCarrito();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El carrito con el id " + id + " ya no existe.", enfe);
            }
            em.remove(carrito);
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
    public List<Carrito> findCarritoEntities() {
        return findCarritoEntities(true, -1, -1);
    }

    public List<Carrito> findCarritoEntities(int maxResults, int firstResult) {
        return findCarritoEntities(false, maxResults, firstResult);
    }

    private List<Carrito> findCarritoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carrito.class));
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

    public Carrito findCarrito(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrito.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarritoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carrito> rt = cq.from(Carrito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}