package com.example.fallingdown

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fallingdown.model.RecordModel
import com.example.fallingdown.model.SampleData
import com.example.fallingdown.model.SubItemModel

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val backToPreviousButton = findViewById<ImageView>(R.id.btn_record_back_page)
        backToPreviousButton.setOnClickListener {
            finish()
        }

        val sampleData = SampleData.records

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val recordRecyclerView = this.findViewById<RecyclerView>(R.id.recyclerView_record)
        recordRecyclerView.layoutManager = layoutManager
        recordRecyclerView.adapter = RecordAdapter(sampleData)
    }
}

private class RecordAdapter(private val records: List<RecordModel>) :
    RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    lateinit var context: Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRecordDate: TextView
        val subItemRecyclerView: RecyclerView

        init {
            txtRecordDate = view.findViewById(R.id.txt_record_date)
            subItemRecyclerView = view.findViewById(R.id.recyclerView_record_sub_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_record, parent, false)
        context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        val date = record.date
        val subItemList = record.subItemList

        val txtRecordDate = holder.txtRecordDate
        val subItemRecyclerView = holder.subItemRecyclerView
        txtRecordDate.text = date

        val subItemAdapter = SubItemAdapter(subItemList)
        subItemRecyclerView.adapter = subItemAdapter
        subItemRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        txtRecordDate.setOnClickListener {
            subItemRecyclerView.visibility = if (subItemRecyclerView.isShown) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int = records.size

}

private class SubItemAdapter(private val subItemList: List<SubItemModel>) :
    RecyclerView.Adapter<SubItemAdapter.ViewHolder>() {

    lateinit var context: Context
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtRecordOrder :TextView
        val txtRecordTime :TextView
        val txtRecordValue:TextView
        init {
            txtRecordOrder = view.findViewById(R.id.txt_record_order)
            txtRecordTime = view.findViewById(R.id.txt_record_time)
            txtRecordValue = view.findViewById(R.id.txt_record_value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_list_item, parent, false)

        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subItem = subItemList[position]
        val order = subItem.order
        val time = subItem.time
        val value = subItem.value

        val txtRecordOrder :TextView = holder.txtRecordOrder
        val txtRecordTime :TextView = holder.txtRecordTime
        val txtRecordValue:TextView = holder.txtRecordValue

        txtRecordOrder.text = order
        txtRecordTime.text = time
        txtRecordValue.text = value
    }

    override fun getItemCount() = subItemList.size

}