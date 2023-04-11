package com.example.rallyapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rallyapp.activities.UserActivity
import com.example.rallyapp.databinding.FragmentHeaderBinding
import com.example.rallyapp.user.UserCredentials

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TAG_LINE = "tag"


/**
 * A simple [Fragment] subclass.
 * Use the [HeaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HeaderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var tagText: String? = null
    private var _binding: FragmentHeaderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tagText = it.getString(TAG_LINE)
            println(it.getString(TAG_LINE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* binding */
        _binding = FragmentHeaderBinding.inflate(inflater, container, false)
        val view = binding.root

        val greetingsText: String
        if(UserCredentials.isUserSet()){
            greetingsText = "Hello ${UserCredentials.getUserName()!!}"
        }else{
            greetingsText = "Hello User"
        }
        binding.greeting.text = greetingsText
        /* setting the text to tagLine */
        binding.tagLine.text = tagText

        binding.userProfileButton.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment Header.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(text: String) =
            HeaderFragment().apply {
                arguments = Bundle().apply {
                    putString(TAG_LINE, text)
                }
            }
    }
}