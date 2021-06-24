package com.example.cupcake.calculations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TravelFunctions: ViewModel() {
    //Starting point
    private var _start = MutableLiveData<String>()
    val start: LiveData<String> = _start

    //End point
    private var _end = MutableLiveData<String>()
    val end: LiveData<String> = _end

    //Start latitude value
    private var _latValStart = MutableLiveData(0.0)
    val latValStart: LiveData<Double>
        get() = _latValStart

    //End latitude value
    private var _latValEnd = MutableLiveData(0.0)
    val latValEnd: LiveData<Double>
        get() = _latValEnd

    //Start longitude value
    private var _longValStart = MutableLiveData(0.0)
    val longValStart: LiveData<Double>
        get() = _longValStart

    //End longitude value
    private var _longValEnd = MutableLiveData(0.0)
    val longValEnd: LiveData<Double>
        get() = _longValEnd

    //Latitude of start
    private var _latStart = MutableLiveData<String>()
    val latStart: LiveData<String> = _latStart

    //Latitude of end
    private var _latEnd = MutableLiveData<String>()
    val latEnd: LiveData<String> = _latEnd

    //Longitude of start
    private var _longStart = MutableLiveData<String>()
    val longStart: LiveData<String> = _longStart

    //Longitude of end
    private var _longEnd = MutableLiveData<String>()
    val longEnd: LiveData<String> = _longEnd

    //Coordinates of start
    private var _coordStart = MutableLiveData<String>()
    val coordStart: LiveData<String> = _coordStart

    //Coordinates of end
    private var _coordEnd = MutableLiveData<String>()
    val coordEnd: LiveData<String> = _coordEnd

    //Unit of measurement
    private var _unit = MutableLiveData<String>("Metric")
    val unit: LiveData<String> = _unit

    //Travel distance
    private var _dist = MutableLiveData<String>()
    val dist: LiveData<String> = _dist

    //Distance data
    private var _distData = MutableLiveData<String>()
    val distData: LiveData<String> = _distData

    //Constructor to begin
    init {
        reset()
    }

    //Reset values
    fun reset() {
        _start.value = ""
        _end.value = ""

        _latValStart.value = 0.0
        _latValEnd.value = 0.0
        _longValStart.value = 0.0
        _longValEnd.value = 0.0

        _latStart.value = ""
        _latEnd.value = ""
        _longStart.value = ""
        _longEnd.value = ""

        _coordStart.value = ""
        _coordEnd.value = ""

        _unit.value = "Metric"
        _dist.value = ""
        _distData.value = ""
    }

    fun startPoint(start: String) {
        _start.value = start
    }

    fun endPoint(end: String) {
        _end.value = end
    }

    fun noTextCoord(spot: String) {
        if (spot == "Start") {
            _start.value = ""
            _latStart.value = ""
            _longStart.value = ""
            _coordStart.value = ""
        }
        if (spot == "End")
            _dist.value = ""
    }

    fun coord(lat: Double, long: Double, spot: String) {
        var latVal = Math.round(lat * 10.0) / 10.0
        var longVal = Math.round(long * 10.0) / 10.0

        //Starting coordinates
        if (spot == "Start") {
            //Set values
            _latValStart.value = latVal
            _longValStart.value = longVal

            //Latitude of start
            if (latVal < 0.0)
                _latStart.value = Math.abs(latVal).toString() + "\u00B0 S"
            else
                _latStart.value = latVal.toString() + "\u00B0 N"

            //Longitude of start
            if (longVal < 0.0)
                _longStart.value = Math.abs(longVal).toString() + "\u00B0 W"
            else
                _longStart.value = longVal.toString() + "\u00B0 E"

            //Coordinates
            _coordStart.value = "Start Coordinates: \n    Latitude: " + _latStart.value + "\n    Longitude: " + _longStart.value
        }
        //End coordinates
        if (spot == "End") {
            //Set values
            _latValEnd.value = latVal
            _longValEnd.value = longVal

            //Latitude of end
            if (latVal < 0.0)
                _latEnd.value = Math.abs(latVal).toString() + "\u00B0 S"
            else
                _latEnd.value = Math.abs(latVal).toString() + "\u00B0 N"

            //Longitude of end
            if (longVal < 0.0)
                _longEnd.value = Math.abs(longVal).toString() + "\u00B0 W"
            else
                _longEnd.value = Math.abs(longVal).toString() + "\u00B0 E"

            //Coordinates
            _coordEnd.value = "End Coordinates: \n    Latitude: " + _latEnd.value + "\n    Longitude: " + _longEnd.value
        }
    }

    //Unit of measurement
    fun unit(unit: String) {
        _unit.value = unit
        noTextCoord("End")
    }

    private fun travelDist(dist: Double) {
        var distVal = 0.0
        var unit = ""

        //Distance based on imperial or metric
        if (_unit.value!! == "Imperial") {
            unit = " mi"
            var distConv = 0.621 * dist
            distVal = Math.abs(String.format("%.2f", distConv).toDouble())
        }
        if (_unit.value!! == "Metric") {
            unit = " km"
            distVal = Math.abs(String.format("%.2f", dist).toDouble())
        }
        //Distance and distance data
        val dist = distVal.toString() + unit
        _dist.value = "Travel Distance: " + dist
        _distData.value = _start.value + " to " + _end.value + ": " + dist
    }

    fun validLoc(loc: String): Boolean {
        //Null if empty
        if (loc.isNullOrEmpty())
            return false

        if (loc[0] == ' ' || loc[loc.length - 1] == ' ')
            return false

        //Check for null input
        for (i in 1..loc.length-1) {
            if (loc[i] == ' ' && loc[i - 1] == ' ')
                return false;
        }
        return true;
    }

    fun distance() {
        //Travel distance
        if (_latValStart.value != null && _latValEnd.value != null
            && _longValStart.value != null && _longValEnd.value != null) {

            //Radius and distances to and from with lat and long
            var r = 6371
            var dLat = degToRad(_latValEnd.value!! - _latValStart.value!!)
            var dLong = degToRad(_longValEnd.value!! - _longValStart.value!!)

            //Distance to destination
            var dist = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(degToRad(_latValStart.value!!)) *
                    Math.cos(degToRad(_latValEnd.value!!)) * Math.sin(dLong / 2) * Math.sin(dLong / 2)
            dist = 2 * Math.atan2(Math.sqrt(dist), Math.sqrt(1 - dist))
            dist = r * dist
            travelDist(dist)
        }
    }
    //Convert degrees to radians
    private fun degToRad(value: Double): Double{
        return (value * Math.PI / 180.0)
    }
}