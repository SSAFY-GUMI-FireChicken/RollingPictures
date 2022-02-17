package com.firechicken.rollingpictures.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.adapter.PlayerRecyclerViewAdapter.UserInfoHolder
import com.firechicken.rollingpictures.dto.UserInfoResDTO
import kotlinx.android.synthetic.main.list_item_player.view.*

class PlayerRecyclerViewAdapter(
    private val context: Context,
    private val playerList: MutableList<UserInfoResDTO>
) : RecyclerView.Adapter<UserInfoHolder>() {

    inner class UserInfoHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(player: UserInfoResDTO) {
            view.playerButton.text = player.nickname
            view.playerButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            if (playerList[position].isLeader == "Y") {
                view.playerButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    context.getResources().getDrawable(R.drawable.crown), null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_player, parent, false)
        return UserInfoHolder(view)
    }

    override fun onBindViewHolder(holder: UserInfoHolder, position: Int) {
        holder.apply {
            bindInfo(playerList[position])
        }
    }

    override fun getItemCount(): Int = playerList.size
}
