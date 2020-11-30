package me.jalxp.easylist.ui.measurementUnits


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.MeasurementUnitsListAdapter
import me.jalxp.easylist.data.entities.MeasureUnit
import me.jalxp.easylist.databinding.FragmentMeasurementUnitsBinding


class MeasurementUnitsFragment : Fragment() {

    private lateinit var binding: FragmentMeasurementUnitsBinding
    private val measurementUnitsViewModel: MeasurementUnitsViewModel by activityViewModels {
        MeasurementUnitsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* View Binding */
        binding = FragmentMeasurementUnitsBinding.inflate(inflater, container, false)

        /* Recycle View */
        val measurementUnitsListAdapter = MeasurementUnitsListAdapter(
            { measurementUnit -> adapterOnItemClick(measurementUnit) },
            { measurementUnit -> adapterOnEditClick(measurementUnit) },
            { measurementUnit -> adapterOnDeleteClick(measurementUnit) }
        )

        with(binding) {
            measurementUnitsRecycleView.adapter = measurementUnitsListAdapter
            measurementUnitsRecycleView.layoutManager =
                LinearLayoutManager(
                    this@MeasurementUnitsFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }

        measurementUnitsViewModel.measurementUnitsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                measurementUnitsListAdapter.submitList(it as MutableList<MeasureUnit>)
            }
        })

        /* Floating Button */
        binding.createMeasurementUnitFab.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_nav_measurementUnits_to_addMeasurementUnitFragment)
        }

        return binding.root
    }

    private fun adapterOnItemClick(measureUnit: MeasureUnit) {
        // TODO
    }

    private fun adapterOnEditClick(measureUnit: MeasureUnit) {
        // TODO
    }

    private fun adapterOnDeleteClick(measureUnit: MeasureUnit) {
        // TODO
    }
}