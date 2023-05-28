package com.example.fallingdown

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fallingdown.model.*
import com.example.fallingdown.service.RecordService
import com.example.fallingdown.service.StatisticService
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import retrofit2.Call
import retrofit2.Response

class RecordActivity : AppCompatActivity() {
    private lateinit var _context :Context
//    private lateinit var _view:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        _context = this
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sp.getString("acc", "")

        val backToPreviousButton = findViewById<ImageView>(R.id.btn_record_back_page)
        backToPreviousButton.setOnClickListener {
            finish()
        }

        RecordService.retrofitService.getRecordsByUserId(userId!!)
            .enqueue(object : retrofit2.Callback<RecordResponseModel> {
                override fun onResponse(
                    call: Call<RecordResponseModel>,
                    response: Response<RecordResponseModel>
                ) {
                    val records = response.body()?.records
                    Log.d("record",response.body().toString())
                    val layoutManager = LinearLayoutManager(_context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    val recordRecyclerView = findViewById<RecyclerView>(R.id.recyclerView_record)
                    recordRecyclerView.layoutManager = layoutManager
                    recordRecyclerView.adapter = RecordAdapter(records!!)
                }

                override fun onFailure(call: Call<RecordResponseModel>, t: Throwable) {
                    Log.e("error",t.toString())
                }
            })
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
        init {
            txtRecordOrder = view.findViewById(R.id.txt_record_order)
            txtRecordTime = view.findViewById(R.id.txt_record_time)
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

        val txtRecordOrder :TextView = holder.txtRecordOrder
        val txtRecordTime :TextView = holder.txtRecordTime

        txtRecordOrder.text = order
        txtRecordTime.text = time
    }

    override fun getItemCount() = subItemList.size

}