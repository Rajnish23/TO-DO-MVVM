package com.architecture.to_do_mvvm.ui.tasks;

import android.content.Context;
import android.content.res.Resources;

import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksDataSource;
import com.architecture.to_do_mvvm.data.source.TasksRepository;
import com.architecture.to_do_mvvm.ui.addedittask.AddEditTaskActivity;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for the implementation of {@link com.architecture.to_do_mvvm.ui.tasks.TasksViewModel}
 */
public class TasksViewModelTest {

    private static List<Task> TASKS;

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private Context mContext;

    @Mock
    private TaskActivity mTasksNavigator;

    @Captor
    private ArgumentCaptor<TasksDataSource.LoadTasksCallback> mLoadTasksCallbackCaptor;

    private TasksViewModel mTasksViewmodel;

    private static final String SNACKBAR_TEXT = "Snackbar text";

    @Before
    public void setupTasksViewModel(){
        MockitoAnnotations.initMocks(this);

        setupContext();

        //Get a reference to the class under test
        mTasksViewmodel = new TasksViewModel(mTasksRepository,
                mContext);

        mTasksViewmodel.setNavigator(mTasksNavigator);

        // We initialise the tasks to 3, with one active and two completed
        TASKS = Lists.newArrayList(new Task("Title1", "Description1"),
                new Task("Title2", "Description2", true), new Task("Title3", "Description3", true));
    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);

        when(mContext.getString(R.string.successfully_saved_task_message))
                .thenReturn("EDIT_RESULT_OK");

        when(mContext.getString(R.string.successfully_added_task_message))
                .thenReturn("ADD_EDIT_RESULT_OK");

        when(mContext.getString(R.string.successfully_deleted_task_message))
                .thenReturn("DELETE_RESULT_OK");

        when(mContext.getString(R.string.completed_tasks_cleared))
                .thenReturn("COMPLETED_TASKS_CLEARED");

        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void loadAllTasksFromRepository_dataLoaded(){
        //When loading of tasks is requested, pass Default Filter type
        mTasksViewmodel.setFiltering(TasksFilterType.ALL_TASKS);
        mTasksViewmodel.loadTasks(true);

        //Verify the getTasks is invoked on repository with stubbed tasks
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());

        //Progress Indicator is shown
        assertTrue(mTasksViewmodel.dataLoading.get());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);

        //Progress Indicator is hidden
        assertFalse(mTasksViewmodel.dataLoading.get());

        //And the data loaded
        assertFalse(mTasksViewmodel.items.isEmpty());
        assertEquals(mTasksViewmodel.items.size(), TASKS.size());
    }

    @Test
    public void loadActiveTasksFromRepositoryAndLoadIntoView(){
        //When load active task is requested, pass {@link TasksFilterType #ACTIVE_TASKS}
        mTasksViewmodel.setFiltering(TasksFilterType.ACTIVE_TASKS);
        mTasksViewmodel.loadTasks(true);

        //Verify the getTasks is invoked on repository with stubbed tasks
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);

        //The Progress indicator is hidden
        assertFalse(mTasksViewmodel.dataLoading.get());

        //And the data loaded
        assertFalse(mTasksViewmodel.items.isEmpty());
        assertEquals(mTasksViewmodel.items.size() ,1);
    }

    @Test
    public void loadCompletedTasksFromRepositoryAndLoadIntoView(){
        //When load completed tasks is requested, pass {@link TasksFilterType #COMPLETED_TASKS}
        mTasksViewmodel.setFiltering(TasksFilterType.COMPLETED_TASKS);
        mTasksViewmodel.loadTasks(true);

        //Verify the getTasks is invoked on repository with stubbed tasks
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);

        //The progress indicator is hidden
        assertFalse(mTasksViewmodel.dataLoading.get());

        //And the data loaded
        assertFalse(mTasksViewmodel.items.isEmpty());
        assertEquals(mTasksViewmodel.items.size() ,2);
    }

    @Test
    public void clickOnFab_ShowsAddTaskUI(){
        //When adding a new tasks
        mTasksViewmodel.addNewTask();

        //Verify the navigator is called
        verify(mTasksNavigator).addNewTask();
    }

    @Test
    public void clearCompletedTasks_ClearTasks(){
        //When completed tasks is selected on menu
        mTasksViewmodel.clearCompletedTasks();

        assertThat(mTasksViewmodel.getSnackbarText(), is("COMPLETED_TASKS_CLEARED"));
        //The repository is called and view is notified
        verify(mTasksRepository).clearCompletedTask();
        verify(mTasksRepository).getTasks(any(TasksDataSource.LoadTasksCallback.class));
    }

    @Test
    public void handleActivityResult_addEditOK(){

        //When AddEditTaskActivity sends a ADD_EDIT_RESULT_OK
        mTasksViewmodel.handleActivityResult(AddEditTaskActivity.REQUEST_CODE,AddEditTaskActivity.ADD_EDIT_RESULT_OK);

        //Then the snackbar shows the correct message
        assertThat(mTasksViewmodel.getSnackbarText(), is("ADD_EDIT_RESULT_OK"));
    }

    @Test
    public void getTasksAddViewVisible(){
        //When the filter type is ALL_TASKS
        mTasksViewmodel.setFiltering(TasksFilterType.ALL_TASKS);

        //Then the "Add Tasks" action is visible
        assertThat(mTasksViewmodel.tasksAddViewVisible.get(), is(true));
    }

    @Test
    public void updateSnackbar_nullValue(){
        //Before setting the Snackbar text, get its current value
        String snackbarText = mTasksViewmodel.getSnackbarText();

        //Check that the value is null
        assertThat(snackbarText, is(nullValue()));
    }

    @Test
    public void updateSnckbar_nonNullValue(){
        //Set a new value to snackbar
        mTasksViewmodel.snackbarText.set(SNACKBAR_TEXT);

        //Get its current value
        String snackbarText = mTasksViewmodel.getSnackbarText();

        //Check that the value matches
        assertThat(snackbarText, is(SNACKBAR_TEXT));
    }
}
