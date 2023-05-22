package com.example.fallingdown.model

data class FriendModel(val username: String)

data class FriendListModel(val leftItem: FriendModel, val rightItem: FriendModel?)

object SampleFriendData {
    val friends = listOf(
        FriendListModel(FriendModel("Ruby"), FriendModel("Stanny")),
        FriendListModel(FriendModel("Cloris"), FriendModel("Sky")),
        FriendListModel(FriendModel("Cloris"),null)
    )
}
