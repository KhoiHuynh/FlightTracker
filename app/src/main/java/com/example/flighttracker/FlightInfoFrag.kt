package com.example.flighttracker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.flight_info_fragment.*


class FlightInfoFrag : Fragment() {

    companion object {
        fun newInstance() = FlightInfoFrag()
    }

    private lateinit var viewModel: FlightInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val flightinfo: String? = arguments?.getString("json")
        tv_flightInfo.text = flightinfo

        return inflater.inflate(R.layout.flight_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FlightInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
