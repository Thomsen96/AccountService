package dtu.group2.Presentation.Resources;

import dtu.group2.Domain.Entities.Customer;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;
import dtu.group2.Infrastructure.Repositories.LocalCustomerIRepository;
import dtu.group2.Infrastructure.Repositories.LocalMerchantIRepository;
import dtu.group2.Infrastructure.Repositories.LocalPaymentRepository;
import dtu.group2.Presentation.Resources.Interfaces.IResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class CustomerResource implements IResource<Customer> {

    private static final LocalCustomerIRepository customerRepository = new LocalCustomerIRepository();
    private final LocalMerchantIRepository merchantRepository = new LocalMerchantIRepository();
    private final LocalPaymentRepository paymentRepository = new LocalPaymentRepository();



    @Override
    @GET
    public Response get(@QueryParam("id") String id) {
        System.out.println(id);
        try {
            System.out.println(customerRepository.get(id));
            return Response.status(Response.Status.OK).entity(customerRepository.get(id)).build();
        } catch (ArgumentNullException e) {
            return Response.status(Response.Status.OK).entity(customerRepository.getAll()).build();
        } catch (EntityNotFoundException e) {
            System.out.println(id);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Customer entity) {
        return Response.status(Response.Status.OK).entity(customerRepository.update(entity)).build();
    }

    @Override
    @DELETE
    public Response delete(@QueryParam("id")String id) {
        return Response.status(Response.Status.OK).entity(customerRepository.delete(id)).build();
    }

    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Customer entity) {
        return Response.status(Response.Status.CREATED).entity(customerRepository.create(entity)).build();
    }
}
