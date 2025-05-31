package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "electro_shop")
public class ElectroShop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    ElectroShopPK electroShopPK;

    @ManyToOne
    @MapsId("shopId")
    @JoinColumn(name = "shop_id", nullable = false)
    Shop shop;

    @ManyToOne
    @MapsId("electroItemId")
    @JoinColumn(name = "electro_item_id", nullable = false)
    ElectroItem electroItem;

    /**
     * Оставшееся количество
     */
    @Column(name = "count_", nullable = false)
    int count;


}
