package com.arnas.abp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = createItems()
        adapter = ItemAdapter(items) { item ->
            if (!item.isCompleted) showPinDialog(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        checkIfAllTasksCompleted()
    }

    private fun showPinDialog(item: Item) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_pin, null)
        val pinEditText: EditText = dialogView.findViewById(R.id.pinEditText)
        val confirmButton: Button = dialogView.findViewById(R.id.confirmButton)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        confirmButton.setOnClickListener {
            val enteredPin = pinEditText.text.toString()
            if (enteredPin == item.password) {
                toggleItemCompletion(item)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun toggleItemCompletion(item: Item) {
        val editor = sharedPreferences.edit()
        val newStatus = !item.isCompleted
        editor.putBoolean(item.id.toString(), newStatus)
        editor.apply()
        adapter.items = createItems()
        adapter.notifyDataSetChanged()
        checkIfAllTasksCompleted()
    }

    private fun createItems(): List<Item> {
        val items = mutableListOf<Item>()

        items.add(Item(1,"Task 1", "Advices", "1111", false))
        items.add(Item(2,"Task 2", "Treasure 1", "1111", false))
        items.add(Item(3,"Task 3", "Treasure 2", "1111", false))
        items.add(Item(4,"Task 4", "Treasure 3", "1111", false))
        items.add(Item(5,"Task 5", "Description 1", "1111", false))
        items.add(Item(6,"Task 6", "Description 2", "1111", false))
        items.add(Item(7,"Task 7", "Description 3", "1111", false))

        return items.map {
            it.copy(isCompleted = sharedPreferences.getBoolean(it.id.toString(), false))
        }
    }

    private fun checkIfAllTasksCompleted() {
        val allCompleted = createItems().all { it.isCompleted }
        if (allCompleted) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fcv, CompletedFragment())
                .commitAllowingStateLoss()
        }
    }
}