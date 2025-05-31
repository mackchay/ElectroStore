package ru.haskov.electronicshop.model.dto;

import jakarta.persistence.*;
import ru.haskov.electronicshop.db.entity.PositionType;

import java.io.Serial;
import java.time.LocalDate;

public class EmployeeDTO {
    private Long id;

    private String lastName;

    private String firstName;

    private String patronymic;

    private LocalDate birthDate;

    private String positionTypeName;

    private boolean gender;

    private Integer soldItemsQuantity;

    private Integer soldItemsSum;
}
