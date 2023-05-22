package com.example.fallingdown

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fallingdown.model.FriendListModel
import com.example.fallingdown.model.SampleFriendData

class FriendsActivity : AppCompatActivity() {
    private val addNewFriendFragment = AddNewFriendFragment("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        val backToPreviousButton = findViewById<ImageView>(R.id.btn_friend_back_page)
        backToPreviousButton.setOnClickListener {
            finish()
        }

        val displayQRCodeButton = findViewById<ImageView>(R.id.btn_display_qrcode)
        displayQRCodeButton.setOnClickListener {
            startActivity(Intent(this,DisplayQRCodeActivity::class.java))
        }

        val scanQrCordButton = findViewById<ImageView>(R.id.btn_scan_qrcode)
        scanQrCordButton.setOnClickListener {
            showQrCodeScanner()
        }

        val friends = SampleFriendData.friends

        val recyclerView = this.findViewById<RecyclerView>(R.id.recyclerView_friends)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FriendsListAdapter(friends)
    }

    private fun showQrCodeScanner(){
        val fragment =addNewFriendFragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.friend_fragment_container, fragment)
        transaction.commit()
    }

    class FriendsListAdapter(private val friends: List<FriendListModel>) :
        RecyclerView.Adapter<FriendsListAdapter.ViewHolder>() {
        lateinit var context: Context

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val leftItem: Button
            val rightItem: Button

            init {
                leftItem = view.findViewById(R.id.friend_list_left_item)
                rightItem = view.findViewById(R.id.friend_list_right_item)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_friend, parent, false)
            context = parent.context
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = friends.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val friend = friends[position]

            val leftFriendItem = holder.leftItem
            val rightFriendItem = holder.rightItem
            leftFriendItem.text = friend.leftItem.username
            if (friend.rightItem == null) {
                rightFriendItem.visibility = View.INVISIBLE
            } else {
                rightFriendItem.text = friend.rightItem.username
            }
        }
    }
}
