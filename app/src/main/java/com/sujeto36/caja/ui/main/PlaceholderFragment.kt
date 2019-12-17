package com.sujeto36.caja.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.sujeto36.caja.ExpandableListAdapter
import com.sujeto36.caja.HeaderModel
import com.sujeto36.caja.R
import com.sujeto36.caja.db.main.IngEgDBHelper
import com.sujeto36.caja.db.main.IngEgModel
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.util.ArrayList
import java.util.HashMap

class PlaceholderFragment(type: String) : Fragment() {

    private lateinit var listAdapter: ExpandableListAdapter
    private lateinit var expListView: ExpandableListView
    private lateinit var listDataHeader: MutableList<HeaderModel>
    private lateinit var listDataChild: HashMap<String, List<IngEgModel>>
    private lateinit var ingEgDBHelper: IngEgDBHelper
    private var value = type

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        ingEgDBHelper = IngEgDBHelper(context!!.applicationContext)
        if (ingEgDBHelper.countIngEg(value) != 0) {
            // get the listview
            expListView = root.expandable_list
            // preparing list data
            prepareListData()
            listAdapter = ExpandableListAdapter(value, root.context, listDataHeader, listDataChild)
            // setting list adapter
            expListView.setAdapter(listAdapter)
        }

        return root
    }

    private fun prepareListData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()

        val listAll: ArrayList<IngEgModel> = ingEgDBHelper.read(value)
        val listHeader: ArrayList<HeaderModel> = ArrayList()
        val listChild: ArrayList<ArrayList<IngEgModel>> = ArrayList()

        //carga lista de fechas para cabecera
        listHeader.add(HeaderModel(listAll[0].date))
        var j = 0
        for (i in listAll.indices) {
            if (listAll[i].date != listHeader[j].date) {
                listHeader.add(HeaderModel(listAll[i].date))
                j++
            }
        }
        //carga lista de precios para cabecera
        j = 0
        for (i in listAll.indices) {
            if (listAll[i].date != listHeader[j].date) {
                j++
            }
            listHeader[j].price = listHeader[j].price + listAll[i].price
        }

        // Adding header data
        for (i in listHeader.indices) {
            listDataHeader.add(listHeader[i])
            listChild.add(ArrayList())
        }
        // Adding child data
        j = 0
        for (i in listAll.indices) {
            if (listAll[i].date != listHeader[j].date) {
                j++
            }
            listChild[j].add(listAll[i])
        }
        for (i in listHeader.indices) {
            listDataChild.put(listDataHeader[i].date, listChild[i])
        }

    }
}