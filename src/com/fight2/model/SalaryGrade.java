package com.fight2.model;

public enum SalaryGrade {
    G1(1000),
    G2(2000),
    G3(3000),
    G4(4000),
    G5(5000);

    private final int salary;

    private SalaryGrade(final int salary) {
        this.salary = salary;
    }

    public int getSalary() {
        return salary;
    }

}
