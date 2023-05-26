package com.example.fallingdown

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ActionMenuView.LayoutParams
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior


class LandingActivity : AppCompatActivity() {
    private val SERVICE_STATUS_LABELS = arrayOf("快打開，手機現在不安全！", "手機被保護囉")
    private lateinit var loginBottomSheet:BaseBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val serviceSP = getSharedPreferences("service", Context.MODE_PRIVATE)
        var serviceStatus = serviceSP.getBoolean("status", false)

        val powerButton: Button = findViewById(R.id.btn_power)
        powerButton.setBackgroundResource(R.drawable.power_switch_off)
        val statusTextView: TextView = findViewById(R.id.txt_falling_down_service_status)
        statusTextView.text =
            if (serviceStatus) SERVICE_STATUS_LABELS[1] else SERVICE_STATUS_LABELS[0]

        powerButton.setOnClickListener {
            serviceStatus = !serviceStatus
            powerButton.setBackgroundResource(if (serviceStatus) R.drawable.power_switch else R.drawable.power_switch_off)
            serviceSP.edit().putBoolean("status", serviceStatus).apply()
            statusTextView.text =
                if (serviceStatus) SERVICE_STATUS_LABELS[1] else SERVICE_STATUS_LABELS[0]
        }

        val loginView = findViewById<View>(R.id.login_bottom_sheet)
        val closeLoginViewBtn = findViewById<Button>(R.id.btn_close_loginView)
         loginBottomSheet = BaseBottomSheet(loginView, closeLoginViewBtn)
        val loginBtn = findViewById<Button>(R.id.btn_login).setOnClickListener {
            val loginBottomSheetBehavior = loginBottomSheet.behavior
            loginBottomSheetBehavior.isHideable = true
            loginBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            sp.edit().putString("acc", "demo").apply()
        }

        val applicationsView = findViewById<View>(R.id.user_application_bottom_sheet)
        val closeApplicationsViewBtn = findViewById<Button>(R.id.btn_close_user_application)
        val applicationsBottomSheet = BaseBottomSheet(applicationsView, closeApplicationsViewBtn)

        applicationsBottomSheet.view.findViewById<FrameLayout>(R.id.btn_record).setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }

        applicationsBottomSheet.view.findViewById<FrameLayout>(R.id.btn_rank).setOnClickListener {
            startActivity(Intent(this, RankActivity::class.java))
        }

        applicationsBottomSheet.view.findViewById<FrameLayout>(R.id.btn_friends)
            .setOnClickListener {
                startActivity(Intent(this, FriendsActivity::class.java))
            }

        applicationsBottomSheet.view.findViewById<FrameLayout>(R.id.btn_profile)
            .setOnClickListener {
                startActivity(Intent(this, ProfileActivity::class.java))
            }

        val chart = findViewById<BarChart>(R.id.barChart)
        val barChart = StatisticsChart(chart)
        barChart.createBarChart()
    }

    override fun onResume() {
        super.onResume()
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val acc = sp.getString("acc", "")

        if (acc.isNullOrEmpty()) {
            val applicationsView = findViewById<View>(R.id.user_application_bottom_sheet)
            val closeApplicationsViewBtn = findViewById<Button>(R.id.btn_close_user_application)
            val applicationsBottomSheet =
                BaseBottomSheet(applicationsView, closeApplicationsViewBtn)
            val applicationsBottomSheetBehavior: BottomSheetBehavior<View> =
                applicationsBottomSheet.behavior
            applicationsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            val loginView = findViewById<View>(R.id.login_bottom_sheet)
            val closeLoginViewBtn = findViewById<Button>(R.id.btn_close_loginView)
            closeLoginViewBtn.visibility = View.INVISIBLE

            val loginBottomSheetBehavior = loginBottomSheet.behavior
            loginBottomSheetBehavior.isHideable = false
            loginBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        Log.d("s", "hello")
    }
}

private open class BaseBottomSheet(open val view: View, open val closeViewBtn: Button) {
    val behavior = BottomSheetBehavior.from(view)

    init {
        updateBottomSheetViewHeight()
        updateCloseViewBtnVisibility()
        implementCloseViewBtnOnClickListener()
    }

    private fun updateBottomSheetViewHeight() {
        view.post {
            val height = view.height
            val params = view.layoutParams
            val offsetOfParent = 50

            params.height = (height - offsetOfParent)
            params.width = LayoutParams.MATCH_PARENT
            view.layoutParams = params
        }
    }

    private fun updateCloseViewBtnVisibility() {
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    closeViewBtn.visibility = View.INVISIBLE
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    closeViewBtn.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun implementCloseViewBtnOnClickListener() {
        closeViewBtn.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}

private class StatisticsChart(val chart: BarChart) {

    private fun prepareFakeEntries(): ArrayList<BarEntry> {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 3f))
        entries.add(BarEntry(1f, 5f))
        entries.add(BarEntry(2f, 4f))
        entries.add(BarEntry(3f, 3f))
        entries.add(BarEntry(4f, 6f))
        return entries
    }

    private fun buildDataSet(): BarDataSet {
        val fakeEntries = prepareFakeEntries()
        val dataSet = BarDataSet(fakeEntries, "")
        dataSet.valueTextSize = 17.0f
        dataSet.valueFormatter = DefaultValueFormatter(0)
        dataSet.color = Color.parseColor("#4DC3A7")
        dataSet.setDrawValues(false)
        return dataSet
    }

    private fun buildData(): BarData {
        val data = BarData()
        val dataSet = buildDataSet()
        data.addDataSet(dataSet)
        return data
    }

    fun createBarChart() {
        val data = buildData()
        chart.data = data
        chart.axisRight.isEnabled = false
        chart.xAxis.isEnabled = false
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.isDoubleTapToZoomEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(false)
        chart.setBackgroundColor(Color.parseColor("#FFFFFF"))
        chart.invalidate()
    }
}