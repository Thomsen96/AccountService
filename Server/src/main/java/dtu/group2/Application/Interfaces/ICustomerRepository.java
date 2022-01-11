package dtu.group2.Application.Interfaces;

import dtu.group2.Domain.Entities.Customer;

public interface ICustomerRepository {
  public Customer Create(Customer account);
  public Customer Read(String id);
  public Boolean Update(Customer account);
  public Boolean Delete(String id);
}
