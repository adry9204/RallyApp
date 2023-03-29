package com.example.rallyapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rallyapp.R
import com.example.rallyapp.activities.CartActivity
import com.example.rallyapp.activities.HomeActivity
import com.example.rallyapp.activities.SearchActivity
import com.example.rallyapp.activities.UserActivity
import com.example.rallyapp.databinding.FragmentBottomNavBinding
import com.example.rallyapp.databinding.FragmentHeaderBinding


/**
 * A simple [Fragment] subclass.
 * Use the [BottomNavFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomNavFragment : Fragment() {
    private var _binding: FragmentBottomNavBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomNavBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.homeNavButton.setOnClickListener{
            val homeActivityIntent = Intent(context, HomeActivity::class.java)
            startActivity(homeActivityIntent)
        }

        binding.cartNavButton.setOnClickListener{
            val cartActivity = Intent(context, CartActivity::class.java)
            startActivity(cartActivity)
        }

        binding.profileNavButton.setOnClickListener{
            val userActivityIntent = Intent(context, UserActivity::class.java)
            startActivity(userActivityIntent)
        }

        binding.searchNavButton.setOnClickListener{
            val searchActivityIntent = Intent(context, SearchActivity::class.java)
            startActivity(searchActivityIntent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BottomNavFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance() =
                BottomNavFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}