package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : Fragment(R.layout.fragment_input2) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val et: EditText = view.findViewById(R.id.editText)
        val tv: TextView = view.findViewById(R.id.testTextView)
        et.setOnEditorActionListener { textView, i, keyEvent ->
            tv.text = et.text
            true
        }

        val confirmBtn: Button? = view.findViewById(R.id.confirmButton)
        if (confirmBtn != null) {
            confirmBtn.setOnClickListener {
                // result API를 사용하여 fragment 간 데이터 전달하기
                val result = et.text.toString()
                Log.d("SANHA", "InputFragment onViewCreated - send result $result");
                setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                // result 보내고 난 후 fragment 닫기
                parentFragmentManager.beginTransaction()
                    .remove(this)
                    .commit()
            }
        }
        else{
            Log.d("SANHA", "InputFragment - confirmBtn is null");
        }

        val cancleBtn: ImageButton = view.findViewById(R.id.cancleButton)
        cancleBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InputFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}