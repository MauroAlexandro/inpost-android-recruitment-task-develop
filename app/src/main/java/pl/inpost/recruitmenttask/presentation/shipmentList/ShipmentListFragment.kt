package pl.inpost.recruitmenttask.presentation.shipmentList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.database.ShipmentsDatabase
import pl.inpost.recruitmenttask.databinding.FragmentShipmentListBinding
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.presentation.shipmentList.adapters.ShipmentsAdapter

@AndroidEntryPoint
class ShipmentListFragment : Fragment() {

    private val viewModel: ShipmentListViewModel by viewModels()
    private var binding: FragmentShipmentListBinding? = null
    private lateinit var shipmentsAdapter: ShipmentsAdapter
    private var allShipmentsListFromService : MutableList<ShipmentNetwork> = ArrayList()
    private var shipmentsByStatusList : MutableList<Any> = ArrayList()
    private lateinit var shipmentsDatabase : ShipmentsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        shipmentsDatabase = ShipmentsDatabase.getInstance(requireContext())
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shipment_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ready_to_pickup_shipments_menu -> {
                // Handle Share item selection
                shipmentsAdapter.setItems(viewModel.getReadyShipments())
                return true
            }
            R.id.other_shipments_menu -> {
                // Handle Settings item selection
                shipmentsAdapter.setItems(viewModel.getRemainingShipments())
                return true
            }
            R.id.parcel_number_menu -> {
                // Handle Settings item selection
                shipmentsAdapter.setItems(viewModel.getShipmentsByParcelNumber())
                return true
            }
            R.id.pickup_date_menu -> {
                // Handle Settings item selection
                shipmentsAdapter.setItems(viewModel.getShipmentsByPickupDate())
                return true
            }
            R.id.expire_date_menu -> {
                // Handle Settings item selection
                shipmentsAdapter.setItems(viewModel.getShipmentsByExpireDate())
                return true
            }
            R.id.stored_date_menu -> {
                // Handle Settings item selection
                shipmentsAdapter.setItems(viewModel.getShipmentsByStoredDate())
                return true
            }
            R.id.all_shipments_menu -> {
                shipmentsAdapter.setItems(shipmentsByStatusList)
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
        viewModel.setShipmentsDatabase(shipmentsDatabase)
        viewModel.viewState.observe(requireActivity()) { shipments ->
            if (shipments.isNotEmpty()) {
                allShipmentsListFromService = shipments.toMutableList()
                shipmentsByStatusList = viewModel.getShipmentsListWithHeaders(requireContext(), allShipmentsListFromService)
                shipmentsAdapter = context?.let { ShipmentsAdapter(it, shipmentsByStatusList) }!!
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = ShipmentListFragment()
    }
}
