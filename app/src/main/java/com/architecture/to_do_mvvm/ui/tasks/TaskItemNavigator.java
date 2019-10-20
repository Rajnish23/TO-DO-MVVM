package com.architecture.to_do_mvvm.ui.tasks;

/**
 * Defines the navigation actions that can be called from a list item in Task List.
 */
public interface TaskItemNavigator {

    void openTaskDetails(String taskId);
}
