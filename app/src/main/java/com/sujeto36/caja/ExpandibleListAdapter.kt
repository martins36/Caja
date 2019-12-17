package com.sujeto36.caja

import java.util.HashMap

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.sujeto36.caja.db.main.IngEgModel

internal class ExpandableListAdapter(
    private val type: String,
    private val context: Context,
    private val titleList: List<HeaderModel>,
    private val dataList: HashMap<String, List<IngEgModel>>
) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return this.dataList[this.titleList[groupPosition].date]!![childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition) as IngEgModel
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }

        val txtListChild = convertView!!.findViewById<View>(R.id.text_item) as TextView //desc
        val txtPriceChild = convertView.findViewById<View>(R.id.text_item_price) as TextView //price

        txtListChild.text = childText.desc
        txtPriceChild.text = childText.priceFormat()
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.dataList[this.titleList[groupPosition].date]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.titleList[groupPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as HeaderModel
        if (convertView == null) {
            val layoutInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }

        val txtListHeader = convertView!!.findViewById<View>(R.id.text_group) as TextView
        txtListHeader.setTypeface(null, Typeface.BOLD)

        val txtPriceHeader = convertView.findViewById<View>(R.id.text_group_price) as TextView
        txtPriceHeader.setTypeface(null, Typeface.BOLD)

        when (type) {
            "E" -> txtPriceHeader.setTextColor(context.resources.getColor(R.color.colorRed))
            "I" -> txtPriceHeader.setTextColor(context.resources.getColor(R.color.colorGreen))
        }

        txtListHeader.text = headerTitle.dateShort()
        txtPriceHeader.text = headerTitle.priceFormat()

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}
