package com.nathan.miniproject.domain.dao;

import com.nathan.miniproject.domain.common.BaseDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@SuperBuilder
@Entity
@Table(name = "order_books")
@NoArgsConstructor
@AllArgsConstructor
public class OrderBooks extends BaseDao implements Serializable {
    private static final long serialVersionUID = 7796028900945682148L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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
