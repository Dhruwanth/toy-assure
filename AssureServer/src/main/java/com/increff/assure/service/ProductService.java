package com.increff.assure.service;

import com.increff.assure.dao.ProductDao;
import com.increff.assure.pojo.ProductPojo;
import model.form.ProductUpdateForm;
import model.data.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService extends AbstractService {
    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ProductPojo product) throws ApiException {
        checkIfProductExists(product.getClientId(), product.getClientSkuId());
        productDao.insert(product);
    }

    private void checkIfProductExists(Long clientId, String clientSkuId) throws ApiException {
        checkNull(productDao.selectByClientIdAndClientSku(clientId, clientSkuId), "Duplicate Product present");
    }

    public ProductPojo getCheckId(Long id) throws ApiException {
        ProductPojo product = productDao.select(id);
        checkNotNull(product, "Product (ID:" + id + ") does not exist.");
        return product;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addList(List<ProductPojo> productList) throws ApiException {
        for (ProductPojo pojo : productList)
            add(pojo);
    }

    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }
    
    @Transactional(rollbackFor = ApiException.class)
    public void update(Long clientId, String clientSkuId, ProductUpdateForm productForm) throws ApiException {
        copySourceToDestination(productDao.selectByClientIdAndClientSku(clientId, clientSkuId), productForm);
    }

    public Long getClientIdOfProduct(Long globalSkuId) throws ApiException {
        return getCheckId(globalSkuId).getClientId();
    }

    public ProductPojo getByClientAndClientSku(Long clientId, String clientSkuId) throws ApiException {
        ProductPojo exists = productDao.selectByClientIdAndClientSku(clientId, clientSkuId);
        checkNotNull(exists, "Product with ClientID:"+clientId+" ClientSKUId:"+clientSkuId+" doesn't exist.");
        return exists;
    }

    public List<ProductPojo> getByClientSku(String clientSkuId) {
        return productDao.selectByClientSku(clientSkuId);
    }

    public List<ProductPojo> getByClientId(Long clientId) {
        return productDao.selectByClientId(clientId);
    }
}