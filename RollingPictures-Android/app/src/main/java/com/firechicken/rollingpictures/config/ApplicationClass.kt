package com.firechicken.rollingpictures.config

import android.app.Application
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.firechicken.rollingpictures.adapter.PlayerRecyclerViewAdapter
import com.firechicken.rollingpictures.adapter.SectionRecyclerViewAdapter
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.util.PreferenceUtil

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApplicationClass_싸피"
class ApplicationClass : Application() {
    companion object{
        const val SERVER_URL = "http://192.168.35.31:8185/"
        const val websocketURL = "ws://192.168.35.31:8185/rolling-pictures/websocket"
//  석규 로컬
//        const val SERVER_URL = "http://192.168.0.9:8185/"
//        const val websocketURL = "ws://192.168.0.9:8185/rolling-pictures/websocket"
        lateinit var retrofit: Retrofit
        lateinit var prefs: PreferenceUtil
        lateinit var loginUserResDTO: SingleResult<LoginUserResDTO>
        lateinit var channelResDTO: SingleResult<ChannelResDTO>
        lateinit var gameChannelResDTO: SingleResult<GameChannelResDTO>
//        lateinit var sectionResDTO: ListResult<SectionResDTO>



        //방의 플레이어 목록
        var playerList: MutableList<UserInfoResDTO> = mutableListOf()
        var sectionList: List<SectionAllRetrieveResDTO>?=null
//        var roundList: List<SectionRetrieveResDTO>?=null

        //리사이클러뷰
        lateinit var recyclerView: RecyclerView
        lateinit var sectionRecyclerView: RecyclerView
        lateinit var playerRecyclerViewAdapter: PlayerRecyclerViewAdapter
        lateinit var sectionRecyclerViewAdapter: SectionRecyclerViewAdapter

        //라운드 넘버
        var roundNum = 0

        //최근 프래그먼트 (GameWaitingFragment : 0, GameWritingFragment : 1, GameDrawingFragment : 2, GameFinishFragment : 3)
        var fragmentNum = -1

    }

    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d(TAG, "onCreate: ")

        prefs = PreferenceUtil(applicationContext)
    }

}