package com.example.cupcake

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.calculations.TravelFunctions
import com.example.cupcake.databinding.FragmentEndPointBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class EndPointFragment : Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentEndPointBinding
    //"activityViewModels()" property delegation to call methods
    private val travelView: TravelFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentEndPointBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //Sets software life cycle owner
            lifecycleOwner = viewLifecycleOwner

            //Bind view model with shared model inside layout
            travelModel = travelView

            //Binds data variable with fragment instance (XML data variable calls functions)
            endPointFragment = this@EndPointFragment
        }
    }

    fun endCoords() {
        //City name
        val city = binding.textInputEditText.text.toString()

        if (travelView.validLoc(city) && city != travelView.start.value) {
            setErrorField(false, "")
            travelView.endPoint(city)

        try {
            //Activity and addresses
            val gc = Geocoder(activity, Locale.getDefault())
            val addresses = gc.getFromLocationName(city, 2)
            val address = addresses.get(0)

            //Latitude and longitude
            val latitude = address.latitude
            val longitude = address.longitude

            //Distances and coordinate of end
            travelView.coord(latitude, longitude, "End")
            travelView.distance()
        }
        catch (e: Exception) {
            setErrorField(true, "Not")
        }

        }
        else {
            //Set errors otherwise
            if (city == travelView.start.value)
                setErrorField(true, "Same")
            else
                setErrorField(true, "Not")
        }
    }

    fun next() {
        //Go to next if not null
        if (travelView.coordEnd.value.isNullOrEmpty())
            pleaseValid()
        else
            findNavController().navigate(R.id.action_endPointFragment_to_mapsFragment)
    }

    private fun setErrorField(error: Boolean, same: String) {
        //Show error message if necessary
        if (error == true) {
            binding.textField.isErrorEnabled = true

            //Message depending on situation
            if (same == "Same")
                binding.textField.error = "Destination can't be same as starting point!"
            else
                binding.textField.error = "Enter valid location!"
        }
        //Otherwise, no error message
        else
            binding.textField.isErrorEnabled = false
    }

    //Show error to get a valid start
    private fun pleaseValid() {
        //Show message, and the error
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Invalid start:")
            .setMessage("Please find valid end coordinates")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }
}