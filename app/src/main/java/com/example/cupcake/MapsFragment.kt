package com.example.cupcake

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cupcake.calculations.TravelFunctions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    //"activityViewModels()" property delegation to call methods
    private val travelView: TravelFunctions by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()
        map_view.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map?.let {
            googleMap = it

            // Add a marker in start and end
            val start = LatLng(travelView.latValStart.value!!, travelView.longValStart.value!!)
            val end = LatLng(travelView.latValEnd.value!!, travelView.longValEnd.value!!)
            googleMap.addMarker(MarkerOptions().position(start).title(travelView.start.value!!))
            googleMap.addMarker(MarkerOptions().position(end).title(travelView.end.value!!))

            //Add polyline, zoom, and set text
            googleMap.addPolyline(PolylineOptions().add(start).add(end)
                .width(8f).color(Color.RED))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(start))
            distance_label.text = travelView.distData.value!!

            //Add circles at start and end
            googleMap.addCircle(CircleOptions().center(start).radius(500.0)
                .strokeWidth(3f).strokeColor(Color.RED)
                .fillColor(Color.argb(70, 150, 50, 50)))
            googleMap.addCircle(CircleOptions().center(end).radius(500.0)
                .strokeWidth(3f).strokeColor(Color.RED)
                .fillColor(Color.argb(70, 150, 50, 50)))

            //Enable compass, zoom, and rotate
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

}
