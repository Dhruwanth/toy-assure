package com.increff.assure.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static model.Constants.PARTITION_SIZE;

public abstract class AbstractDao<T> {
    private final Class<T> clazz;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected TypedQuery<T> getQuery(String jpql, Class<T> targetClass) {
        return entityManager.createQuery(jpql, targetClass);
    }
    
    protected  TypedQuery<T> getQuery(String jpql) {
		return entityManager.createQuery(jpql, clazz);
	}

    protected EntityManager entityManager() {
        return entityManager;
    }

    @Transactional(readOnly = true)
    public T select(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(this.clazz);
        Root<T> c = q.from(this.clazz);
        q.select(c);
        ParameterExpression<Long> idParam = cb.parameter(Long.class);
        q.where(cb.equal(c.get("id"), idParam));
        TypedQuery<T> typedQuery = entityManager.createQuery(q);
        typedQuery.setParameter(idParam, id);
        return getSingle(typedQuery);
    }

    @Transactional(readOnly = true)
    public List<T> selectAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.clazz);
        Root<T> root = criteriaQuery.from(this.clazz);
        criteriaQuery.select(root);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        List<T> resultList = query.getResultList();
        if (Objects.isNull(resultList))
            return new ArrayList<>();
        return resultList;
    }

    protected <T> List<List<T>> partition(List<T> list){
        Long partitionSize = PARTITION_SIZE;
        List<List<T>> partitions = new ArrayList<>();

        for (int i=0; i<list.size(); i += partitionSize) {
            partitions.add(list.subList(i, Math.min(i + partitionSize.intValue(), list.size())));
        }
        return partitions;
    }

    @Transactional
    public void insert(T object) {
        entityManager.persist(object);
    }
}