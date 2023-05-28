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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fallingdown.model.RankModel
import com.example.fallingdown.service.RankService
import retrofit2.Call
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class RankActivity : AppCompatActivity() {
    private lateinit var _context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        _context = this
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val userId = sp.getString("acc", "")
        val username = sp.getString("username","")

        findViewById<ImageView>(R.id.btn_rank_back_page).setOnClickListener {
            finish()
        }

        RankService.retrofitService.getRecordsByUserId(userId!!)
            .enqueue(object : retrofit2.Callback<List<RankModel>> {
                override fun onResponse(
                    call: Call<List<RankModel>>,
                    response: Response<List<RankModel>>
                ) {
                    val rank: List<RankModel> = response.body()!!

                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_rank)
                    val layoutManager = LinearLayoutManager(_context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = RankAdapter(rank,username!!)

                    Log.d("rank", response.body().toString())
                }

                override fun onFailure(call: Call<List<RankModel>>, t: Throwable) {

                }

            })
    }

    class RankAdapter(private val rank: List<RankModel>,private val username:String) :
        RecyclerView.Adapter<RankAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val badgeImageView: ImageView
            val badgeTextView: TextView
            val usernameTextView: TextView
            val frequencyTextView: TextView
            val cardViewContainer: CardView

            init {
                badgeImageView = view.findViewById(R.id.iv_rank_badge)
                badgeTextView = view.findViewById(R.id.txt_rank_badge)
                usernameTextView = view.findViewById(R.id.txt_rank_username)
                frequencyTextView = view.findViewById(R.id.txt_rank_frequency)
                cardViewContainer = view.findViewById(R.id.recyclerViewContent_rank_cardView)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_rank, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = rank.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val rank = rank[position]
            val badgeImageView: ImageView = holder.badgeImageView
            val badgeTextView: TextView = holder.badgeTextView
            val usernameTextView: TextView = holder.usernameTextView
            val frequencyTextView: TextView = holder.frequencyTextView
            val cardViewContainer: CardView = holder.cardViewContainer
            setBadgeImage(badgeImageView, position)

            val isBadgeTextVisible = position >= 3
            if (isBadgeTextVisible) {
                setBadgeText(badgeTextView, position)
            }
            setBadgeTextVisibility(badgeTextView, isBadgeTextVisible)

            setUsername(usernameTextView, rank.username)
            setFrequencyValue(frequencyTextView, rank.frequency)

//            val MY_USERNAME = "Ben"
            if (rank.username.trim() == username) {
                cardViewContainer.setCardBackgroundColor(Color.parseColor("#FFD25E"))
                badgeImageView.setImageResource(R.drawable.myself_badge)
            }
        }

        fun setBadgeImage(imageView: ImageView, position: Int) {
            val badgeResource = when (position) {
                0 -> R.drawable.gold_badge
                1 -> R.drawable.silver_badge
                2 -> R.drawable.cooper_badge
                else -> R.drawable.default_badge
            }
            imageView.setImageResource(badgeResource)
        }

        fun setBadgeTextVisibility(textView: TextView, isVisible: Boolean) {
            textView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        }

        fun setBadgeText(textView: TextView, position: Int) {
            textView.text = position.toString()
        }

        fun setUsername(textView: TextView, username: String) {
            textView.text = username
        }

        fun setFrequencyValue(textView: TextView, frequency: Double) {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.DOWN
            val roundOff = df.format(frequency)
            textView.text = roundOff.toString()
        }
    }
}