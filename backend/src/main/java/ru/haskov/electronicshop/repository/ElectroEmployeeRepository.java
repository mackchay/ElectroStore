package ru.haskov.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.haskov.electronicshop.db.entity.ElectroEmployee;
import ru.haskov.electronicshop.db.entity.ElectroEmployeePK;

@Repository
public interface ElectroEmployeeRepository extends JpaRepository<ElectroEmployee, ElectroEmployeePK> {
}
