package dtu.group2.Infrastructure.Repositories;

import dtu.group2.Domain.Entities.Payment;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;
import dtu.group2.Infrastructure.Repositories.Interfaces.IRepository;

import java.util.Collection;
import java.util.HashMap;

public class LocalPaymentRepository implements IRepository<Payment> {

    private final static HashMap<String, Payment> payments = new HashMap<>();
    private static int counter = 0;


    @Override
    public Payment get(String id) throws EntityNotFoundException, ArgumentNullException {
        if (id == null) throw new ArgumentNullException("Argument id cannot be null");
        var customer = payments.get(id);
        if (customer == null) throw new EntityNotFoundException("payment with id " + id + " is unknown");
        return customer;
    }

    @Override
    public Collection<Payment> getAll() {
        return payments.values();
    }

    @Override
    public Payment create(Payment entity) {
        entity.setId("pid" + (++counter));
        return payments.put(entity.getId(), entity);
    }

    @Override
    public Payment update(Payment entity) {
        return payments.replace(entity.getId(), entity);
    }

    @Override
    public Payment delete(String id) {
        return payments.remove(id);
    }
}
