package com.increff.assure.dao;

import com.increff.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao<ProductPojo> {
    ProductDao() {
        super(ProductPojo.class);
    }

    private static final String SELECT_BY_GLOBAL_SKU_IDS = "select p from ProductPojo p where id IN (:globalSkuIds)";

    @Transactional(readOnly = true)
    public ProductPojo selectByClientIdAndClientSku(Long clientId, String clientSkuId) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> c = q.from(ProductPojo.class);
        q.select(c);
        ParameterExpression<Long> clientIdParam = cb.parameter(Long.class);
        ParameterExpression<String> clientSkuIdParam = cb.parameter(String.class);
        q.where(
                cb.equal(c.get("clientId"), clientIdParam),
                cb.equal(c.get("clientSkuId"), clientSkuIdParam)
        );
        TypedQuery<ProductPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(clientIdParam, clientId);
        typedQuery.setParameter(clientSkuIdParam, clientSkuId);
        return getSingle(typedQuery);
    }

    public List<ProductPojo> selectByClientId(Long clientId) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> c = q.from(ProductPojo.class);
        q.select(c);
        ParameterExpression<Long> clientIdParam = cb.parameter(Long.class);
        q.where(
                cb.equal(c.get("clientId"), clientIdParam)
        );
        TypedQuery<ProductPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(clientIdParam, clientId);
        return typedQuery.getResultList();
    }

    public List<ProductPojo> selectByClientSku(String clientSkuId) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> c = q.from(ProductPojo.class);
        q.select(c);
        ParameterExpression<String> clientSkuIdParam = cb.parameter(String.class);
        q.where(
                cb.equal(c.get("clientSkuId"), clientSkuIdParam)
        );
        TypedQuery<ProductPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(clientSkuIdParam, clientSkuId);
        return typedQuery.getResultList();
    }

    public List<ProductPojo> selectByGlobalSkuIds(List<Long> globalSkuIds){
        List<ProductPojo> finalList = new ArrayList<>();
        for(List<Long> partitionedGlobalSkus: partition(globalSkuIds)){
            TypedQuery<ProductPojo> q = getQuery(SELECT_BY_GLOBAL_SKU_IDS, ProductPojo.class);
            q.setParameter("globalSkuIds", globalSkuIds);
            finalList.addAll(q.getResultList());
        }
        return finalList;
    }
}
