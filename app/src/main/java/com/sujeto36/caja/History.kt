package com.sujeto36.caja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListView
import com.sujeto36.caja.db.main.IngEgDBHelper
import kotlinx.android.synthetic.main.activity_history.*
import java.util.ArrayList
import java.util.HashMap

class History : AppCompatActivity() {

    private lateinit var listAdapter: ExpandableHistoryAdapter
    private lateinit var expListView: ExpandableListView
    private lateinit var listDataHeader: MutableList<HistoryModel>
    private lateinit var listDataChild: HashMap<String, List<HistoryChildModel>>
    private lateinit var ingEgDBHelper: IngEgDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        ingEgDBHelper = IngEgDBHelper(this)
        if (ingEgDBHelper.countSueldo() > 1) {
            // get the listview
            expListView = expandable_history
            // preparing list data
            prepareListData()
            listAdapter = ExpandableHistoryAdapter(this, listDataHeader, listDataChild)
            // setting list adapter
            expListView.setAdapter(listAdapter)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun prepareListData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        val size = ingEgDBHelper.countSueldo() - 2

        // carga los datos de la cabecera
        for (i in 0..size) {
            listDataHeader.add(ingEgDBHelper.readHistory(i + 1))
        }

        // carga los childs
        for (i in 0..size) {
            listDataChild.put(listDataHeader[i].dateSueldo, listDataHeader[i].historyChildModel)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}