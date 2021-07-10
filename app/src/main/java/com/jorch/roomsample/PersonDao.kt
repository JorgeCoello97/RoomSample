package com.jorch.roomsample

import androidx.room.*

@Dao
interface PersonDao {

    @Query("SELECT * FROM Person")
    suspend fun getAll(): List<Person>

    @Query("SELECT * FROM Person WHERE id = :id")
    suspend fun getById(id: Int): Person

    @Insert
    suspend fun insert(person: Person)

    @Insert
    suspend fun insert(people: List<Person>)

    @Update
    suspend fun update(person: Person)

    @Delete
    suspend fun delete(person: Person)
}