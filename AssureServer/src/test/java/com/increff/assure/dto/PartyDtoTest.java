package com.increff.assure.dto;

import model.PartyType;
import model.form.PartyForm;
import com.increff.assure.pojo.PartyPojo;
import com.increff.assure.service.PartyService;
import com.increff.assure.service.ApiException;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class PartyDtoTest extends AbstractUnitTest {

    @Autowired
    private  PartyDto partyDto;

    @Autowired
    private PartyService partyService;

    private PartyForm partyForm1;
    private PartyPojo partyPojo1;
    private PartyPojo partyPojo2;
    private PartyPojo partyPojo3;// Todo should not be declared

    @Before
    public void init() throws Exception {
        partyForm1 = DataUtil.createPartyForm("   client1  ", PartyType.CLIENT);
        partyPojo1 = DataUtil.createPartyPojo("   client2  ", PartyType.CLIENT);
        partyPojo2 = DataUtil.createPartyPojo("   customer1  ", PartyType.CUSTOMER);
        partyPojo3 = DataUtil.createPartyPojo("   customer2  ", PartyType.CUSTOMER);// these creations should not be here
    }

    @Test
    public void testAdd() throws ApiException {
        partyDto.add(partyForm1);
        assertEquals(1, partyService.getAll().size());
        PartyPojo returned= partyService.getAll().get(0);
        assertEquals(partyForm1.getType(),returned.getType());
        assertEquals(partyForm1.getName().trim().toLowerCase(),returned.getName());
    }

    @Test(expected = ApiException.class)
    public void testAddInvalid() throws ApiException {
        partyDto.add(partyForm1);
        partyDto.add(partyForm1);
    }

    @Test
    public void testGet() throws ApiException {
        partyService.add(partyPojo1);
        assertEquals("client2", (partyDto.get(partyPojo1.getId()).getName()).trim().toLowerCase());
        assertEquals(PartyType.CLIENT, partyDto.get(partyPojo1.getId()).getType());
        assertEquals(1, partyDto.getAll().size());
    }

    @Test
    public void testGetAll() throws ApiException {
        partyService.add(partyPojo1);
        partyService.add(partyPojo2);
        partyService.add(partyPojo3);
        assertEquals(3, partyDto.getAll().size());
    }

    @Test
    public void testGetAllClients() throws ApiException {
        partyService.add(partyPojo1);
        partyService.add(partyPojo2);
        partyService.add(partyPojo3);
        assertEquals(1, partyDto.getAllClients().size());
    }

    @Test
    public void testGetAllCustomers() throws ApiException {
        partyService.add(partyPojo1);
        partyService.add(partyPojo2);
        partyService.add(partyPojo3);
        assertEquals(2, partyDto.getAllCustomers().size());
    }

}
