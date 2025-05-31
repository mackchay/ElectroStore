package ru.haskov.electronicshop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "store_employee")
public class Employee implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор сотрудника
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    Long id;

    /**
     * Фамилия сотрудника
     */
    @Column(name = "lastname", nullable = false, length = 100)
    String lastName;

    /**
     * Имя сотрудника
     */
    @Column(name = "firstname", nullable = false, length = 100)
    String firstName;

    /**
     * Отчество сотрудника
     */
    @Column(name = "patronymic", nullable = false, length = 100)
    String patronymic;

    /**
     * Дата рождения сотрудника
     */
    @Column(name = "birth_date", nullable = false)
    LocalDate birthDate;

    /**
     * Ссылка на должность сотрудника
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    PositionType positionType;

    /**
     * Пол сотрудника (true - мужской, false - женский)
     */
    @Column(name = "gender", nullable = false)
    Boolean gender;

}
