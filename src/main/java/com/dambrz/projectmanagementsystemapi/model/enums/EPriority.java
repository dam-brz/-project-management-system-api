package com.dambrz.projectmanagementsystemapi.model.enums;

public enum EPriority {
    NOT_SELECTED(0),
    URGENT(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4);


    private final int priorityCode;

    EPriority(int priorityCode) {
        this.priorityCode = priorityCode;
    }

    public int getPriorityCode() {
        return priorityCode;
    }
}
