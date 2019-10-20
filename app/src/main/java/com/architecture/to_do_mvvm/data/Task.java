package com.architecture.to_do_mvvm.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Immutable model class for a task
 */
@Entity(tableName = DatabaseConstant.TABLE_NAME)
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseConstant.COLUMN_ID)
    private final String mId;

    @Nullable
    @ColumnInfo(name = DatabaseConstant.COLUMN_TITLE)
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = DatabaseConstant.COLUMN_DESCRIPTION)
    private final String mDescription;

    @ColumnInfo(name = DatabaseConstant.COLUMN_STATUS)
    private final boolean mCompleted;

    /**
     * Use this constructor to make new active task
     *
     * @param title       title of the task
     * @param description description of the task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description) {
        this(UUID.randomUUID().toString(), title, description, false);
    }

    /**
     * Use this constructor to create and active task if the task already have an id
     *
     * @param id          of the task
     * @param title       title of the task
     * @param desctiption description of the task
     */
    @Ignore
    public Task(@NonNull String id, @Nullable String title, String desctiption) {
        this(id, title, desctiption, false);
    }

    /**
     * Use this constructor to create new completed task
     *
     * @param title       of the task
     * @param description of the task
     * @param completed   true if task is completed, else false
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        this(UUID.randomUUID().toString(), title, description, completed);
    }

    /**
     * Use this constructor to specify a completed task if the task has an id
     *
     * @param mId          id of the task
     * @param mTitle       title of the task
     * @param mDescription description of the task
     * @param mCompleted   true if task is completed, else false
     */
    public Task(@NonNull String mId, @Nullable String mTitle, @Nullable String mDescription, boolean mCompleted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mCompleted = mCompleted;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        }
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) &&
                Strings.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task with title "+ mTitle;
    }
}
