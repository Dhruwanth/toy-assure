package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"orderId","globalSkuId"})}
)
public class OrderItemPojo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long orderedQuantity;

    @Column(nullable = false)
    private Long allocatedQuantity = 0L;

    @Column(nullable = false)
    private Long fulfilledQuantity = 0L;
}
