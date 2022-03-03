package com.increff.assure.dao;

import com.increff.assure.pojo.PartyPojo;
import model.PartyType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PartyDao extends AbstractDao<PartyPojo> {
    PartyDao() {
        super(PartyPojo.class);
    }

    @Transactional(readOnly = true)
    public PartyPojo selectByNameAndType(String name, PartyType type) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<PartyPojo> q = cb.createQuery(PartyPojo.class);
        Root<PartyPojo> c = q.from(PartyPojo.class);
        q.select(c);
        ParameterExpression<String> nameParam = cb.parameter(String.class);
        ParameterExpression<PartyType> typeParam = cb.parameter(PartyType.class);
        q.where(
                cb.equal(c.get("name"), nameParam),
                cb.equal(c.get("type"), typeParam)
        );
        TypedQuery<PartyPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(nameParam, name);
        typedQuery.setParameter(typeParam, type);
        return getSingle(typedQuery);
    }

    @Transactional(readOnly = true)
    public List<PartyPojo> selectAll(PartyType type) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<PartyPojo> q = cb.createQuery(PartyPojo.class);
        Root<PartyPojo> c = q.from(PartyPojo.class);
        q.select(c);
        ParameterExpression<PartyType> typeParam = cb.parameter(PartyType.class);
        q.where(
                cb.equal(c.get("type"), typeParam)
        );
        TypedQuery<PartyPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(typeParam, type);
        List<PartyPojo> resultList = typedQuery.getResultList();
        if (Objects.isNull(resultList))
            return new ArrayList<>();
        return resultList;
    }
}
