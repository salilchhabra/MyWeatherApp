package com.example.myweatherapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.myweatherapp.R
import com.example.myweatherapp.helper.SharedPreferenceUtils
import kotlinx.android.synthetic.main.fragment_fourth.*


class FourthFragment : Fragment() {
    private var mSharedPreferenceUtils: SharedPreferenceUtils? = null
    private var switch: SwitchCompat? = null
    private var save: Button? = null
    private var userName: String? = null

    companion object {
        const val USER_NAME = "user_name"

        fun newInstance(userName: String?): FourthFragment {
            val fragment = FourthFragment()

            val bundle = Bundle().apply {
                putString(USER_NAME, userName)
            }
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)
        userName = arguments?.getString(FirstFragment.USER_NAME)

        mSharedPreferenceUtils = activity?.applicationContext?.let {
            SharedPreferenceUtils.getInstance(
                it
            )
        }
        switch = view?.findViewById(R.id.sw_degree)
        save = view?.findViewById(R.id.btn_save)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mSharedPreferenceUtils?.containsKey("Celcius")!!) {
            switch?.isChecked = mSharedPreferenceUtils?.getSharedPrefBoolean("Celcius")!!
        } else {
            switch?.isChecked = true
        }
        switch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mSharedPreferenceUtils?.putSharedPrefBoolean("Celcius", true)
            } else {
                mSharedPreferenceUtils?.putSharedPrefBoolean("Celcius", false)
            }
        }
        tv_name.text = String.format("welcome %s", userName)
        save?.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}