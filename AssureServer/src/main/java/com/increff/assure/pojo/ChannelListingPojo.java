package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames={"channelId", "globalSkuId"}),
        @UniqueConstraint(columnNames={"channelId", "clientId", "channelSkuId"})})
public class ChannelListingPojo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelSkuId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long clientId;
}
