package dtu.group2.Presentation.Resources;

import dtu.group2.Domain.Entities.Customer;
import dtu.group2.Domain.Entities.Merchant;
import dtu.group2.Domain.Entities.Payment;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;
import dtu.group2.Infrastructure.Repositories.Interfaces.IRepository;
import dtu.group2.Infrastructure.Repositories.LocalCustomerIRepository;
import dtu.group2.Infrastructure.Repositories.LocalMerchantIRepository;
import dtu.group2.Infrastructure.Repositories.LocalPaymentRepository;
import dtu.group2.Presentation.Resources.Interfaces.IResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchants")
public class MerchantResource implements IResource<Merchant> {

    private final IRepository<Customer> customerRepository = new LocalCustomerIRepository();
    private final IRepository<Merchant> merchantRepository = new LocalMerchantIRepository();
    private final IRepository<Payment> paymentRepository = new LocalPaymentRepository();



    @Override
    @GET
    public Response get(@QueryParam("id") String id) {
        try {
            return Response.status(Response.Status.OK).entity(merchantRepository.get(id)).build();
        } catch (ArgumentNullException e) {
            return Response.status(Response.Status.OK).entity(merchantRepository.getAll()).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Merchant entity) {
        return Response.status(Response.Status.OK).entity(merchantRepository.update(entity)).build();
    }

    @Override
    @DELETE
    public Response delete(@QueryParam("id")String id) {
        return Response.status(Response.Status.OK).entity(merchantRepository.delete(id)).build();
    }

    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Merchant entity) {
        return Response.status(Response.Status.CREATED).entity(merchantRepository.create(entity)).build();
    }
}
