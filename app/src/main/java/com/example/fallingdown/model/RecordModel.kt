package com.example.fallingdown.model

data class RecordModel(val date: String, val subItemList: List<SubItemModel>)
data class SubItemModel(val order: String, val time: String, val value: String)

object SampleData {

    private val subItemList = listOf(
        SubItemModel("#1", "10:24", "137cm"),
        SubItemModel("#2", "10:25", "127cm"),
        SubItemModel("#3", "10:26", "117cm"),
        SubItemModel("#4", "10:27", "107cm"),
        SubItemModel("#5", "10:28", "131cm"),
    )

    val records = listOf(
        RecordModel("2023/05/01", subItemList),
        RecordModel("2023/05/03", subItemList.reversed())
    )
}
