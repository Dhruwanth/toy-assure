package com.increff.assure.service;

import com.increff.assure.dao.PartyDao;
import com.increff.assure.pojo.PartyPojo;
import model.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartyService extends AbstractService {
    @Autowired
    private PartyDao partyDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(PartyPojo partyPojo) throws ApiException {
        checkIfPartyExists(partyPojo);
        partyDao.insert(partyPojo);
    }

    private void checkIfPartyExists(PartyPojo partyPojo) throws ApiException {
        checkNull(partyDao.selectByNameAndType(partyPojo.getName(), partyPojo.getType()), partyPojo.getName() + " already exists.");
    }
//if exists already return the same entity back
    public PartyPojo getCheckId(Long id) throws ApiException {
        PartyPojo consumer = partyDao.select(id);
        checkNotNull(consumer, "Consumer (ID:" + id + ") does not exist.");
        return consumer;
    }

    public void getCheckClient(Long id) throws ApiException {
        if (!getCheckId(id).getType().equals(PartyType.CLIENT))
            throw new ApiException("Invalid Client");
    }

    public void getCheckCustomer(Long id) throws ApiException {
        if (!getCheckId(id).getType().equals(PartyType.CUSTOMER))
            throw new ApiException("Invalid Customer");
    }

    public List<PartyPojo> getAll() {
        return partyDao.selectAll();
    }

    public List<PartyPojo> getAll(PartyType type) {
        return partyDao.selectAll(type);
    }

    public String getName(Long clientId) throws ApiException {
        return getCheckId(clientId).getName();
    }
}