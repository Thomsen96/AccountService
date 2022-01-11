package dtu.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SimpleDtuService {

  public static final String HTTP_LOCALHOST_8080 = "http://localhost:8080";
  public static final String HTTP_DOCKER_LOCALHOST_8181 = "http://dtu_server_cont:8181";
  public static String HTTP_CHOSEN_HOST_AND_PORT = HTTP_LOCALHOST_8080;
  private final Client client = ClientBuilder.newClient();

  public SimpleDtuService(){
    // For CI in jenkins, we need to provide a Docker specific host:port combination
    if ("True".equals(System.getenv("IN_DOCKER_ENV"))){
      HTTP_CHOSEN_HOST_AND_PORT = HTTP_DOCKER_LOCALHOST_8181;
      System.out.println("Running in a Dockerfile has been detected. Changed the host and port.");
    }

  }

  public Response pay(int amount, String cid, String mid) {
    var payment = new Payment(amount, cid, mid);

    //System.out.println(res.getStatus());
    return client
        .target(HTTP_CHOSEN_HOST_AND_PORT)
        .path("payments")
        .request(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .post(Entity.json(payment));
  }

  public Response getPayments() {
    return client.target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("payments")
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .get();
  }

  public Response createCustomer(String id, String balance) {
    return client
            .target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("accounts")
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.json(new Customer(id, balance)));
  }

  public Response createMerchant(String id, String balance) {
    return client
            .target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("merchants")
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.json(new Merchant(id, balance)));
  }


  public Response removeCustomer(String cid1) {
    return client.target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("accounts")
            .queryParam("id", cid1)
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .delete();
  }

  public Response removeMerchant(String mid1) {
    return client.target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("merchants")
            .queryParam("id", mid1)
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .delete();
  }

  public Response getCustomer(String cid) {
    return client.target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("accounts")
            .queryParam("id", cid)
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .get();
  }

  public Response getMerchant(String mid) {
    return client.target(HTTP_CHOSEN_HOST_AND_PORT)
            .path("merchants")
            .queryParam("id", mid)
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .get();
  }
}
