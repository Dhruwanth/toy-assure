package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.NoResultException;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChannelListingDao extends AbstractDao<ChannelListingPojo> {
    ChannelListingDao() {
        super(ChannelListingPojo.class);
    }

    private static String SELECT_BY_CHANNEL_ID_AND_GLOBAL_SKU_IDS = "select p from ChannelListingPojo p where channelId=:channelId AND globalSkuId IN (:globalSkuIds)";

    @Transactional(readOnly = true)
    public ChannelListingPojo selectByChannelAndGlobalSku(Long channelId, Long globalSkuId) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<ChannelListingPojo> q = cb.createQuery(ChannelListingPojo.class);
        Root<ChannelListingPojo> c = q.from(ChannelListingPojo.class);
        q.select(c);
        ParameterExpression<Long> channelIdParam = cb.parameter(Long.class);
        ParameterExpression<Long> globalSkuIdParam = cb.parameter(Long.class);
        q.where(
                cb.equal(c.get("channelId"), channelIdParam),
                cb.equal(c.get("globalSkuId"), globalSkuIdParam)
        );
        TypedQuery<ChannelListingPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(channelIdParam, channelId);
        typedQuery.setParameter(globalSkuIdParam, globalSkuId);
        return getSingle(typedQuery);
    }

    public ChannelListingPojo selectByChannelIdChannelSkuAndClient(Long channelId, String channelSkuId, Long clientId) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<ChannelListingPojo> q = cb.createQuery(ChannelListingPojo.class);
        Root<ChannelListingPojo> c = q.from(ChannelListingPojo.class);
        q.select(c);
        ParameterExpression<Long> clientIdParam = cb.parameter(Long.class);
        ParameterExpression<Long> channelIdParam = cb.parameter(Long.class);
        ParameterExpression<String> channelSkuIdParam = cb.parameter(String.class);
        q.where(
                cb.equal(c.get("channelId"), channelIdParam),
                cb.equal(c.get("clientId"), clientIdParam),
                cb.equal(c.get("channelSkuId"), channelSkuIdParam)
        );
        TypedQuery<ChannelListingPojo> typedQuery = entityManager().createQuery(q);
        typedQuery.setParameter(channelIdParam, channelId);
        typedQuery.setParameter(clientIdParam, clientId);
        typedQuery.setParameter(channelSkuIdParam, channelSkuId);
        return getSingle(typedQuery);
    }

    public List<ChannelListingPojo> selectByChannelIdAndGlobalSkuIds(Long channelId, List<Long> globalSkuIds){
        List<ChannelListingPojo> pojoList;
        try{
            TypedQuery<ChannelListingPojo> q = getQuery(SELECT_BY_CHANNEL_ID_AND_GLOBAL_SKU_IDS, ChannelListingPojo.class);
            q.setParameter("channelId", channelId);
            q.setParameter("globalSkuIds", globalSkuIds);
            pojoList = q.getResultList();
        }catch (NoResultException e){
            pojoList = new ArrayList<>();
        }
        return pojoList;
    }
}
