package com.increff.assure.dto;

import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.dao.PartyDao;
import com.increff.assure.dao.ProductDao;
import model.PartyType;
import com.increff.assure.pojo.PartyPojo;
import com.increff.assure.service.ApiException;
import model.form.BinSkuForm;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BinDtoTest extends AbstractUnitTest {
    @Autowired
    private BinDto binDto;

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private ProductDao productDao;

    private List<BinSkuForm> formList;
    private PartyPojo consumerPojo;
    private ProductPojo productPojo;
    private BinSkuPojo binSkuPojo;

    @Before
    public void init() {
        formList = new ArrayList<>();
        binSkuPojo = new BinSkuPojo();
        binSkuDao.insert(binSkuPojo);
        consumerPojo = DataUtil.createPartyPojo("client1", PartyType.CLIENT);
        partyDao.insert(consumerPojo);
        productPojo = DataUtil.createProductPojo("name1", "brand1", "clientSku1", "desc1", 999.99, consumerPojo.getId());
        productDao.insert(productPojo);
        BinSkuForm inventory = DataUtil.createBinSkuForm(binSkuPojo.getBinId(), productPojo.getId(), 10L);
        formList.add(inventory);
    }

//    @Test
//    public void testCreateBin() {
//        binDto.add(1);
//        Long actual = 1L + 1L;
//        Long expected = (long) binDao.selectAll().size();
//        assertEquals(actual, expected);
//    }

    @Test
    public void testUploadInventoryForm() throws ApiException {
        ProductPojo product2 = DataUtil.createProductPojo("name2","brand2","clientSku2","desc2",99.99, consumerPojo.getId());
        productDao.insert(product2);
        BinSkuForm inventory = DataUtil.createBinSkuForm(binSkuPojo.getBinId(), product2.getId(), 10L);
        formList.add(inventory);
        binDto.addBinWiseInventory(formList);
        assertEquals(formList.size(), binSkuDao.selectAll().size());
        BinSkuPojo returned = binSkuDao.selectAll().get(0);
        assertEquals(productPojo.getId(), returned.getGlobalSkuId());
        assertEquals(formList.get(0).getQuantity(), returned.getQuantity());
        assertEquals(formList.get(0).getBinId(), returned.getBinId());
        returned = binSkuDao.selectAll().get(1);
        assertEquals(product2.getId(), returned.getGlobalSkuId());
        assertEquals(formList.get(0).getQuantity(), returned.getQuantity());
        assertEquals(formList.get(0).getBinId(), returned.getBinId());
    }

    @Test(expected = ApiException.class)
    public void testAddOrUploadInventoryInvalid() throws ApiException {
        binDto.addBinWiseInventory(formList);
    }

    @Test(expected = ApiException.class)
    public void testUploadInventoryInvalid() throws ApiException {
        formList.get(0).setBinId(1000101L);
        binDto.addBinWiseInventory(formList);
    }

}
