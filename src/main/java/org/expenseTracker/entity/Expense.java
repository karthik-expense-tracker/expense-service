package org.expenseTracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    private String category;

    @Column
    private int amount;

    @Column
    private String date;

    @Column
    private String expenseType;
}
