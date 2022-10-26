package service;

import classes.Type;
import dto.Type.GetTypesResponse;
import lombok.NoArgsConstructor;
import repository.TypeRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class TypeService {
    private TypeRepository typeRepository;

    @Inject
    public TypeService(TypeRepository typeRepository){
        this.typeRepository = typeRepository;
    }

    public Optional<Type> find(String typeName) {
        return typeRepository.find(typeName);
    }

    public void create(Type type) {
        typeRepository.create(type);
    }

    public void update(Type type) {
        typeRepository.update(type);
    }

    public void delete(String name) {
        typeRepository.delete(typeRepository.find(name).orElseThrow());
    }

    public List<Type> findAll(){
        return typeRepository.findAll();
    }

    public void updateIcon(String typeName, InputStream is) {
        typeRepository.find(typeName).ifPresent(type -> {
            try {
                type.setIcon(is.readAllBytes());
                typeRepository.update(type);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public void deleteIcon(String typeName){
        typeRepository.find(typeName).ifPresent(type -> {
            type.setIcon(null);
            typeRepository.update(type);
        });
    }
}
