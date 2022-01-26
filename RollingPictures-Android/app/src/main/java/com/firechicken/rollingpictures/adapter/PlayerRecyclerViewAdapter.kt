package com.firechicken.rollingpictures.adapter

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.dto.User

class PlayerRecyclerViewAdapter(
    private val context: Context,
    private val playerList: MutableList<User>
) : RecyclerView.Adapter<PlayerRecyclerViewAdapter.UserInfoHolder>() {

    inner class UserInfoHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(player: User) {
            view.findViewById<TextView>(R.id.playerButton).text = player.nickName
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
            val player = itemView.findViewById<AppCompatButton>(R.id.playerButton)
            player.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            //임의로 방장(푸읍) 선정하여 왕관 표시
            if (player.text.toString() == "푸읍") {
                player.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    context.getResources().getDrawable(R.drawable.crown),
                    null
                )
            }
        }
    }

    override fun getItemCount(): Int = playerList.size

    fun deleteItem(index: Int) {
        playerList.removeAt(index)
        notifyDataSetChanged()
    }


}