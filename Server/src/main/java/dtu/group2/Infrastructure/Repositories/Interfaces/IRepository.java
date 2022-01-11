package dtu.group2.Infrastructure.Repositories.Interfaces;

import dtu.group2.Domain.Entities.Interfaces.IEntity;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;

import java.util.Collection;

public interface IRepository<T extends IEntity> {
    public T get(String id) throws EntityNotFoundException, ArgumentNullException;
    public Collection<T> getAll();
    public T create(T entity);
    public T update(T entity);
    public T delete(String id);


}
