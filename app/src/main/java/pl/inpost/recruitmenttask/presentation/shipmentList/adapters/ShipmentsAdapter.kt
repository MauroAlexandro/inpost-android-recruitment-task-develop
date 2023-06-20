package pl.inpost.recruitmenttask.presentation.shipmentList.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.util.Utils

class ShipmentsAdapter (
    private val context: Context,
    private var itemsList: List<Any>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    private var headersSize = 0
    private var delivery = "OUT_FOR_DELIVERY"
    private var ready = "READY_TO_PICKUP"

    override fun getItemCount(): Int {
        val totalListSize = itemsList.size
        headersSizeUpdate()

        return totalListSize - headersSize //Remove headers from count
    }

    private fun headersSizeUpdate() {
        headersSize = itemsList.count { it is Header }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemsList[position] is Header){
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(context).inflate(R.layout.shipment_header_layout, parent, false)
            SectionTitleViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.shipment_item_layout, parent, false)
            SectionContentViewHolder(view)
        }
    }

    fun setItems(newItemsList: List<Any>){
        itemsList = newItemsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList[position]

        if(holder is SectionTitleViewHolder){
            val header = (item as Header)
            holder.sectionTitleTextView.text = header.headerTitle
        } else if (holder is SectionContentViewHolder){
            val shipment = item as ShipmentNetwork
            //Since 'Design is not important here' I've let It take an empty space, but ideal was to remove from the list
            //This way you can also see that that item is hidden because is archived
            if(Utils.shipmentIsArchived(context, shipment.number)) {
                holder.shipmentItemLayout.visibility = GONE
                item.operations.manualArchive = true
            } else {
                holder.shipmentItemLayout.visibility = VISIBLE
            }

            holder.shipmentNumber.text = shipment.number
            holder.status.text = context.getString(shipment.status.nameRes)
            holder.sender.text = shipment.sender?.email

            if(shipment.status.name == delivery) {
                holder.statusImage.setBackgroundResource(R.mipmap.out_for_delivery)
                holder.statusImage.layoutParams.width = context.resources.getDimension(R.dimen.item_out_for_delivery_status_image_width).toInt()
                holder.statusImage.layoutParams.height = context.resources.getDimension(R.dimen.item_out_for_delivery_status_image_height).toInt()
            } else {
                holder.statusImage.setBackgroundResource(R.mipmap.waiting)
                holder.statusImage.layoutParams.width = context.resources.getDimension(R.dimen.item_status_image_width).toInt()
                holder.statusImage.layoutParams.height = context.resources.getDimension(R.dimen.item_status_image_height).toInt()
            }

            if(shipment.status.name == ready) {
                holder.readyTitle.visibility = VISIBLE
                holder.readyTitle.text = context.getString(shipment.status.nameRes)
                holder.ready.visibility = VISIBLE
                holder.ready.text = Utils.convertDate(shipment.storedDate)
            } else {
                holder.readyTitle.visibility = INVISIBLE
                holder.ready.visibility = INVISIBLE
            }

            holder.shipmentItemLayout.setOnLongClickListener {
                Utils.archiveShipmentsOnSharedPreferences(context, shipment.number)
                item.operations.manualArchive = true
                notifyItemChanged(position)
                true
            }
        }
    }
}

class SectionTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val sectionTitleTextView: TextView = view.findViewById(R.id.section_title)
}

class SectionContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val shipmentItemLayout: ConstraintLayout = view.findViewById(R.id.shipment_item_layout)
    val shipmentNumber: TextView = view.findViewById(R.id.shipment_number)
    val status: TextView = view.findViewById(R.id.status)
    val statusImage: ImageView = view.findViewById(R.id.shipment_status_image)
    val sender: TextView = view.findViewById(R.id.sender)
    val readyTitle: TextView = view.findViewById(R.id.ready_title)
    val ready: TextView = view.findViewById(R.id.ready)
}