package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import model.OrderStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"channelId", "channelOrderId"})}
)
public class OrderPojo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelOrderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
