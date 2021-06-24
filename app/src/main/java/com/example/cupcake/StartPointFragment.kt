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
import com.example.cupcake.databinding.FragmentStartPointBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_start_point.*
import java.util.*

class StartPointFragment : Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentStartPointBinding
    //"activityViewModels()" property delegation to call methods
    private val travelView: TravelFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartPointBinding.inflate(inflater, container, false)
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
            startPointFragment = this@StartPointFragment
        }
    }

    fun noText() {
        //If using current location
        if (currLoc.isChecked == true) {
            textField.isEnabled = false
            text_input_edit_text.isEnabled = false

            binding.textInputEditText.text = null
            binding.textField.isErrorEnabled = false
            travelView.noTextCoord("Start")
        }
        //If not using current location
        else {
            textField.isEnabled = true
            text_input_edit_text.isEnabled = true
        }
    }

    fun startCoords() {
        //Use default or custom location
        if (currLoc.isChecked == true)  {
            useDef()
        }
        else
            noUseDef()
    }

    private fun useDef() {
        try {
            //Activity and addresses
            val gc = Geocoder(activity, Locale.getDefault())
            val adds = gc.getFromLocationName("Sydney", 2)
            val add = adds.get(0)

            //Latitude and longitude
            travelView.coord(add.latitude, add.longitude, "Start")
            travelView.startPoint("Sydney (Android Studio Default)")
        }
        catch (e: Exception) {
            pleaseDef()
        }
    }

    private fun noUseDef() {
        //City name
        val city = binding.textInputEditText.text.toString()

        if (travelView.validLoc(city)) {
            setErrorField(false)
            travelView.startPoint(city)

            try {
                //Activity and addresses
                val gc = Geocoder(activity, Locale.getDefault())
                val addresses = gc.getFromLocationName(city, 2)
                val address = addresses.get(0)

                //Latitude and longitude
                val latitude = address.latitude
                val longitude = address.longitude
                travelView.coord(latitude, longitude, "Start")
            }
            catch (e: Exception) {
                setErrorField(true)
            }
        }
        else
            setErrorField(true)
    }

    fun next() {
        //Go to next if not null
        if (travelView.coordStart.value.isNullOrEmpty())
            pleaseValid()
        else
            findNavController().navigate(R.id.action_startPointFragment_to_endPointFragment)
    }

    private fun setErrorField(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField.isErrorEnabled = true
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
            .setMessage("Please find valid starting coordinates")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }

    //Show error to get a valid start
    private fun pleaseDef() {
        //Show message, and the error
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error:")
            .setMessage("Couldn't load default location")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }
}