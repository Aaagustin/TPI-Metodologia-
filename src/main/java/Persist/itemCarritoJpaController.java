package Persist;

import Models.itemCarrito;
import Models.Carrito;
import Models.Producto;
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

public class itemCarritoJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public itemCarritoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public itemCarritoJpaController() {
        // Inicialización vinculada a tu Unidad de Persistencia TpiPU
        this.emf = Persistence.createEntityManagerFactory("TpiPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ==========================================
    //                MÉTODO: CREATE
    // ==========================================
    public void create(itemCarrito itemCarrito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            // Adjuntar la entidad Carrito si existe
            Carrito carrito = itemCarrito.getCarrito();
            if (carrito != null) {
                carrito = em.getReference(carrito.getClass(), carrito.getIdCarrito());
                itemCarrito.setCarrito(carrito);
            }
            
            // Adjuntar la entidad Producto si existe
            Producto producto = itemCarrito.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdProducto());
                itemCarrito.setProducto(producto);
            }
            
            em.persist(itemCarrito);
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
    public void edit(itemCarrito itemCarrito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            // Mantener consistencia de las referencias antes del merge
            Carrito carritoNew = itemCarrito.getCarrito();
            if (carritoNew != null) {
                carritoNew = em.getReference(carritoNew.getClass(), carritoNew.getIdCarrito());
                itemCarrito.setCarrito(carritoNew);
            }
            
            Producto productoNew = itemCarrito.getProducto();
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdProducto());
                itemCarrito.setProducto(productoNew);
            }
            
            itemCarrito = em.merge(itemCarrito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = itemCarrito.getIdItem();
                if (finditemCarrito(id) == null) {
                    throw new NonexistentEntityException("El ítem con el id " + id + " ya no existe.");
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
            itemCarrito itemCarrito;
            try {
                itemCarrito = em.getReference(itemCarrito.class, id);
                itemCarrito.getIdItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El ítem con el id " + id + " ya no existe.", enfe);
            }
            em.remove(itemCarrito);
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
    public List<itemCarrito> finditemCarritoEntities() {
        return finditemCarritoEntities(true, -1, -1);
    }

    public List<itemCarrito> finditemCarritoEntities(int maxResults, int firstResult) {
        return finditemCarritoEntities(false, maxResults, firstResult);
    }

    private List<itemCarrito> finditemCarritoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(itemCarrito.class));
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

    public itemCarrito finditemCarrito(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(itemCarrito.class, id);
        } finally {
            em.close();
        }
    }

    public int getitemCarritoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<itemCarrito> rt = cq.from(itemCarrito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}