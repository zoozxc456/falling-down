package com.example.fallingdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class CompleteAddFriendFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.fragment_complete_add_friend, container, false)
        view.findViewById<Button>(R.id.btn_complete_add_friend_accept).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
        return view
    }

}