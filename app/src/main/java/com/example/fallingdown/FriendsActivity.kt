package com.example.fallingdown

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fallingdown.model.FriendListModel
import com.example.fallingdown.model.FriendModel
import com.example.fallingdown.service.FriendshipService
import retrofit2.Call
import retrofit2.Response

class FriendsActivity : AppCompatActivity() {
    private val scanQrCodeFragment = ScanQrCodeFragment()
    private lateinit var _context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        _context = this

        val backToPreviousButton = findViewById<ImageView>(R.id.btn_friend_back_page)
        backToPreviousButton.setOnClickListener {
            finish()
        }

        val displayQRCodeButton = findViewById<ImageView>(R.id.btn_display_qrcode)
        displayQRCodeButton.setOnClickListener {
            startActivity(Intent(this, DisplayQRCodeActivity::class.java))
        }

        val scanQrCordButton = findViewById<ImageView>(R.id.btn_scan_qrcode)
        scanQrCordButton.setOnClickListener {
            showQrCodeScanner()
        }

        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sp.getString("acc", "")

        FriendshipService.retrofitService.getFriendships(userId!!)
            .enqueue(object : retrofit2.Callback<List<FriendModel>> {
                override fun onResponse(
                    call: Call<List<FriendModel>>,
                    response: Response<List<FriendModel>>
                ) {
                    val friends = response.body()
                    val friendList: ArrayList<FriendListModel> = ArrayList()

                    var resize = when (friends!!.size!!.mod(2)) {
                        0 -> ((friends!!.size!!) / 2)
                        else -> ((friends!!.size!! + 1) / 2)
                    }
                    for (index in 0 until resize) {
                        Log.d("friend", index.toString())
                        val left = FriendModel(username = friends[index * 2].username)
                        val right =
                            if (index * 2 + 1 < friends!!.size!!) FriendModel(username = friends[index * 2 + 1].username) else null
                        friendList.add(FriendListModel(left, right))
                    }

                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_friends)
                    val layoutManager = LinearLayoutManager(_context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = FriendsListAdapter(friendList)

                    Log.d("friend", response.body().toString())
                }

                override fun onFailure(call: Call<List<FriendModel>>, t: Throwable) {
                }
            })
    }

    private fun showQrCodeScanner() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.friend_fragment_container, scanQrCodeFragment).addToBackStack(null)
        transaction.commit()
    }

    class FriendsListAdapter(private val friends: ArrayList<FriendListModel>) :
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
