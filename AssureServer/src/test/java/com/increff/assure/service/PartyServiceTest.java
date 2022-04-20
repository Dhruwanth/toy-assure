package com.increff.assure.service;

import com.increff.assure.dao.PartyDao;
import model.PartyType;
import com.increff.assure.pojo.PartyPojo;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PartyServiceTest extends AbstractUnitTest {

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyDao partyDao;

    private PartyPojo partyPojo;

    @Before
    public void init()
    {
        partyPojo = DataUtil.createPartyPojo("client1", PartyType.CLIENT);
        partyDao.insert(partyPojo);

    }

    @Test(expected= ApiException.class)
    public void testAddInvalid() throws  ApiException {
        PartyPojo partyPojo2=DataUtil.createPartyPojo("client1",PartyType.CLIENT);
        partyService.add(partyPojo2);
    }

    @Test
    public void testAddValid() throws ApiException {
        PartyPojo partyPojo2=DataUtil.createPartyPojo("customer1",PartyType.CLIENT);
        partyService.add(partyPojo2);
        assertEquals( 2, partyDao.selectAll().size());
        List<PartyPojo> pojoList= partyDao.selectAll();
        PartyPojo partyPojoReturned=pojoList.get(0);
        assertEquals(partyPojo.getName(),partyPojoReturned.getName());
        assertEquals(partyPojo.getType(),partyPojoReturned.getType());
        partyPojoReturned=pojoList.get(1);
        assertEquals(partyPojo2.getName(),partyPojoReturned.getName());
        assertEquals(partyPojo2.getType(),partyPojoReturned.getType());
    }

    @Test
    public void testGetAll()
    {
        PartyPojo partyPojo2=DataUtil.createPartyPojo("customer1",PartyType.CUSTOMER);
        partyDao.insert(partyPojo2);
        assertEquals(2, partyService.getAll().size());
    }

    @Test
    public void testGetAllByType()
    {
        assertEquals(1, partyService.getAll(PartyType.CLIENT).size());
    }

}
