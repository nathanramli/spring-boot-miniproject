package com.nathan.miniproject.domain.dao;

import com.nathan.miniproject.domain.common.BaseDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
public class Transactions extends BaseDao implements Serializable {
    private static final long serialVersionUID = 5318745549096487085L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Stocks stock;

    @ManyToOne
    private Users user;

    @Column(name = "share_units", nullable = false)
    private Long shareUnits;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "is_buy", nullable = false)
    private Boolean isBuy;
}
