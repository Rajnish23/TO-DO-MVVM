package com.architecture.to_do_mvvm.data.source.local;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.architecture.to_do_mvvm.data.Task;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class TaskDaoTest {
    private static final Task TASK = new Task("id","title", "description" , true);

    private ToDoDatabase mDatabase;

    @Before
    public void initDb(){
        //Use an in-memory database because the information is stores here disappers when process is killed.
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                ToDoDatabase.class).build();
    }

    @After
    public void closeDb(){
        mDatabase.close();
    }

    @Test
    public void insertTaskAndGetById(){
        //When inserting a task
        mDatabase.getTaskDao().insertTask(TASK);

        //Fetch the task by id
        Task loaded = mDatabase.getTaskDao().getTaskById(TASK.getId());

        assertTask(loaded, "id", "title", "description", true);

    }

    @Test
    public void insertTaskReplaceOnConflict(){
        //Insert the task
        mDatabase.getTaskDao().insertTask(TASK);

        //New task with same id
        Task newTask = new Task("id","Title2", "Description2", true);
        //New task with same id is inserted
        mDatabase.getTaskDao().insertTask(newTask);

        //Getting the task by id
        Task loaded = mDatabase.getTaskDao().getTaskById(newTask.getId());

        assertTask(loaded,"id","Title2", "Description2", true);
    }

    @Test
    public void insertTaskAndGetTask(){
        //When inserting a task
        mDatabase.getTaskDao().insertTask(TASK);

        //Fetch the Tasks
        List<Task> loaded = mDatabase.getTaskDao().getTasks();

        assertThat(loaded.size(), is(1));

        assertTask(loaded.get(0), "id", "title", "description", true);

    }

    @Test
    public void updateTaskAndGetById(){
        // When inserting a task
        mDatabase.getTaskDao().insertTask(TASK);

        //Define the updated task
        Task updateTask = new Task("id","title2","description2", true );

        mDatabase.getTaskDao().updateTask(updateTask);

        //Load the updated task
        Task loadedTask = mDatabase.getTaskDao().getTaskById(updateTask.getId());
        assertTask(loadedTask, "id", "title2", "description2", true);
    }

    @Test
    public void updateCompletedTaskAndGetById(){
        // When inserting a task
        mDatabase.getTaskDao().insertTask(TASK);

        //Define the completed updated task
        Task updateTask = new Task("id","title2","description2", false );

        mDatabase.getTaskDao().updateTask(updateTask);

        //Load the updated task
        Task loadedTask = mDatabase.getTaskDao().getTaskById(updateTask.getId());
        assertTask(loadedTask, "id", "title2", "description2", false);
    }

    @Test
    public void deleteTaskByIdAndGetTasks(){
        //Insert the task
        mDatabase.getTaskDao().insertTask(TASK);

        //When deleting the task by id
        mDatabase.getTaskDao().deleteTaskById(TASK.getId());

        //Get all the task after deletion
        List<Task> tasks = mDatabase.getTaskDao().getTasks();

        //Verify the size is 0
        assertThat(tasks.size(), is(0));
    }

    @Test
    public void deleteCompletedTaskByIdAndGetTasks(){
        //Insert the task one with complete and one with activate
        mDatabase.getTaskDao().insertTask(TASK);
        mDatabase.getTaskDao().insertTask(new Task("Activate Task", "Activate Description"));

        //When deleting the task by id
        mDatabase.getTaskDao().deleteCompletedTask();

        //Get all the task after deletion
        List<Task> tasks = mDatabase.getTaskDao().getTasks();

        //Verify the size is 0
        assertThat(tasks.size(), is(1));
    }


    private void assertTask(Task task, String id, String title, String description, boolean completed) {
        assertThat(task, notNullValue());
        assertThat(task.getId(), is(id));
        assertThat(task.getTitle(), is(title));
        assertThat(task.getDescription(), is(description));
        assertThat(task.isCompleted(), is(completed));
    }
}
