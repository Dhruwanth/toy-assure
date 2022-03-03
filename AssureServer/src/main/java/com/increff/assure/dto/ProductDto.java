package com.increff.assure.dto;

import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ApiException;
import com.increff.assure.service.PartyService;
import com.increff.assure.service.ProductService;
import com.increff.assure.util.ConvertUtil;
import com.increff.assure.util.NormalizeUtil;
import model.data.ProductData;
import model.form.ProductForm;
import model.form.ProductUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto extends AbstractDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private PartyService partyService;

    public void addProducts(List<ProductForm> productFormList, Long clientId) throws ApiException {
        partyService.getCheckClient(clientId);
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (ProductForm productForm : productFormList) {
            checkValid(productForm);
            NormalizeUtil.normalize(productForm);

            productPojoList.add(convertFormToPojo(productForm, clientId));
        }
        productService.addList(productPojoList);
    }

    public ProductData get(Long id) throws ApiException {
        return ConvertUtil.convert(productService.getCheckId(id), ProductData.class);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> allProductMasterPojo = productService.getAll();
        return ConvertUtil.convert(allProductMasterPojo, ProductData.class);
    }

    public void update(Long clientId, String clientSku, ProductUpdateForm form) throws ApiException {
        productService.update(clientId, clientSku, form);
    }

    public ProductData getByClientAndClientSku(Long clientId, String clientSkuId) throws ApiException {
        return ConvertUtil.convert(productService.getByClientAndClientSku(clientId, clientSkuId), ProductData.class);
    }

    public List<ProductData> getByClientId(Long clientId) throws ApiException {
        return ConvertUtil.convert(productService.getByClientId(clientId), ProductData.class);
    }

    public void validateFormList(List<ProductForm> formList, Long clientId) throws ApiException {
        partyService.getCheckClient(clientId);
        StringBuilder errorDetailString = new StringBuilder();
        for (int index = 0; index < formList.size(); index++) {
            try {
                checkValid((formList.get(index)));
            } catch (ApiException e) {
                errorDetailString.append("Error in Line: ").append(index + 1).append(": ").append(e.getMessage()).append("<br \\>");
            }
        }
        checkFalse(errorDetailString.length() > 0, errorDetailString.toString());//push code to git
    }

    private ProductPojo convertFormToPojo(ProductForm productForm, Long clientId) throws ApiException {
        ProductPojo productPojo = ConvertUtil.convert(productForm, ProductPojo.class);
        productPojo.setClientId(clientId);
        return productPojo;
    }
}