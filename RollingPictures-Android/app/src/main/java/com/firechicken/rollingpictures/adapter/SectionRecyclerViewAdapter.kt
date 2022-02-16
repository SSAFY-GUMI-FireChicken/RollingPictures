package com.firechicken.rollingpictures.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerList
import com.firechicken.rollingpictures.dto.RoundInfo
import com.firechicken.rollingpictures.dto.SectionAllRetrieveResDTO
import com.firechicken.rollingpictures.dto.SectionRetrieveResDTO
import kotlinx.android.synthetic.main.fragment_game_writing.*
import kotlinx.android.synthetic.main.list_item_player.view.*
import kotlinx.android.synthetic.main.list_item_section.view.*

class SectionRecyclerViewAdapter(
    private val context: Context,
    private val sectionList: List<SectionAllRetrieveResDTO>,
    private val sectionIdx: Int
) : RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionInfoHolder>() {

    inner class SectionInfoHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(round: RoundInfo) {
            if(round.roundNumber%2==1){
                view.keywordTextView.visibility = View.VISIBLE
                view.playerName1.visibility = View.VISIBLE
                view.ImageView.visibility = View.GONE
                view.playerName2.visibility = View.GONE
                view.keywordTextView.setText(round.img)
                view.playerName1.setText(round.nickname)

            }else{
                view.keywordTextView.visibility = View.GONE
                view.playerName1.visibility = View.GONE
                view.ImageView.visibility = View.VISIBLE
                view.playerName2.visibility = View.VISIBLE
                Glide.with(context!!).load(round.img).into(view.ImageView)
                view.playerName2.setText(round.nickname)

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionInfoHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_section, parent, false)
        return SectionInfoHolder(view)
    }


    override fun onBindViewHolder(holder: SectionInfoHolder, position: Int) {
        holder.apply {
            bindInfo(sectionList[sectionIdx].roundInfos[position])

        }
    }

    override fun getItemCount(): Int = sectionList[sectionIdx].roundInfos.size



}