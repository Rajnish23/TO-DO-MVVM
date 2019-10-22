# TO-DO-MVVM
This app is clone of Google architecture sample branch todo-mvvm-databinding.
Uses the Data Binding Library to display the data and bind UI elements to actions.

## What You need to Know
Before going through this MVVM Project 
* Should be familiar with MVVM Architecture
* Have a knowledge of RoomDatabase
* Basics of Mockito for Unit Testing

## Structure of Code
* Data - Implements LocalData Source, DAO, RoomDatabase and Repository
* UI - Holds the Implementation of Fragment, Activites
* Util - Utitlity class AppExecutors, ActivityUtils, SnackbarUtils.

<img src="https://github.com/Rajnish23/TO-DO-MVVM/blob/master/mvvm-databinding.png" alt="Data binding keeps the View and ViewModel in sync."/>

## Learning outcome
* Learn about Two way binding, DataBindingAdapters 
* Learn about Observable. Which bounds to UI on property change.
* Room integration, Learn about Room annotation @Entity, @Dao, @Database, @ColumnInfo, @Insert, @Delete, @Query etc.
* Testing with Mockito