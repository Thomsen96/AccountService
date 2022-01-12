package dtu.group2.Presentation.Resources.Interfaces;

import dtu.group2.Domain.Entities.Interfaces.IEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface IResource<T extends IEntity> {
    Response get(@QueryParam("id") String id);
    Response update(@QueryParam("entity") T entity);
    Response delete(@QueryParam("id") String id);
    Response create(@QueryParam("entity") T entity);
}
