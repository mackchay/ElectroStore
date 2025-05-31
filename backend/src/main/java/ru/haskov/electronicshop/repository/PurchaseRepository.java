package ru.haskov.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.haskov.electronicshop.db.entity.Purchase;
import ru.haskov.electronicshop.model.dto.BestEmployeeDto;
import ru.haskov.electronicshop.model.dto.TopSellerDto;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("""
    SELECT new ru.haskov.electronicshop.model.dto.BestEmployeeDto(
        e.id,
        e.firstName,
        e.lastName,
        e.patronymic,
        e.birthDate,
        pt.name,
        e.gender,
        COUNT(p.id),
        SUM(i.price)
    )
    FROM Purchase p
    JOIN p.employee e
    JOIN e.positionType pt
    JOIN p.electroItem i
    WHERE p.purchaseDate >= CURRENT_DATE - 2 YEAR
    GROUP BY e.id, e.firstName, e.lastName, pt.name,
    e.patronymic, e.birthDate, e.gender
    ORDER BY COUNT(p.id) DESC, SUM(i.price) DESC
""")
    List<BestEmployeeDto> findBestEmployeesLastYear();

    @Query("""
    SELECT new ru.haskov.electronicshop.model.dto.TopSellerDto(
        e.id,
        e.firstName,
        e.lastName,
        e.patronymic,
        e.birthDate,
        pt.name,
        e.gender,
        COUNT(p.id),
        SUM(i.price),
        et.name
    )
    FROM Purchase p
    JOIN p.employee e
    JOIN e.positionType pt
    JOIN p.electroItem i
    JOIN i.electroType et
    WHERE pt.name = :positionName
      AND et.name = :electroTypeName
    GROUP BY e.id, e.firstName, e.lastName,
    e.patronymic, e.birthDate, e.gender, pt.name, et.name
    ORDER BY SUM(i.price) DESC
""")
    List<TopSellerDto> findTopSeller(
            @Param("positionName") String positionName,
            @Param("electroTypeName") String electroTypeName
    );

    @Query("""
    SELECT SUM(i.price)
    FROM Purchase p
    JOIN p.electroItem i
    JOIN p.purchaseType pt
    WHERE pt.name = :purchaseTypeName
""")
    BigDecimal getTotalCashSalesLastYear(
            @Param("purchaseTypeName") String purchaseTypeName
    );
}
