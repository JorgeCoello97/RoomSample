package com.jorch.roomsample

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.jorch.roomsample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var adapter: PersonAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = applicationContext as App
        with(binding){
            lifecycleScope.launch {
                swipeRefresh.isRefreshing = true
                var people: List<Person>
                withContext(Dispatchers.IO) {
                    people = app.room.personDao().getAll()
                }
                adapter = PersonAdapter(lifecycleScope, ::envelopeForDelete, people)
                recyclerViewPersons.adapter = adapter
                swipeRefresh.isRefreshing = false
            }

            bttnRandom.setOnClickListener {
                val randomValue = Random(System.currentTimeMillis())
                editTextName.setText(randomValue.nextInt().toString())
                editTextSurname.setText(randomValue.nextInt().toString())
            }

            bttnAddPerson.setOnClickListener {
                if (editTextName.text.isEmpty() && editTextSurname.text.isEmpty()) bttnRandom.performClick()
                val person = Person(
                    name = editTextName.text.toString(),
                    surname = editTextSurname.text.toString())
                lifecycleScope.launch {
                    swipeRefresh.isRefreshing = true
                    withContext(Dispatchers.IO) {
                        app.room.personDao().insert(person)
                    }
                    adapter.people + person
                    adapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }
    private fun envelopeForDelete(person:Person, scope: CoroutineScope) = with(binding){
        scope.launch() {
            swipeRefresh.isRefreshing  = true
            val seqPeople = adapter.people.asSequence()
            val idPersonDeleted = person.id

            withContext(Dispatchers.IO){
                app.room.personDao().delete(person)
            }

            adapter.people = seqPeople
                .filter { person.id != idPersonDeleted }
                .toList()
            adapter.notifyDataSetChanged()
            swipeRefresh.isRefreshing  = false
        }
    }
}