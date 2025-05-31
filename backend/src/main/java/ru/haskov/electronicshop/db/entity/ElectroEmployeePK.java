package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectroEmployeePK implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "employee_id")
    Long employeeId;

    @Column(name = "electro_type_id")
    Long electroTypeId;


}
