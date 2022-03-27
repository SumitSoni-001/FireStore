package com.example.firebasetopics

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_data.*
import java.util.*
import kotlin.collections.ArrayList

class DataActivity : AppCompatActivity() {

    lateinit var list: ArrayList<dataModel>
    lateinit var adapter: DataAdapter
    lateinit var db: FirebaseFirestore

    lateinit var swipeGesture: SwipeGesture    // Contains Swipe Gesture

    lateinit var searchList: ArrayList<dataModel>   // Search List

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        list = ArrayList()
        searchList = ArrayList()
        db = FirebaseFirestore.getInstance()

        swipeGesture = object : SwipeGesture(this) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition

                Collections.swap(list, fromPos, toPos)
                adapter.notifyItemMoved(fromPos, toPos)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        adapter.delete(viewHolder.adapterPosition)
                        Toast.makeText(
                            this@DataActivity,
                            "Item deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        val archivedItem = list[viewHolder.adapterPosition]
                        list.removeAt(viewHolder.adapterPosition)
                        adapter.add(list.size, archivedItem)
                        Toast.makeText(
                            this@DataActivity,
                            "Item Archived successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Setting SwipeGestures on RecyclerView
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(dataRCV)

        readData()

    }

    fun readData() {
        db.collection("Users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) {
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                    }

                    // Contains the list of documents that changed since the last snapshot.
                    for (document in value?.documentChanges!!) {
                        if (document.type == DocumentChange.Type.ADDED) {

                            /** This list contains the newly added documents(i.e Not Modified only newly created) */
                            list.add(document.document.toObject(dataModel::class.java))
                        }
                    }

                    /** Filter by age */
                    list.sortBy {
                        it.Age
                    }

                    // Setting data on RecyclerView
                    dataRCV.layoutManager = LinearLayoutManager(applicationContext)
                    adapter = DataAdapter(applicationContext, list)
                    dataRCV.adapter = adapter

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        val search = menu?.findItem(R.id.search)
        val actionView = search?.actionView as SearchView

        actionView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                searchList.clear()

                val searchText = newText!!.lowercase(Locale.getDefault())

                if (searchText.isNotEmpty()) {

                    searchList.forEach {
                        // If searched text matches any name in list then that item will be added to new list and displayed
                        if (it.Name!!.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }

                    adapter.notifyDataSetChanged()

                } else {
                    searchList.clear()
                    searchList.addAll(list)
                    adapter.notifyDataSetChanged()
                }

                return false
            }

        })

        return true
    }

}