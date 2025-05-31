package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "electro_employee")
public class ElectroEmployee implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    ElectroEmployeePK id;

    @MapsId("employeeId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    @MapsId("electroTypeId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "electro_type_id", nullable = false)
    ElectroType electroType;

}
