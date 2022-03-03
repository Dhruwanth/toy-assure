package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import model.InvoiceType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "invoiceType"})}
)
public class ChannelPojo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;
}
