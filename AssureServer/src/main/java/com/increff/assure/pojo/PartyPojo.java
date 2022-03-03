package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import model.PartyType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "type"})})
public class PartyPojo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PartyType type;
}