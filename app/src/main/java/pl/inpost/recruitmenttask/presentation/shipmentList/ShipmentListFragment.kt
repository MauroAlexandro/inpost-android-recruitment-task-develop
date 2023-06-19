package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.databinding.FragmentShipmentListBinding
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.presentation.shipmentList.adapters.Header
import pl.inpost.recruitmenttask.presentation.shipmentList.adapters.ShipmentsAdapter

@AndroidEntryPoint
class ShipmentListFragment : Fragment() {

    private val viewModel: ShipmentListViewModel by viewModels()
    private var binding: FragmentShipmentListBinding? = null
    private lateinit var shipmentsAdapter: ShipmentsAdapter
    private var shipmentsList : MutableList<Any> = ArrayList()
    private var readyToPickupShipmentsList : MutableList<Any> = ArrayList()
    private var remainingShipmentsList : MutableList<Any> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shipment_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ready_to_pickup_shipments_menu -> {
                // Handle Share item selection
                shipmentsAdapter.setItems(readyToPickupShipmentsList)
                return true
            }
            R.id.other_shipments_menu -> {
                // Handle Settings item selection
                shipmentsAdapter.setItems(remainingShipmentsList)
                return true
            }
            R.id.all_shipments_menu -> {
                shipmentsAdapter.setItems(shipmentsList)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShipmentListBinding.inflate(inflater, container, false)
        return requireNotNull(binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(requireActivity()) { shipments ->
            if (shipments.isNotEmpty()) {
                shipmentsList = getShipmentsListWithHeaders(shipments)
                shipmentsAdapter = context?.let { ShipmentsAdapter(it, shipmentsList) }!!
                binding?.shipmentsRecyclerview?.adapter = shipmentsAdapter
                binding?.shipmentsRecyclerview?.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            getShipmentsFromService()
            binding?.swipeRefresh?.isRefreshing = false
        }
    }

    /**
     * On Swipe to Refresh It calls refreshData from ViewModel
     */
    private fun getShipmentsFromService() {
        viewModel.refreshData()
    }

    /**
     * Add the Headers for the List
     * - Ready for Pick up
     * - Remaining
     */
    private fun getShipmentsListWithHeaders(shipments: List<ShipmentNetwork>): MutableList<Any> {
        shipmentsList = ArrayList()
        shipmentsList.add(Header(getString(R.string.status_ready_to_pickup)))
        readyToPickupShipmentsList = shipments.filter { it.operations.highlight }.toMutableList()
        shipmentsList.addAll(readyToPickupShipmentsList)
        shipmentsList.add(Header(getString(R.string.the_remaining)))
        remainingShipmentsList = shipments.filter { !it.operations.highlight }.toMutableList()
        shipmentsList.addAll(remainingShipmentsList)
        return shipmentsList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = ShipmentListFragment()
    }
}
