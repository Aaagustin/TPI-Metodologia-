package Persist;

import Models.Cliente;
import Models.Direccion;
import Persist.exceptions.NonexistentEntityException;

// IMPORTS: Todos unificados bajo el paquete javax
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

public class ClienteJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ClienteJpaController() {
        // Inicialización vinculada a tu Unidad de Persistencia TpiPU
        this.emf = Persistence.createEntityManagerFactory("TpiPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ==========================================
    //                MÉTODO: CREATE
    // ==========================================
    public void create(Cliente cliente) {
        if (cliente.getListaDirecciones() == null) {
            cliente.setListaDirecciones(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            // Vincular de forma bidireccional las direcciones antes de persistir
            List<Direccion> attachedListaDirecciones = new ArrayList<Direccion>();
            for (Direccion listaDireccionesDireccionToAttach : cliente.getListaDirecciones()) {
                listaDireccionesDireccionToAttach.setCli(cliente);
                attachedListaDirecciones.add(listaDireccionesDireccionToAttach);
            }
            cliente.setListaDirecciones(attachedListaDirecciones);
            
            em.persist(cliente);
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
    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<Direccion> listaDireccionesNew = cliente.getListaDirecciones();

            List<Direccion> attachedListaDireccionesNew = new ArrayList<Direccion>();
            for (Direccion listaDireccionesNewDireccionToAttach : listaDireccionesNew) {
                listaDireccionesNewDireccionToAttach.setCli(cliente);
                attachedListaDireccionesNew.add(listaDireccionesNewDireccionToAttach);
            }
            listaDireccionesNew = attachedListaDireccionesNew;
            cliente.setListaDirecciones(listaDireccionesNew);

            // Al ejecutar merge, gracias a "orphanRemoval = true" en la entidad Cliente,
            // las direcciones removidas de la lista se borrarán automáticamente de la DB.
            cliente = em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("El cliente con el id " + id + " ya no existe.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El cliente con el id " + id + " ya no existe.", enfe);
            }
            
            // Al eliminar el cliente, CascadeType.ALL eliminará sus direcciones asociadas
            em.remove(cliente);
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
    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}