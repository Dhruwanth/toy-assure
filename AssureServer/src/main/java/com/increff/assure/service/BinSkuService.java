package com.increff.assure.service;

import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.pojo.BinSkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuService extends AbstractService {
    @Autowired
    private BinSkuDao binSkuDao;

    public void addOrUpdate(BinSkuPojo inputPojo) {
        BinSkuPojo existingPojo = getByBinIdAndGlobalSku(inputPojo.getBinId(), inputPojo.getGlobalSkuId());
        if (Objects.nonNull(existingPojo))
            existingPojo.setQuantity(inputPojo.getQuantity() + existingPojo.getQuantity());
        else
            binSkuDao.insert(inputPojo);
    }

    public List<BinSkuPojo> getAll() {
        return binSkuDao.selectAll();
    }

    public BinSkuPojo getByBinIdAndGlobalSku(Long binId, Long globalSkuId) {
        return binSkuDao.selectByBinIdAndGlobalSku(binId, globalSkuId);
    }

    public BinSkuPojo getCheckId(Long id) throws ApiException {
        BinSkuPojo binSkuPojo = binSkuDao.select(id);
        checkNotNull(binSkuPojo, "Bin Inventory Item (ID:" + id + ") does not exist.");
        return binSkuPojo;
    }

    public Long removeFromBin(BinSkuPojo targetBin, Long requiredQuantity) {
        Long deduction;
        deduction = min(targetBin.getQuantity(), requiredQuantity);
        targetBin.setQuantity(targetBin.getQuantity() - deduction);

        return requiredQuantity - deduction;
    }

    public List<BinSkuPojo> selectBinsByGlobalSku(Long globalSku) {
        return binSkuDao.selectByGlobalSku(globalSku);
    }//delete the bin if quantity is zero or while searching just search the non zero quantity bins.

    public void addList(List<BinSkuPojo> binSkuMasterPojoList) {
        for (BinSkuPojo binSkuPojo : binSkuMasterPojoList)
            addOrUpdate(binSkuPojo);
    }
    public Long allocateFromAllBins(Long globalSku, Long quantityToAllocate) {
        Long remainingQuantityToAllocate = quantityToAllocate;
        List<BinSkuPojo> allBinSkus = selectBinsByGlobalSku(globalSku);
        sortByQuantity(allBinSkus);

        for (BinSkuPojo binSkuPojo : allBinSkus) {
            remainingQuantityToAllocate = removeFromBin(binSkuPojo, remainingQuantityToAllocate);
            if (remainingQuantityToAllocate == 0) return quantityToAllocate;
        }
        return quantityToAllocate - remainingQuantityToAllocate;
    }
    private void sortByQuantity(List<BinSkuPojo> allBinSkus) {
        allBinSkus.sort((bin1, bin2) -> (bin2.getQuantity()).compareTo(bin1.getQuantity()));
    }
    
    public List<BinSkuPojo> getSearchByBinAndProduct(Long binId, Long globalSkuId) {
        return binSkuDao.getSearchByBinAndProduct(binId, globalSkuId);
    }
}