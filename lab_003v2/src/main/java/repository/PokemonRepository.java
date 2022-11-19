package repository;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import datastore.DataStore;
import dto.Pokemon.UpdatePokemonRequest;
import serialization.CloningUtility;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
public class PokemonRepository implements Repository<Pokemon, String> {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Pokemon> find(String name) {
        return Optional.ofNullable(em.find(Pokemon.class, name));
    }

    @Override
    public List<Pokemon> findAll() {
        return em.createQuery("select c from Pokemon c", Pokemon.class).getResultList();
    }

    @Override
    public void create(Pokemon entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Pokemon entity) {
        em.remove(em.find(Pokemon.class, entity.getName()));
    }

    @Override
    public void update(Pokemon entity) {
        em.merge(entity);
    }

    @Override
    public void detach(Pokemon entity) {
        em.detach(entity);
    }

    public Optional<Pokemon> findByVarAndType(String name, Type type) {
        try {
            return Optional.of(em.createQuery("select c from Pokemon c where c.name = :name and c.type = :type", Pokemon.class)
                    .setParameter("type", type)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    public List<Pokemon> findAllByType(Type type) {
        return em.createQuery("select c from Pokemon c where c.type = :type", Pokemon.class)
                .setParameter("type",type)
                .getResultList();
    }
}
