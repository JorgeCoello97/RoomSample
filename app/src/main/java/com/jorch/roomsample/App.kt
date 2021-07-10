package com.jorch.roomsample

import android.app.Application
import androidx.room.Room

class App: Application(){

    lateinit var room: PeopleDb

    override fun onCreate() {
        super.onCreate()
        room = Room.databaseBuilder(this,PeopleDb::class.java,"person")
            .build()
    }
}