package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElectroShopPK implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *  Идентификатор магазина
     */
    @Column(name = "shop_id")
    Long shopId;

    /**
     *  Идентификатор электротовара
     */
    @Column(name = "electro_item_id")
    Long electroItemId;

}
