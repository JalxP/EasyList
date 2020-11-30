package me.jalxp.easylist.ui.measurementUnits


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentAddMeasurementUnitBinding


class AddMeasurementUnitFragment : Fragment() {

    private lateinit var binding: FragmentAddMeasurementUnitBinding
    private val measurementUnitsViewModel: MeasurementUnitsViewModel by activityViewModels {
        MeasurementUnitsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMeasurementUnitBinding.inflate(inflater, container, false)

        binding.addMeasurementButton.setOnClickListener {
            addMeasurementUnit()
        }

        return binding.root
    }

    private fun addMeasurementUnit() {
        val measurementDesignation = binding.addMeasurementTitle.text.toString()
        when {
            measurementDesignation.isEmpty() -> {
                binding.measurementTextLayout.error = getString(R.string.need_designation_error)
                return

            }
            measureAlreadyExists(measurementDesignation) -> {
                binding.measurementTextLayout.error = getString(R.string.measurement_already_exists_error)
                return
            }
            else -> {
                measurementUnitsViewModel.insertNewMeasurementUnit(measurementDesignation)
                findNavController().navigate(R.id.action_addMeasurementUnitFragment_to_nav_measurementUnits)
            }
        }
    }

    private fun measureAlreadyExists(measurementDesignation: String) : Boolean {
        val measures =  measurementUnitsViewModel.measurementUnitsLiveData.value

        measures?.forEach {
            if (it.designation.equals(measurementDesignation,true))
                return true
        }
        return false
    }
}