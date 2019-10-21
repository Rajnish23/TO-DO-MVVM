package com.architecture.to_do_mvvm.ui.addedittask;

import android.content.Context;

import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksDataSource;
import com.architecture.to_do_mvvm.data.source.TasksRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.*;

public class AddEditViewModelTest {
    public static final String SNACKBAR_TEXT = "Snackbar text";

    @Mock
    private TasksRepository mTaskRepository;

    @Captor
    private ArgumentCaptor<TasksDataSource.GetTaskCallback> mGetTaskCallbackCaptor;

    private AddEditTaskViewModel mAddEditViewModel;

    @Before
    public void setupAddEditTaskViewmodel(){
        MockitoAnnotations.initMocks(this);

        mAddEditViewModel = new AddEditTaskViewModel(
                mTaskRepository,
                mock(Context.class)
        );

        mAddEditViewModel.onActivityCreated(mock(AddEditTaskActivity.class));
    }

    @Test
    public void saveNewTaskToRepository_showSuccessMessageUI(){
        //Set the title and description befor calling saveTask on viewmodel
        mAddEditViewModel.title.set("New Task Title");
        mAddEditViewModel.description.set("Some Task Description");
        //Call the SaveTask
        mAddEditViewModel.saveTask();

        //Verify that saveTask is invoked on repository instance
        verify(mTaskRepository).saveTask(any(Task.class));
    }

    @Test
    public void populateTasks_CallRepoAndUpdateView(){
        Task testTask = new Task("1","Title", "Description");

        mAddEditViewModel = new AddEditTaskViewModel(
                mTaskRepository,
                mock(Context.class)
        );

        mAddEditViewModel.onActivityCreated(mock(AddEditTaskActivity.class));

        //When viewmodel is asked to pouplate the task
        mAddEditViewModel.start(testTask.getId());

        //Verify that getTask is called on TaskRepository
        verify(mTaskRepository).getTask(eq(testTask.getId()), mGetTaskCallbackCaptor.capture());

        //Simulate callback
        mGetTaskCallbackCaptor.getValue().onTaskLoaded(testTask);

        //Verify the fields
        Assert.assertEquals(mAddEditViewModel.title.get(), testTask.getTitle());
        Assert.assertEquals(mAddEditViewModel.description.get(), testTask.getDescription());
    }

    @Test
    public void updateSnackbar_nullValue(){
        //Before setting snackbar text, get its initial value
        String snackbarText = mAddEditViewModel.getSnackbarText();

        //Check that value is null
        Assert.assertThat("Snackbar text does not match",snackbarText, is(nullValue()));
    }

    @Test
    public void updateSnackbar_nonNullValue(){

        //Set the text of snackbar
        mAddEditViewModel.snackbarText.set(SNACKBAR_TEXT);

        //Get the current snackbar text
        String snackbarText = mAddEditViewModel.snackbarText.get();

        //Chect that value is not null
        Assert.assertThat("Snackbar text does not match",snackbarText, is(SNACKBAR_TEXT));
    }
}
