package com.firechicken.rollingpictures.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.firechicken.rollingpictures.adapter.ChannelRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.databinding.ActivityPublicRoomListBinding
import com.firechicken.rollingpictures.dto.ChannelListResDTO
import com.firechicken.rollingpictures.dto.ChannelResDTO
import com.firechicken.rollingpictures.dto.InOutChannelReqDTO
import com.firechicken.rollingpictures.dto.SingleResult
import com.firechicken.rollingpictures.service.ChannelService
import com.firechicken.rollingpictures.util.RetrofitCallback

class PublicRoomListActivity : AppCompatActivity() {

    var channelList = mutableListOf<ChannelResDTO>()
    lateinit var channelRecyclerViewAdapter: ChannelRecyclerViewAdapter
    lateinit var activityPublicRoomListBinding: ActivityPublicRoomListBinding
    var listIdx = 0
    var totalCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPublicRoomListBinding = ActivityPublicRoomListBinding.inflate(layoutInflater)
        setContentView(activityPublicRoomListBinding.root)

        searchChannelList(listIdx, 10)

        activityPublicRoomListBinding.refreshImageButton.setOnClickListener {
            searchChannelList(listIdx, 10)
        }

        activityPublicRoomListBinding.prevImageButton.setOnClickListener {
            if(listIdx==0) {
                toast("가장 첫 페이지 입니다!")
            }else {
                searchChannelList(--listIdx, 10)
            }
        }

        activityPublicRoomListBinding.nextImageButton.setOnClickListener {
            if(listIdx==totalCnt/10) {
                toast("가장 마지막 페이지 입니다!")
            }else {
                searchChannelList(++listIdx, 10)
            }
        }
    }

    private fun searchChannelList(page: Int, batch: Int) {
        ChannelService().searchChannelList(page, batch, object :
            RetrofitCallback<SingleResult<ChannelListResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelListResDTO>) {
                if (responseData.data.totalCnt > 0L) {

                    channelList = mutableListOf()

                    for(i:Int in 0..responseData.data.channels.size-1){
                        channelList.add(responseData.data.channels[i])
                    }

                    totalCnt = responseData.data.totalCnt.toInt()

                    adapterRefresh()
                } else {
                    Toast.makeText(this@PublicRoomListActivity, "문제가 발생하였습니다. 다시 시도해주세요.1",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(this@PublicRoomListActivity, "문제가 발생하였습니다. 다시 시도해주세요.2", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(this@PublicRoomListActivity, "문제가 발생하였습니다. 다시 시도해주세요.3", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun adapterRefresh(){
        channelRecyclerViewAdapter = ChannelRecyclerViewAdapter(this@PublicRoomListActivity, channelList)
        channelRecyclerViewAdapter.setItemClickListener(object : ChannelRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v:View, channel: ChannelResDTO, pos : Int) {
                inChannel(channel.code, ApplicationClass.prefs.getId()!!)
            }
        })

        activityPublicRoomListBinding.recyclerViewChannel.apply {

            layoutManager = GridLayoutManager(context, 2)
            adapter = channelRecyclerViewAdapter
        }
    }

    fun inChannel(roomcode: String, userId: Long) {
        val req = InOutChannelReqDTO(roomcode, userId)
        ChannelService().inChannel(req, object : RetrofitCallback<SingleResult<ChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<ChannelResDTO>) {
                if(responseData.data != null){
                    if (responseData.data.id > 0) {
                        ApplicationClass.channelResDTO = responseData
                        val intent = Intent(this@PublicRoomListActivity, GameActivity::class.java)
                        intent.putExtra("code", roomcode)
                        startActivity(intent)
                    } else {
                        toast("inChannel에서 문제가 발생하였습니다. 다시 시도해주세요.")
                    }
                }else{
                    if(responseData.msg == "방 정보가 없습니다."){
                        toast("존재하지 않는 방 코드입니다. 다시 입력해주세요.")
                    }else{
                        toast("해당 방은 정원초과 입니다.")
                    }
                }
            }

            override fun onFailure(code: Int) {
                toast("inChannel에서 실패문제가 발생하였습니다. 다시 시도해주세요.")
            }

            override fun onError(t: Throwable) {
                toast("inChannel에서 에러문제가 발생하였습니다. 다시 시도해주세요.")
            }
        })
    }

    private fun toast(text: String) {
        Toast.makeText(this@PublicRoomListActivity, text, Toast.LENGTH_SHORT).show()
    }
}
