package com.increff.assure.dto;

import com.increff.assure.dao.PartyDao;
import com.increff.assure.dao.ProductDao;
import com.increff.assure.service.ApiException;
import model.PartyType;
import model.data.ProductData;
import model.form.ProductForm;
import model.form.ProductUpdateForm;
import com.increff.assure.pojo.PartyPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ProductService;
import com.increff.assure.util.AbstractUnitTest;
import com.increff.assure.util.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static com.increff.assure.util.ConvertUtil.convert;

public class ProductDtoTest extends AbstractUnitTest{

    @Autowired
    ProductDto productDto;

    @Autowired
    ProductService productService;

    @Autowired
    PartyDao partyDao;

    @Autowired
    ProductDao productDao;

    private PartyPojo partyPojo;
    private List<ProductForm> productFormList;
    private List<ProductForm> productFormListInvalid;

    @Before
    public void init()
    {
        productFormList =new ArrayList<>();
        productFormListInvalid = new ArrayList<>();
        partyPojo = DataUtil.createPartyPojo("client1", PartyType.CLIENT);
        partyDao.insert(partyPojo);
        ProductForm productForm1=DataUtil.createProductForm("name1","brand1","clientSku1","desc1",99.4445);
        productFormList.add(productForm1);
        productFormListInvalid.add(productForm1);
        productForm1=DataUtil.createProductForm("name2","brand2","clientSku2","desc2",99.45);
        productFormList.add(productForm1);
        productForm1=DataUtil.createProductForm("name2","brand2","clientSku1","desc2",99.45);
        productFormListInvalid.add(productForm1);
    }

    @Test
    public void testAddValid() throws Exception {
        productDto.addProducts(productFormList, partyPojo.getId());
        List<ProductPojo> productPojoList= productService.getAll();
        assertEquals(productFormList.size(),productPojoList.size());
        ProductForm productForm1= productFormList.get(0);
        ProductPojo productPojo1=productPojoList.get(0);
        assertEquals(productForm1.getClientSkuId(),productPojo1.getClientSkuId());
        assertEquals(productForm1.getBrandId(),productPojo1.getBrandId());
        assertEquals(productForm1.getDescription(),productPojo1.getDescription());
        assertEquals(productForm1.getMrp(),productPojo1.getMrp(),0.01);
        assertEquals(productForm1.getName(),productPojo1.getName());
        productPojo1=productPojoList.get(1);
        productForm1= productFormList.get(1);
        assertEquals(productForm1.getClientSkuId(),productPojo1.getClientSkuId());
        assertEquals(productForm1.getBrandId(),productPojo1.getBrandId());
        assertEquals(productForm1.getDescription(),productPojo1.getDescription());
        assertEquals(productForm1.getMrp(),productPojo1.getMrp(),0.01);
        assertEquals(productForm1.getName(),productPojo1.getName());
    }

    @Test(expected = ApiException.class)
    public void testAddInvalid1() throws ApiException {
        productDto.addProducts(productFormList, partyPojo.getId());
        productDto.addProducts(productFormList, partyPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void testAddInvalid2() throws ApiException {
        productDto.addProducts(productFormListInvalid, partyPojo.getId());
    }

    @Test
    public void testCheckClientIdAndClientSkuInvalid() throws ApiException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ProductPojo productPojo=convert(productFormList.get(0),ProductPojo.class);
        productPojo.setClientId(partyPojo.getId());
        productPojo.setClientSkuId("clientSku3");
    }

    @Test
    public void testGet() throws Exception {
        ProductForm productForm= productFormList.get(0);
        ProductPojo productPojo=convert(productForm,ProductPojo.class);
        productPojo.setClientId(partyPojo.getId());
        List<ProductPojo> productPojoList=new ArrayList<>();
        productPojoList.add(productPojo);
        productService.add(productPojo);
        ProductData productData= productDto.get(productPojoList.get(0).getId());
        assertEquals(productPojo.getBrandId(),productData.getBrandId());
        assertEquals(productPojo.getClientSkuId(),productData.getClientSkuId());
        assertEquals(productPojo.getMrp(),productData.getMrp(),0.001);
        assertEquals(productPojo.getDescription(),productData.getDescription());
    }

    @Test
    public void testGetAll() throws Exception {
        ProductPojo productPojo=convert(productFormList.get(0),ProductPojo.class);
        List<ProductPojo> productPojoList=new ArrayList<>();
        productPojo.setClientId(partyPojo.getId());
        productPojoList.add(productPojo);
        productService.add(productPojo);
        List<ProductData> productDataList= productDto.getAll();
        assertEquals(1,productDataList.size());
    }

    @Test
    public void testUpdate() throws Exception {
        ProductPojo productPojo=convert(productFormList.get(0),ProductPojo.class);
        List<ProductPojo> productPojoList=new ArrayList<>();
        productPojo.setClientId(partyPojo.getId());
        productPojoList.add(productPojo);
        productService.add(productPojo);
        ProductUpdateForm updateProductForm=convert(productFormList.get(0),ProductUpdateForm.class);
        updateProductForm.setMrp(100.10);
        updateProductForm.setDescription("updatedDesc");
        productDto.update(productPojo.getId(),updateProductForm);
        ProductPojo returned= productService.getByClientAndClientSku(productPojo.getClientId(),productPojo.getClientSkuId());
        assertEquals(updateProductForm.getDescription().toLowerCase().trim(),returned.getDescription());
        assertEquals(updateProductForm.getMrp(),returned.getMrp(),0.001);
    }

}
