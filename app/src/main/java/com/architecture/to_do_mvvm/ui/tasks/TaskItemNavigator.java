package com.architecture.to_do_mvvm.ui.tasks;

import com.architecture.to_do_mvvm.data.Task;

/**
 * Defines the navigation actions that can be called from a list item in Task List.
 */
public interface TaskItemNavigator {

    void openTaskDetails(String taskId);

    void completeTask(Task task);

    void activateTask(Task task);
}
