package com.example.fallingdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ConfirmAddFriendFragment : Fragment() {
    private val _completeAddFriendFragment: CompleteAddFriendFragment = CompleteAddFriendFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.fragment_confirm_add_friend, container, false)
        view.findViewById<Button>(R.id.btn_confirm_add_friend_accept).setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.beginTransaction()
                .replace(R.id.friend_fragment_container, _completeAddFriendFragment)
                .addToBackStack(null).commit()
        }
        view.findViewById<Button>(R.id.btn_confirm_add_friend_reject).setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.beginTransaction()
                .replace(R.id.friend_fragment_container, ScanQrCodeFragment()).addToBackStack(null)
                .commit()
        }
        return view
    }

}