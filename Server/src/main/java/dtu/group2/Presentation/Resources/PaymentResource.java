package dtu.group2.Presentation.Resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtu.group2.Domain.Entities.Customer;
import dtu.group2.Domain.Entities.Merchant;
import dtu.group2.Domain.Entities.Payment;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;
import dtu.group2.Infrastructure.Repositories.LocalCustomerIRepository;
import dtu.group2.Infrastructure.Repositories.LocalMerchantIRepository;
import dtu.group2.Infrastructure.Repositories.LocalPaymentRepository;
import dtu.group2.Presentation.Resources.Interfaces.IResource;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Path("/payments")
public class PaymentResource implements IResource<Payment> {

    private final LocalCustomerIRepository customerRepository = new LocalCustomerIRepository();
    private final LocalMerchantIRepository merchantRepository = new LocalMerchantIRepository();
    private final LocalPaymentRepository paymentRepository = new LocalPaymentRepository();

    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Payment payment) {
        Customer account = null;
        Merchant merchant1 = null;
        try {
            account = customerRepository.get(payment.cid);
            merchant1 = merchantRepository.get(payment.mid);
            var newBalance = account.getBalance().subtract(new BigDecimal(String.valueOf(payment.amount)));
            account.balance(newBalance.toString());
            merchant1.setBalance(merchant1.getBalance().add(new BigDecimal(String.valueOf(payment.amount))));
            paymentRepository.create(payment);
            return Response.noContent().build();
        } catch (EntityNotFoundException | ArgumentNullException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Override
    @GET
    public Response get(@QueryParam("id") String id) {
        try {
            return Response.status(Response.Status.OK).entity(paymentRepository.get(id)).build();
        } catch (ArgumentNullException e) {
            return Response.status(Response.Status.OK).entity(paymentRepository.getAll()).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Override
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Payment entity) {
        return Response.status(Response.Status.OK).entity(paymentRepository.update(entity)).build();
    }

    @Override
    @DELETE
    public Response delete(@QueryParam("id")String id) {
        return Response.status(Response.Status.OK).entity(paymentRepository.delete(id)).build();
    }


}
