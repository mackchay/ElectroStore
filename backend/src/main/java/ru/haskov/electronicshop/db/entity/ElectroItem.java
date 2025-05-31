package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "electro_item")
public class ElectroItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    Long id;

    @Column(name = "name", nullable = false, length = 150)
    String name;

    @ManyToOne
    @JoinColumn(name = "etype_id", nullable = false)
    ElectroType electroType;

    @Column(name = "price", nullable = false)
    Long price;

    @Column(name = "count_", nullable = false)
    Integer count;

    @Column(name = "archive", nullable = false)
    Boolean archive;

    @Column(name = "description")
    String description;
}
