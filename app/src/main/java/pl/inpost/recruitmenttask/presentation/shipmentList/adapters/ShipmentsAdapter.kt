package pl.inpost.recruitmenttask.presentation.shipmentList.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
            holder.sectionTitleTextView.text = (item as Header).headerTitle
        } else if (holder is SectionContentViewHolder){
            holder.shipmentNumber.text = (item as ShipmentNetwork).number
            holder.status.text = context.getString(item.status.nameRes)
            holder.sender.text = item.sender?.email

            if(item.status.name == delivery) {
                holder.statusImage.setBackgroundResource(R.mipmap.out_for_delivery)
                holder.statusImage.layoutParams.width = context.resources.getDimension(R.dimen.item_out_for_delivery_status_image_width).toInt()
                holder.statusImage.layoutParams.height = context.resources.getDimension(R.dimen.item_out_for_delivery_status_image_height).toInt()
            } else {
                holder.statusImage.setBackgroundResource(R.mipmap.waiting)
                holder.statusImage.layoutParams.width = context.resources.getDimension(R.dimen.item_status_image_width).toInt()
                holder.statusImage.layoutParams.height = context.resources.getDimension(R.dimen.item_status_image_height).toInt()
            }

            if(item.status.name == ready) {
                holder.readyTitle.visibility = VISIBLE
                holder.readyTitle.text = context.getString(item.status.nameRes)
                holder.ready.visibility = VISIBLE
                holder.ready.text = Utils.convertDate(item.storedDate)
            } else {
                holder.readyTitle.visibility = INVISIBLE
                holder.ready.visibility = INVISIBLE
            }
        }
    }
}

class SectionTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val sectionTitleTextView: TextView = view.findViewById(R.id.section_title)
}

class SectionContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val shipmentNumber: TextView = view.findViewById(R.id.shipment_number)
    val status: TextView = view.findViewById(R.id.status)
    val statusImage: ImageView = view.findViewById(R.id.shipment_status_image)
    val sender: TextView = view.findViewById(R.id.sender)
    val readyTitle: TextView = view.findViewById(R.id.ready_title)
    val ready: TextView = view.findViewById(R.id.ready)
}