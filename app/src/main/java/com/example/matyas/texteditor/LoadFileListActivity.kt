package com.example.matyas.texteditor

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class LoadFileListActivity : ListActivity() {
    private val data = FileOperations.getFiles()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val intent = Intent()
        val bundle = Bundle()

        val fileName = data[position]

        bundle.putString("fileName", fileName)
        intent.putExtras(bundle)
        setResult(RESULT_OK, intent)
        finish()
    }
}
