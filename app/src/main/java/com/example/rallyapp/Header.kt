package com.example.rallyapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.rallyapp.databinding.FragmentHeaderBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TAG_LINE = "tag"


/**
 * A simple [Fragment] subclass.
 * Use the [Header.newInstance] factory method to
 * create an instance of this fragment.
 */
class Header : Fragment() {
    // TODO: Rename and change types of parameters
    private var tagText: String? = null
    private lateinit var binding: FragmentHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tagText = it.getString(TAG_LINE)
            println(it.getString(TAG_LINE))
        }
        binding = FragmentHeaderBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflater = inflater.inflate(R.layout.fragment_header, container, false)
        val tag = inflater.findViewById<TextView>(R.id.tag_line)
        tag.text = tagText
        return inflater
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
            Header().apply {
                arguments = Bundle().apply {
                    putString(TAG_LINE, text)
                }
            }
    }
}