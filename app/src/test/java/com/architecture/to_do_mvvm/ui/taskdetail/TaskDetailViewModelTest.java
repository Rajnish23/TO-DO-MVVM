package com.architecture.to_do_mvvm.ui.taskdetail;

import android.content.Context;
import android.content.res.Resources;

import com.architecture.to_do_mvvm.R;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskDetailViewModelTest {

    private static final String TITLE_TEST = "title";

    private static final String DESCRIPTION_TEST = "description";

    private static final String NO_DATA_STRING = "NO_DATA_STRING";

    private static final String NO_DATA_DESC_STRING = "NO_DATA_DESC_STRING";

    public static final String SNACKBAR_TEXT = "Snackbar text";

    @Mock
    private TasksRepository mTaskRepository;

    @Mock
    private Context mContext;

    @Captor
    private ArgumentCaptor<TasksDataSource.GetTaskCallback> mGetTaskCallbackCaptor;

    private TaskDetailViewModel mTaskDetailViewModel;

    private Task mTask;

    @Before
    public void setupTaskViewModel(){
        MockitoAnnotations.initMocks(this);

        setupContext();

        mTask = new Task(TITLE_TEST, DESCRIPTION_TEST);

        mTaskDetailViewModel = new TaskDetailViewModel(mContext,
                mTaskRepository);
        mTaskDetailViewModel.setNavigator(mock(TaskDetailActivity.class));
    }

    @Test
    public void getActiveTaskFromRepositoryAndLoadIntoView(){
        setupViewModelRepositoryCallback();

        //Verify the task requested is correct or not
        assertEquals(mTaskDetailViewModel.title.get(), mTask.getTitle());
        assertEquals(mTaskDetailViewModel.description.get(), mTask.getDescription());
    }

    @Test
    public void deleteTask(){
        setupViewModelRepositoryCallback();

        //When deleteTask is requested
        mTaskDetailViewModel.deleteTask();

        //verify deleteTask is invoked from repository
        verify(mTaskRepository).deleteTask(mTask.getId());
    }

    @Test
    public void completeTask(){
        setupViewModelRepositoryCallback();

        //When complete task is requested
        mTaskDetailViewModel.setCompleted(true);

        //verify completeTask is invoked from repository
        verify(mTaskRepository).completeTask(mTask);
    }

    @Test
    public void activaTask(){
        setupViewModelRepositoryCallback();

        //When activate task is requested
        mTaskDetailViewModel.setCompleted(false);

        //verify activateTask is invoked from repository
        verify(mTaskRepository).activateTask(mTask);
    }

    @Test
    public void updateSnackbar_nullValue(){
        //Get Current value of Snackbar
        String snackbar = mTaskDetailViewModel.getSnackBarText();

        //assert that the current value is null
        assertThat("Snackbar Text does not match", snackbar, is(nullValue()));
    }

    @Test
    public void updateSnackbar_nonNullValue(){
        //Set the value of Snackbar
        mTaskDetailViewModel.snackBarText.set(SNACKBAR_TEXT);

        //Get the current value of Snackbar
        String snackBarText = mTaskDetailViewModel.getSnackBarText();

        //assert that the value is not null
        assertThat("Snackbar Text does not match", snackBarText, is(SNACKBAR_TEXT));
    }

    private void setupViewModelRepositoryCallback() {
        mTaskDetailViewModel.start(mTask.getId());

        verify(mTaskRepository).getTask(eq(mTask.getId()), mGetTaskCallbackCaptor.capture());

        mGetTaskCallbackCaptor.getValue().onTaskLoaded(mTask);
    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);

        when(mContext.getString(R.string.no_data)).thenReturn(NO_DATA_STRING);

        when(mContext.getString(R.string.no_data_description)).thenReturn(NO_DATA_DESC_STRING);

        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }
}
