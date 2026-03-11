package com.shopwise.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopwise.app.entity.SaleItem;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    // top produits par volume vendu
    @Query("""
        SELECT si.product.id, si.product.name, si.product.price, SUM(si.quantity) AS total
        FROM SaleItem si
        GROUP BY si.product.id, si.product.name, si.product.price
        ORDER BY total DESC
        LIMIT :limit
        """)
    List<Object[]> findTopProducts(@Param("limit") int limit);

    // produits achetés ensemble (co-occurrence dans les mêmes ventes)
    @Query("""
        SELECT si2.product.id, si2.product.name, si2.product.price, COUNT(si2.id) AS coCount
        FROM SaleItem si1
        JOIN SaleItem si2 ON si1.sale = si2.sale
        WHERE si1.product.id = :productId
          AND si2.product.id <> :productId
        GROUP BY si2.product.id, si2.product.name, si2.product.price
        ORDER BY coCount DESC
        LIMIT :limit
        """)
    List<Object[]> findSimilarProducts(@Param("productId") Long productId, @Param("limit") int limit);
}
