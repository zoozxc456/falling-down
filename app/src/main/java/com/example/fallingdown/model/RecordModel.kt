package com.example.fallingdown.model

data class RecordModel(val date: String, val subItemList: List<SubItemModel>)
data class SubItemModel(val order: String, val time: String)

data class RecordResponseModel(val records:List<RecordModel>)

object SampleData {

    private val subItemList = listOf(
        SubItemModel("#1", "10:24"),
        SubItemModel("#3", "10:26"),
        SubItemModel("#2", "10:25"),
        SubItemModel("#4", "10:27"),
        SubItemModel("#5", "10:28"),
    )

    val records = listOf(
        RecordModel("2023/05/01", subItemList),
        RecordModel("2023/05/02", subItemList),
        RecordModel("2023/05/03", subItemList),
        RecordModel("2023/05/04", subItemList),
        RecordModel("2023/05/05", subItemList),
        RecordModel("2023/05/06", subItemList),
        RecordModel("2023/05/07", subItemList),
        RecordModel("2023/05/08", subItemList),
        RecordModel("2023/05/09", subItemList),
        RecordModel("2023/05/10", subItemList),
        RecordModel("2023/05/11", subItemList),
        RecordModel("2023/05/12", subItemList),
        RecordModel("2023/05/13", subItemList),
        RecordModel("2023/05/14", subItemList),
        RecordModel("2023/05/15", subItemList),
        RecordModel("2023/05/16", subItemList),
        RecordModel("2023/05/17", subItemList),
        RecordModel("2023/05/18", subItemList),
    ).reversed()
}
