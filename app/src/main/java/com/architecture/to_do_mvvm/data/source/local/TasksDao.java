package com.architecture.to_do_mvvm.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.architecture.to_do_mvvm.data.DatabaseConstant;
import com.architecture.to_do_mvvm.data.Task;

import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface TasksDao {

    /**
     * Select all tasks from the tasks table
     *
     * @return all tasks.
     */
    @Query(DatabaseConstant.GET_ALL_TASK)
    List<Task> getTasks();

    /**
     * Select a task by id
     *
     * @param taskId the task id
     * @return the task with taskid
     */
    @Query(DatabaseConstant.GET_TASK_BY_ID)
    Task getTaskById(String taskId);

    /**
     * Insert a task in database. If task already exists, replace it
     * @param task the task to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    /**
     * Update a task
     *
     * @param task to be updated
     * @return the number of tasks updated, This should always be 1.
     */
    @Update
    int updateTask(Task task);

    /**
     * Update the complete status of task
     *
     * @param taskId id of the task
     * @param completed status to be updated.
     */
    @Query(DatabaseConstant.UPDATE_TASK_STATUS)
    void updateCompleted(String taskId, boolean completed);

    /**
     * Delete a task by id
     *
     * @param taskId id of the task
     * @return the number of the taks deleted. This should be always 1
     */
    @Query(DatabaseConstant.DELETE_TASK_BY_ID)
    int deleteTaskById(String taskId);

    /**
     * Delete all tasks
     */
    @Query(DatabaseConstant.DELETE_ALL_TASK)
    void deleteAllTasks();

    void deleteCompletedTask();
}
