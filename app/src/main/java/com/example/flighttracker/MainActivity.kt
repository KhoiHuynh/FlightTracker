package com.example.flighttracker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    private var button_date: Button? = null
    private var textview_date: TextView? = null
    var cal: Calendar = Calendar.getInstance()
    private val client = OkHttpClient()

    private var day: String? = null
    private var month: String? = null
    private var thisYear: String? = null
    private var min: String? = null
    private var hours: String? = null

    private var fullDateFormat: String? = null


    //data for API
    private var displayTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the references from layout file
        textview_date = this.text_view_date_1
        button_date = this.button_date_1

        textview_date!!.text = "--/--/----"

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                day = dayOfMonth.toString()
                month = (monthOfYear + 1).toString()
                thisYear = year.toString()

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()

                Log.d("KHOIKHOI", "the month is: " + month)
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener {
            DatePickerDialog(this@MainActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        button_flightTime!!.setOnClickListener{
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)
            TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                min = m.toString()
                hours = h.toString()

                if(hours?.length == 1){
                    hours = "0$hours"
                }
                if(min?.length == 1){
                    min = "0$min"
                }

                displayTime = "$hours : $min"

                tv_flightTimeDisplay!!.text = displayTime

            }),hour,minute,true).show()
        }


        //API stoof
        button_checkFlight.setOnClickListener{
            getResponseParams(day, month, thisYear, hours, min)
            val getFlightNum = et_flightNum.text.toString()
            Log.d("KhoiKhoi", "https://aviation-edge.com/v2/public/timetable?key=0ec8de-61e660&dep_schTime=$fullDateFormat&flight_iata=$getFlightNum")
            run("https://aviation-edge.com/v2/public/timetable?key=0ec8de-61e660&dep_schTime=$fullDateFormat&flight_iata=$getFlightNum")
        }

    }

    private fun run (url:String){
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("KHOIKHOI", e.toString())
            }
            override fun onResponse(call: Call, response: Response){
//                Log.d("KHOIKHOI", response.body?.string())
//                val body = response.body?.string()
//                val gson = GsonBuilder().create()
//                val flightDetails = gson.fromJson(body, Models.FlightDetails::class.java)

                val bundle = Bundle()
                bundle.putString("json", response.body?.string())
                // set Fragmentclass Arguments
                val fragobj = FlightInfoFrag()
                fragobj.arguments = bundle

                runOnUiThread {



//                    if(flightDetails.actualRunway != null){
//                        Log.d("KHOIKHOII", flightDetails.actualRunway)
//                    }
//                    if(flightDetails.estimatedRunway != null){
//                        Log.d("KHOIKHOII", flightDetails.estimatedRunway)
//                    }
//                    if(flightDetails.scheduledTime != null){
//                        Log.d("KHOIKHOII", flightDetails.scheduledTime)
//                    }
//                    if(flightDetails.status != null){
//                        Log.d("KHOIKHOII", flightDetails.status)
//                    }
//
//                    else{
//                        Log.d("KHOIKHOI", "it's fucking null")
//                    }
                }
            }
        })
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.time)
    }

    private fun getResponseParams(day:String?, month:String?, year:String?, hour:String?, min:String?){
        var monthz = month
        var dayz = day
        var hourz = hour
        var minz = min

        if(month?.length == 1){
            monthz = "0$month"
        }
        if(day?.length == 1){
            dayz = "0$day"
        }

        fullDateFormat = year + "-" + monthz + "-" + dayz + "T" + hourz + ":" + minz + ":00.000"


        Toast.makeText(this, fullDateFormat, Toast.LENGTH_LONG).show()
    }

}
