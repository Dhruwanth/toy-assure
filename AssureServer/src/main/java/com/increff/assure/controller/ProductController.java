package com.increff.assure.controller;

import com.increff.assure.dto.ProductDto;
import com.increff.assure.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.data.ProductData;
import model.form.ProductForm;
import model.form.ProductUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Add a List of Products for given client")
    @RequestMapping(path = "/list/{clientId}", method = RequestMethod.POST)
    public void addProducts(@PathVariable Long clientId, @RequestBody List<ProductForm> formList) throws ApiException {
        productDto.addProducts(formList, clientId);
    }

    @ApiOperation(value = "Get list of all products by clientId")
    @RequestMapping(path = "/client/{clientId}", method = RequestMethod.GET)
    public List<ProductData> getByClient(@PathVariable Long clientId) throws ApiException {
        return productDto.getByClientId(clientId);
    }

    @ApiOperation(value = "Get a product by Client and Client SKU")
    @RequestMapping(path = "/{clientId}/{clientSkuId}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Long clientId, @PathVariable String clientSkuId) throws ApiException {
        return productDto.getByClientAndClientSku(clientId, clientSkuId);
    }

    @ApiOperation(value = "Update a product entry")
    @RequestMapping(path = "/{clientId}/{clientSku}", method = RequestMethod.PUT)
    public void update(@PathVariable Long clientId, @PathVariable String clientSku,
                       @RequestBody ProductUpdateForm form) throws ApiException {
        productDto.update(clientId, form);
    }

    @ApiOperation(value = "Validate products master list for given Client")
    @RequestMapping(path = "/validate/{clientId}", method = RequestMethod.POST)
    public void validateList(@PathVariable Long clientId, @RequestBody List<ProductForm> formList) throws ApiException {
        productDto.validateFormList(formList, clientId);
    }
}