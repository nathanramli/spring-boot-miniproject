package com.nathan.miniproject.domain.dao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nathan.miniproject.domain.common.BaseDao;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(name = "user_stocks")
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@IdClass(UserStocks.UserStockId.class)
public class UserStocks extends BaseDao implements Serializable {
    private static final long serialVersionUID = -1472328914838883463L;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserStockId implements Serializable {
        private static final long serialVersionUID = 192857303874636011L;

        private Long user;
        private String stock;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Id
    @ManyToOne
    @JoinColumn(name = "stock_code", nullable = false)
    private Stocks stock;

    @Column(name = "share_units", nullable = false)
    private Long shareUnits;

    @Column(name = "price", nullable = false)
    private Double price;
}
