package com.jorch.roomsample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jorch.roomsample.databinding.ActivityMainBinding
import com.jorch.roomsample.databinding.ItemPersonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PersonAdapter(
    val scope: CoroutineScope,
    val listener: (person: Person, scope: CoroutineScope) -> Unit,
    var people: List<Person>): RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(people[position])
    }

    override fun getItemCount() = people.size

    inner class PersonViewHolder(private val binding: ItemPersonBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(person:Person) = with(person){
            binding.textViewName.text = name
            binding.textViewSurname.text = surname
            binding.imageViewDelete.setOnClickListener {
                scope.launch {
                    listener(person, scope)
                }
            }

        }
    }
}