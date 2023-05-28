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
import com.example.fallingdown.model.LoginRequestModel
import com.example.fallingdown.model.LoginResponseModel
import com.example.fallingdown.model.Statistic
import com.example.fallingdown.service.LoginServiceApi
import com.example.fallingdown.service.StatisticService
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Response


class LandingActivity : AppCompatActivity() {
    private val SERVICE_STATUS_LABELS = arrayOf("快打開，手機現在不安全！", "手機被保護囉")
    private lateinit var loginBottomSheet: BaseBottomSheet
    private lateinit var applicationsBottomSheet: BaseBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chart = findViewById<BarChart>(R.id.barChart)
        val serviceSP = getSharedPreferences("service", Context.MODE_PRIVATE)
        var serviceStatus = serviceSP.getBoolean("status", false)

        val powerButton: Button = findViewById(R.id.btn_power)
        powerButton.setBackgroundResource(if (serviceStatus) R.drawable.power_switch else R.drawable.power_switch_off)

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
            val payload = LoginRequestModel(account = "Ben25", password = "Ben25")
            LoginServiceApi.retrofitService.login(payload)
                .enqueue(object : retrofit2.Callback<LoginResponseModel> {

                    override fun onResponse(
                        call: Call<LoginResponseModel>?,
                        response: Response<LoginResponseModel>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            val title =
                                applicationsBottomSheet.view.findViewById<TextView>(R.id.user_application_title)
                            title.text = "Hello, " + data?.username

                            val loginBottomSheetBehavior = loginBottomSheet.behavior
                            loginBottomSheetBehavior.isHideable = true
                            loginBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                            val sp =
                                getSharedPreferences(
                                    "mySharedPreferences",
                                    Context.MODE_PRIVATE
                                )
                            sp.edit().putString("acc", data?.userId).putString("username",data?.username).apply()

                            val barChart = StatisticsChart(chart, data!!.userId,applicationsBottomSheet)
                            barChart.createBarChart()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponseModel?>?, t: Throwable?) {
                        // 連線失敗
                        Log.e("Error", t.toString())
                    }
                })
        }

        val applicationsView = findViewById<View>(R.id.user_application_bottom_sheet)
        val closeApplicationsViewBtn = findViewById<Button>(R.id.btn_close_user_application)
        applicationsBottomSheet = BaseBottomSheet(applicationsView, closeApplicationsViewBtn)

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
    }
    private class StatisticsChart(val chart: BarChart, val userId: String,val view: BaseBottomSheet) {

        private fun prepareFakeEntries(): ArrayList<BarEntry> {
            var entries = ArrayList<BarEntry>()

            val response = StatisticService.retrofitService.getStatisticAsync(userId)
                .enqueue(object : retrofit2.Callback<List<Statistic>> {

                    override fun onResponse(
                        call: Call<List<Statistic>>?,
                        response: Response<List<Statistic>>
                    ) {

                        if (response.isSuccessful) {
                            response.body()!!.forEachIndexed { index, element ->
                                entries.add(BarEntry(index.toFloat(), element.counter.toFloat()))
                            }

                            Log.d("a",response.body()!!.toString())
                            view.view.findViewById<TextView>(R.id.txt_falling_times).text = response.body()!!.lastOrNull()?.counter.toString()+"次"

                            if (response.body()!![4].counter==0){
                                view.view.findViewById<TextView>(R.id.txt_falling_percent).text = "小心囉"
                            }else{
                                view.view.findViewById<TextView>(R.id.txt_falling_percent).text =(response.body()!![4].counter/response.body()!![3].counter).toString()+"%"
                            }


                            val dataSet = BarDataSet(entries, "")
                            dataSet.valueTextSize = 17.0f
                            dataSet.valueFormatter = DefaultValueFormatter(0)
                            dataSet.color = Color.parseColor("#4DC3A7")
                            dataSet.setDrawValues(false)
                            val data = BarData()
                            data.addDataSet(dataSet)
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

                    override fun onFailure(call: Call<List<Statistic>?>?, t: Throwable?) {
                        // 連線失敗
                        Log.e("Error", t.toString())
                    }
                })

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

