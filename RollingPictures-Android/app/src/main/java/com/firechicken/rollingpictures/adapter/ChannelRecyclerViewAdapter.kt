package com.firechicken.rollingpictures.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.dto.ChannelResDTO
import kotlinx.android.synthetic.main.list_item_public_room.view.*
import com.firechicken.rollingpictures.adapter.ChannelRecyclerViewAdapter.ChannelInfoHolder

class ChannelRecyclerViewAdapter(
    private var context: Context,
    private val channelList: MutableList<ChannelResDTO>
) : RecyclerView.Adapter<ChannelInfoHolder>() {

    interface OnItemClickListener{
        fun onItemClick(v:View, channel: ChannelResDTO, pos : Int)
    }
    private lateinit var listener : OnItemClickListener

    fun setItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class ChannelInfoHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(channel: ChannelResDTO, pos: Int) {
            view.publicRoomItemButton.text = channel.title + " [${channel.curPeopleCnt}/${channel.maxPeopleCnt}]"
            view.publicRoomItemButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            itemView.setOnClickListener {
                listener.onItemClick(view,channel, pos)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelInfoHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_public_room, parent, false)
        return ChannelInfoHolder(view)
    }


    override fun onBindViewHolder(holder: ChannelInfoHolder, position: Int) {
        holder.apply {
            bindInfo(channelList[position], position)
        }
    }

    override fun getItemCount(): Int = channelList.size

}