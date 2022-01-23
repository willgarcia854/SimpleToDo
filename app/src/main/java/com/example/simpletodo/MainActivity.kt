package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import org.apache.commons.io.FileUtils as FileUtils

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClickListener(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()
                saveItems()
            }

        }
        // 1. let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // Code in here is going to be executed when the user clicks on a button
//            Log.i("Will", "user clicked on button")
//        }
        loadItems()
        // Look up the recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)
        // That's all!
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        // Set up the button and input field so that the user can enter a task
        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. Grab the text the user has inputted into the field
            val userInputtedTask = inputTextField.text.toString()
            // 2. Add the string to the list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            // 2.a. Notify the adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // 3. Clear out the field so it can be reset
            inputTextField.setText("")

            // save the file
            saveItems()

        }
    }
    // save the data the user has inputted
    // saving by writing and reading to a file
    // Get the file we need
    fun getDataFile() : File {

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }
    // Load the items by reading every item in the file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
    // Save items by loading them to our file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}