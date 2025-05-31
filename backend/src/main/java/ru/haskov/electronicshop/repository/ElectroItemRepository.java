package ru.haskov.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.haskov.electronicshop.db.entity.ElectroItem;

@Repository
public interface ElectroItemRepository extends JpaRepository<ElectroItem, Long> {
}
