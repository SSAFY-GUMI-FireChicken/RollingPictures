package com.firechicken.rollingpictures.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.adapter.PlayerRecyclerViewAdapter
import com.firechicken.rollingpictures.adapter.SectionRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerList
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.roundList
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.sectionList
import com.firechicken.rollingpictures.databinding.FragmentGameFinishBinding
import com.firechicken.rollingpictures.databinding.FragmentGameWaitingBinding
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.RoundService
import com.firechicken.rollingpictures.service.SectionService
import com.firechicken.rollingpictures.util.RetrofitCallback
import com.firechicken.rollingpictures.util.RetrofitUtil.Companion.sectionService
import kotlinx.android.synthetic.main.fragment_game_writing.*

private const val TAG = "GameFinishFragment_싸피"

class GameFinishFragment : Fragment() {

    private lateinit var fragmentGameFinishBinding: FragmentGameFinishBinding
    private lateinit var fContext: Context
    var sectionIdx=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentGameFinishBinding = FragmentGameFinishBinding.inflate(inflater, container, false)
        fContext = container!!.context
        return fragmentGameFinishBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllSection(gameChannelResDTO.data.id)

        Log.d(TAG, "onViewCreated: ${gameChannelResDTO.data.id}, ${playerList[sectionIdx].id}")
//        getSection(gameChannelResDTO.data.id, playerList[sectionIdx].id)



        Log.d(TAG, "onViewCreated: 처음 리사이클러뷰 ")

        leftRightArrow()

        fragmentGameFinishBinding.rightButton.setOnClickListener {
            sectionIdx++
            getAllSection(gameChannelResDTO.data.id)
            ApplicationClass.sectionRecyclerView.smoothScrollToPosition(0)
        }
        fragmentGameFinishBinding.leftButton.setOnClickListener {
            sectionIdx--
            getAllSection(gameChannelResDTO.data.id)
            ApplicationClass.sectionRecyclerView.smoothScrollToPosition(0)

        }


    }

    private fun leftRightArrow(){
        if(sectionIdx==0){
            fragmentGameFinishBinding.leftButton.visibility = View.GONE
            fragmentGameFinishBinding.rightButton.visibility = View.VISIBLE
        }else if(sectionIdx==sectionList!!.size-1){
            fragmentGameFinishBinding.rightButton.visibility = View.GONE
            fragmentGameFinishBinding.leftButton.visibility = View.VISIBLE
        }else{
            fragmentGameFinishBinding.rightButton.visibility = View.VISIBLE
            fragmentGameFinishBinding.leftButton.visibility = View.VISIBLE
        }
    }

    private fun synchronize(){
        ApplicationClass.sectionRecyclerView = fragmentGameFinishBinding.recyclerViewSection
        ApplicationClass.sectionRecyclerViewAdapter = SectionRecyclerViewAdapter(fContext, sectionList!!, sectionIdx)
        ApplicationClass.sectionRecyclerView.apply {
            adapter = ApplicationClass.sectionRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        Log.d(TAG, "synchronize: 생성 후")
    }

    //스케치북 보기
    private fun getAllSection(gameChannelId: Long) {
        SectionService().getAllSection(gameChannelId, object :
            RetrofitCallback<ListResult<SectionAllRetrieveResDTO>> {
            override fun onSuccess(code: Int, responseData: ListResult<SectionAllRetrieveResDTO>) {
                if (responseData.output == 1) {
                    sectionList = responseData.data
                    Log.d(TAG, "onSuccess: ${sectionList}")

                    leftRightArrow()
                    synchronize()

                    Log.d(TAG, "onSuccess: 그림 조회")
                } else {
                    Toast.makeText(
                        context,
                        "문제가 발생하였습니다. 다시 시도해주세요.1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.2", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.3", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

//    //섹션조회
//    private fun getSection(gameChannelId: Long, userId: Long) {
//        SectionService().getSection(gameChannelId, userId, object :
//            RetrofitCallback<ListResult<SectionRetrieveResDTO>> {
//            override fun onSuccess(code: Int, responseData: ListResult<SectionRetrieveResDTO>) {
//                if (responseData.output == 1) {
//                    roundList = responseData.data
//                    Log.d(TAG, "onSuccess: ${roundList}")
//
//                    synchronize()
//
//
//                    Log.d(TAG, "onSuccess: 그림 조회")
//                } else {
//                    Toast.makeText(
//                        context,
//                        "문제가 발생하였습니다. 다시 시도해주세요.1",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun onFailure(code: Int) {
//                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.2", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onError(t: Throwable) {
//                Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.3", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
//    }
}