package com.nathan.miniproject.domain.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nathan.miniproject.domain.common.BaseDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE stocks SET deleted_at = CURRENT_TIMESTAMP WHERE code = ?")
@Where(clause = "deleted_at IS NULL")
public class Stocks extends BaseDao implements Serializable {
    private static final long serialVersionUID = 5318745549096487085L;

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stock")
    private List<UserStocks> userStocks;
}
