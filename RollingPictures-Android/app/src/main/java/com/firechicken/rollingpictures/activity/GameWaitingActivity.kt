package com.firechicken.rollingpictures.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firechicken.rollingpictures.adapter.PlayerRecyclerViewAdapter
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.databinding.ActivityGameWaitingBinding

class GameWaitingActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var playerRecyclerViewAdapter: PlayerRecyclerViewAdapter

    //임의의 플레이어 데이터 삽입
    val userList: MutableList<String> =
        mutableListOf("푸읍", "푸읏", "풉킥", "아름", "이승호", "풉")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityGameWaitingBinding = ActivityGameWaitingBinding.inflate(layoutInflater)
        setContentView(activityGameWaitingBinding.root)

        activityGameWaitingBinding.exitRoom.setOnClickListener {
            val dialog = GameExitDialog(this)
            dialog.showDialog()
        }

        activityGameWaitingBinding.startGameButton.setOnClickListener {
            val intent = Intent(this@GameWaitingActivity, GameActivity::class.java)
            startActivity(intent)
        }

        recyclerView = activityGameWaitingBinding.recyclerViewPlayer
        playerRecyclerViewAdapter = PlayerRecyclerViewAdapter(this, userList)
        recyclerView.apply {
            adapter = playerRecyclerViewAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
        activityGameWaitingBinding.playerCountTextView.setText("${playerRecyclerViewAdapter.itemCount}/10")

    }
}