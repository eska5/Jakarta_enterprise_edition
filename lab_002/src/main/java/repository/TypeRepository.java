package repository;

import classes.Type;
import datastore.DataStore;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Dependent
public class TypeRepository implements Repository<Type, String> {
    private DataStore store;

    @Inject
    public TypeRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Type> find(String typeName) {
        return store.findType(typeName);
    }

    @Override
    public List<Type> findAll() {
        return store.findAllTypes();
    }

    @Override
    public void create(Type entity) {
        store.createType(entity);
    }

    @Override
    public void delete(Type entity) {
        store.deleteType(entity.getTypeName());
    }

    @Override
    public void update(Type entity) {
        store.updateType(entity);
    }

}
