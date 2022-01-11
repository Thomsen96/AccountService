package dtu.group2.Infrastructure.Repositories;
import dtu.group2.Application.Interfaces.ICustomerRepository;
import dtu.group2.Domain.Entities.Customer;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;
import dtu.group2.Infrastructure.Repositories.Interfaces.IRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class LocalCustomerIRepository implements ICustomerRepository, IRepository<Customer> {

  private static final List<Customer> accounts = new ArrayList<>();


  @Override
  public Customer Create(Customer account) {
    accounts.add(account);
    return account;
  }

  @Override
  public Customer Read(String id) {
    return accounts.stream().filter(account -> account.getId().equals(id)).findFirst().orElse(null);
  }

  @Override
  public Boolean Update(Customer account) {

    return accounts.stream().filter(acc -> acc.getId().equals(account.getId())).map(acc -> account).anyMatch(acc -> true);
  }

  @Override
  public Boolean Delete(String id) {
    for (Customer account : accounts) {
      if(account.getId().equals(id))
      {
        accounts.remove(account);
        return true;
      }
    }
    return false;
  }

  private static final HashMap<String, Customer> customers = new HashMap<>();

  @Override
  public Customer get(String id) throws EntityNotFoundException, ArgumentNullException {
    if (id == null) throw new ArgumentNullException("Argument id cannot be null");
    var customer = customers.get(id);
    if (customer == null) throw new EntityNotFoundException("customer with id " + id + " is unknown");
    return customer;
  }

  @Override
  public Collection<Customer> getAll() {
    return customers.values();
  }

  @Override
  public Customer create(Customer entity) {
    if (entity == null) throw new IllegalArgumentException("Argument customer cannot be null");
    customers.put(entity.getId(), entity);
    return entity;
  }

  @Override
  public Customer update(Customer entity) {
    return customers.replace(entity.getId(), entity);
  }

  @Override
  public Customer delete(String id) {
    return customers.remove(id);
  }
}
