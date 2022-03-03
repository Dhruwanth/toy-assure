package com.increff.assure.dto;

import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.service.*;
import model.data.BinSkuData;
import model.form.BinSkuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.increff.assure.util.ConvertUtil.convert;

@Service
public class BinDto extends AbstractDto {
    @Autowired
    private BinService binService;
    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public ArrayList<Long> add(Integer binCount) throws ApiException {
        checkTrue(binCount > 0, "Bin Count must be positive");
        return binService.addBins(binCount);
    }

    public List<Long> getAllBins() {
        return binService.getAll().stream().map(BinPojo::getId).collect(Collectors.toList());
    }
    
    @Transactional(rollbackFor = ApiException.class)
    public void add(BinSkuForm binSkuForm) throws ApiException {
        checkValid(binSkuForm);
        validateProductAndBin(binSkuForm);

        BinSkuPojo binSkuPojo = convert(binSkuForm, BinSkuPojo.class);
        binSkuService.addOrUpdate(binSkuPojo);
        addOrUpdateInventory(binSkuPojo);
    }

    public BinSkuData get(Long id) throws ApiException {
        return convert(binSkuService.getCheckId(id), BinSkuData.class);
    }
    
    @Transactional(rollbackFor = ApiException.class)
    public void addBinWiseInventory(List<BinSkuForm> formList) throws ApiException {
        checkValid(formList);
        for (BinSkuForm form : formList) {
            validateProductAndBin(form);

            BinSkuPojo binSkuPojo = convert(form, BinSkuPojo.class);
            binSkuService.addOrUpdate(binSkuPojo);
            addOrUpdateInventory(binSkuPojo);
        }
    }

    public List<BinSkuData> getAllBinSku() throws ApiException {
        return convert(binSkuService.getAll(), BinSkuData.class);
    }

    private void validateProductAndBin(BinSkuForm binSkuForm) throws ApiException {
        productService.getCheckId(binSkuForm.getGlobalSkuId());
        binService.getCheckId(binSkuForm.getBinId());
    }

    private void addOrUpdateInventory(BinSkuPojo binSkuPojo) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setAvailableQuantity(binSkuPojo.getQuantity());
        inventoryPojo.setGlobalSkuId(binSkuPojo.getGlobalSkuId());

        inventoryService.addOrUpdate(inventoryPojo);
    }
}