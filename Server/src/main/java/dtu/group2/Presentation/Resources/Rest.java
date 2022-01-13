package dtu.group2.Presentation.Resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtu.group2.Application.AccountServiceServer;
import dtu.ws.fastmoney.BankServiceException_Exception;


@Path("/account")
public class Rest {
	
	private static AccountServiceServer service = new AccountServiceServer();

	@POST
	@Path("/customer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@PathParam("customerId") String customerId) {
		try {
			return Response.status(Response.Status.CREATED).entity(service.createCustomer(customerId)).build();
		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.CREATED).entity(false).build();
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

