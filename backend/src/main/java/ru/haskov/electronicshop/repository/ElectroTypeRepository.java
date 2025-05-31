package ru.haskov.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.haskov.electronicshop.db.entity.ElectroType;

@Repository
public interface ElectroTypeRepository extends JpaRepository<ElectroType, Long> {
}
