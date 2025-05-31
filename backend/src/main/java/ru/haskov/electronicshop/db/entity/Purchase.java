package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase")
public class Purchase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "electro_id", nullable = false)
    ElectroItem electroItem;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @Column(name = "purchase_date", nullable = false)
    LocalDateTime purchaseDate;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    PurchaseType purchaseType;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    Shop shop;
}
