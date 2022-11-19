package repository;

import classes.Type;
import datastore.DataStore;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class TypeRepository implements Repository<Type, String> {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Type> find(String typeName) {
        return Optional.ofNullable(em.find(Type.class, typeName));
    }

    @Override
    public List<Type> findAll() {
        return em.createQuery("select p from Type p", Type.class).getResultList();
    }

    @Override
    public void create(Type entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Type entity) {
        em.remove(em.find(Type.class, entity.getTypeName()));
    }

    @Override
    public void update(Type entity) {
        em.merge(entity);
    }

    @Override
    public void detach(Type entity){
        em.detach(entity);
    }

}
