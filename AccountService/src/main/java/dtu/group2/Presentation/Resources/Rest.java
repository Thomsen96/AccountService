package dtu.group2.Presentation.Resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtu.group2.Application.AccountService;
import dtu.group2.Domain.Entities.CustomerCreationRequest;
import dtu.group2.Repositories.CustomerRepository;
import dtu.group2.Repositories.MerchantRepository;
import dtu.ws.fastmoney.BankServiceException_Exception;


@Path("/customers")
public class Rest {
	
	private static AccountService service = new AccountService(new CustomerRepository(), new MerchantRepository());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(CustomerCreationRequest CreationRequest) {
		try {
			service.createCustomer(CreationRequest.getAccountNumber());
			return Response.status(Response.Status.OK).entity(CreationRequest).build();
		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/customer/{customerId}")
	public Response get(@PathParam("customerId") String customerId) {
		return Response.status(Response.Status.ACCEPTED).entity(service.getCustomer(customerId)).build();
	}

	@DELETE
	@Path("{customerId}")
	public Response delete(@PathParam("customerId") String customerId) {
		service.deleteCustomer(customerId);
		
		if(service.getCustomer(customerId) == null) {
			return Response.status(Response.Status.OK).entity(true).build();	
		}
		
		return Response.status(Response.Status.BAD_REQUEST).entity(false).build();
		
	}
}

