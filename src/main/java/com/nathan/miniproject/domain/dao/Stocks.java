package com.nathan.miniproject.domain.dao;

import com.nathan.miniproject.domain.common.BaseDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@SuperBuilder
@Entity
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE stocks SET deleted_at = CURRENT_TIMESTAMP WHERE code = ?")
@Where(clause = "deleted_at IS NULL")
public class Stocks extends BaseDao {
    private static final long serialVersionUID = 3942782107265430850L;

    @Id
    private String code;

    @Column(name = "name", nullable = false)
    private String name;
}
