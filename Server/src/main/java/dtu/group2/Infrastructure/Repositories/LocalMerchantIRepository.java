package dtu.group2.Infrastructure.Repositories;

import dtu.group2.Domain.Entities.Merchant;
import dtu.group2.Infrastructure.Repositories.Exceptions.ArgumentNullException;
import dtu.group2.Infrastructure.Repositories.Exceptions.EntityNotFoundException;
import dtu.group2.Infrastructure.Repositories.Interfaces.IRepository;

import java.util.Collection;
import java.util.HashMap;

public class LocalMerchantIRepository implements IRepository<Merchant> {

    private final static HashMap<String, Merchant> merchants = new HashMap<>();



    @Override
    public Merchant get(String id) throws ArgumentNullException, EntityNotFoundException {
        if (id == null) throw new ArgumentNullException("Argument id cannot be null");
        var merchant = merchants.get(id);
        if (merchant == null) throw new EntityNotFoundException("merchant with id " + id + " is unknown");
        return merchant;
    }

    @Override
    public Collection<Merchant> getAll() {
        return merchants.values();
    }

    @Override
    public Merchant create(Merchant entity) {
        return merchants.put(entity.getId(), entity);
    }

    @Override
    public Merchant update(Merchant entity) {
        return merchants.replace(entity.getId(), entity);
    }

    @Override
    public Merchant delete(String id) {
        return merchants.remove(id);
    }
}
