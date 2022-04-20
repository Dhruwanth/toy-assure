package com.increff.assure.dao;

import com.increff.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
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

    public List<ProductPojo> search(Long clientId, String clientSkuId) {
        List<ProductPojo> pojoList = new ArrayList<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> cq = cb.createQuery(ProductPojo.class);

        Root<ProductPojo> root = cq.from(ProductPojo.class);
        List<Predicate> predicates = new ArrayList<>();

        if (clientSkuId == null) {
            predicates.add(cb.equal(root.get("clientId"), clientId));
        } else {
            predicates.add(cb.equal(root.get("clientId"), clientId));
            predicates.add(cb.equal(root.get("clientSkuId"), clientSkuId));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        Query query = entityManager.createQuery(cq);
        pojoList = query.getResultList();
        return pojoList;
    }
}
