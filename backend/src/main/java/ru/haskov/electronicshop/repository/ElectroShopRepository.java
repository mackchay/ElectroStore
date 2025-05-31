package ru.haskov.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.haskov.electronicshop.db.entity.ElectroShop;
import ru.haskov.electronicshop.db.entity.ElectroShopPK;

@Repository
public interface ElectroShopRepository extends JpaRepository<ElectroShop, ElectroShopPK> {
}
