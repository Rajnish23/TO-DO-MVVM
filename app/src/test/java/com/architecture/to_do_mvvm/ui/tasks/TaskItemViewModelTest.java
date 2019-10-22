package com.architecture.to_do_mvvm.ui.tasks;

import android.content.Context;
import android.content.res.Resources;

import com.architecture.to_do_mvvm.Injection;
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

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskItemViewModelTest {

    private static final String NO_DATA_STRING = "NO_DATA_STRING";

    private static final String NO_DATA_DESC_STRING = "NO_DATA_DESC_STRING";

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private Context mContext;

    @Mock
    private TaskActivity mTaskItemNavigator;

    @Captor
    private ArgumentCaptor<TasksDataSource.GetTaskCallback> mLoadTaskCallbackCaptor;

    private TaskItemViewModel mTaskItemViewModel;

    private Task mTask;

    @Before
    public void setupTasksViewmodel(){
        MockitoAnnotations.initMocks(this);

        setupContext();

        //Get a refernce of viewmodel
        mTaskItemViewModel = new TaskItemViewModel(mContext,
                mTasksRepository);

        mTaskItemViewModel.setNavigator(mTaskItemNavigator);
    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);

        when(mContext.getString(R.string.no_data)).thenReturn(NO_DATA_STRING);

        when(mContext.getString(R.string.no_data_description)).thenReturn(NO_DATA_DESC_STRING);

        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void clickOnTask_ShowsDetalUI(){
        loadTaskIntoViewModel();

        mLoadTaskCallbackCaptor.getValue().onTaskLoaded(mTask); //Trigger Callback

        //verify the task is same what we requested
        assertEquals(mTaskItemViewModel.title.get(), mTask.getTitle());
        assertEquals(mTaskItemViewModel.description.get(), mTask.getDescription());
    }

    @Test
    public void nullTask_ShowsNoData(){
        loadTaskIntoViewModel();

        //Load something different from null first (Otherwise the change callback isn't run)
        mLoadTaskCallbackCaptor.getValue().onTaskLoaded(mTask);
        mLoadTaskCallbackCaptor.getValue().onTaskLoaded(null); //Trigger Callback

        //verify the task is same what we requested
        assertEquals(mTaskItemViewModel.title.get(), NO_DATA_STRING);
        assertEquals(mTaskItemViewModel.description.get(), NO_DATA_DESC_STRING);

    }

    @Test
    public void completeTask_showTasksMarkedComplete(){
        loadTaskIntoViewModel();

        mLoadTaskCallbackCaptor.getValue().onTaskLoaded(mTask); //Trigger callback

        //When task is marked complete
        mTaskItemViewModel.setCompleted(true);

        //Verify completeTask is invoked on repository instance
        verify(mTasksRepository).completeTask(mTask);
    }

    @Test
    public void ActivateTask_showTasksMarkedComplete(){
        loadTaskIntoViewModel();

        mLoadTaskCallbackCaptor.getValue().onTaskLoaded(mTask); //Trigger callback

        //When task is marked complete
        mTaskItemViewModel.setCompleted(false);

        //Verify completeTask is invoked on repository instance
        verify(mTasksRepository).activateTask(mTask);
    }

    @Test
    public void unavailableTasks_ShowsError() {
        loadTaskIntoViewModel();

        mLoadTaskCallbackCaptor.getValue().onDataNotAvailable(); // Trigger callback

        // Then repository is called
        assertFalse(mTaskItemViewModel.isDataAvailable());
    }

    private void loadTaskIntoViewModel() {
        //Stubbed Task
        mTask = new Task("Details Title", "For this description");

        //When open task detail is requested
        mTaskItemViewModel.start(mTask.getId());

        //verify getTask on repository get invoked, and use captor to get callback
        verify(mTasksRepository).getTask(eq(mTask.getId()) , mLoadTaskCallbackCaptor.capture());
    }

}
