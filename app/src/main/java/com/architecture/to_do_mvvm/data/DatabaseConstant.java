package com.architecture.to_do_mvvm.data;

public class DatabaseConstant {

    public static final String TABLE_NAME = "Tasks";

    public static final String COLUMN_ID = "entryid";

    public static final String COLUMN_TITLE = "title";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_STATUS = "completed";

    public static final String GET_ALL_TASK = "SELECT * FROM " + TABLE_NAME;

    public static final String GET_TASK_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :taskId";

    public static final String UPDATE_TASK_STATUS = "UPDATE " + TABLE_NAME + " SET " + COLUMN_STATUS +
            " = :completed WHERE "+ COLUMN_ID+" = :taskId";

    public static final String DELETE_TASK_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE "+ COLUMN_ID+" = :taskId";

    public static final String DELETE_ALL_TASK = "DELETE FROM " + TABLE_NAME;

    public static final String DELETE_COMPLETED_TASK = "DELETE FROM "+TABLE_NAME +" WHERE "+COLUMN_STATUS +" = 1";
}
