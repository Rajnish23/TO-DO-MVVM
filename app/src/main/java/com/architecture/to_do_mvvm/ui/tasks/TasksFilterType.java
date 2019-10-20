package com.architecture.to_do_mvvm.ui.tasks;

public enum TasksFilterType {

    /*
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the Active Task (Not Completed yet).
     */

    ACTIVE_TASKS,

    /**
     * Filters only the Completed Task.
     */
    COMPLETED_TASKS
}
