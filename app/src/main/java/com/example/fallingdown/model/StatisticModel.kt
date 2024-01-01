package com.example.fallingdown.model

import java.util.Date

data class Statistic(val userId:String, val date: Date,val counter:Int)

data class StatisticResponseModel(val data:List<Statistic>)
