package com.firechicken.rollingpictures.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.adapter.SectionRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.fragmentNum
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.sectionList
import com.firechicken.rollingpictures.databinding.FragmentGameFinishBinding
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.SectionService
import com.firechicken.rollingpictures.util.RetrofitCallback
import kotlinx.android.synthetic.main.activity_game.*

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
        fragmentNum = 3
        (activity as GameActivity).apply {
            roundTextView.setText("Sketchbook")
        }
        fragmentGameFinishBinding = FragmentGameFinishBinding.inflate(inflater, container, false)
        fContext = container!!.context
        return fragmentGameFinishBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllSection(gameChannelResDTO.data.id)

        fragmentGameFinishBinding.rightButton.setOnClickListener {
            if(sectionIdx==sectionList!!.size-1){
                Toast.makeText(context, "마지막 페이지입니다.", Toast.LENGTH_SHORT)
            }else{
                sectionIdx++
                getAllSection(gameChannelResDTO.data.id)
                ApplicationClass.sectionRecyclerView.smoothScrollToPosition(0)
            }
        }

        fragmentGameFinishBinding.leftButton.setOnClickListener {
            if(sectionIdx==0){
                Toast.makeText(context, "첫 번째 페이지입니다.", Toast.LENGTH_SHORT)
            }else{
                sectionIdx--
                getAllSection(gameChannelResDTO.data.id)
                ApplicationClass.sectionRecyclerView.smoothScrollToPosition(0)
            }
        }

        fragmentGameFinishBinding.exitButton.setOnClickListener {
            val dialog = GameExitDialog(fContext)
            dialog.showDialog()
            dialog.setOnClickListener(object : GameExitDialog.OnDialogClickListener {
                override fun onDialogOkClick() {
                    GameActivity().outChannel(ApplicationClass.channelResDTO.data.code, ApplicationClass.prefs.getId()!!)
                    ApplicationClass.prefs.setEnteredChannel("none")
                    dialog.dialog.dismiss()
                    requireActivity().finish()
                }
            })
        }
    }

    private fun leftRightArrow(){
        fragmentGameFinishBinding.rightButton.visibility = View.VISIBLE
        fragmentGameFinishBinding.leftButton.visibility = View.VISIBLE
        if(sectionIdx==0){
            fragmentGameFinishBinding.leftButton.visibility = View.INVISIBLE
        }
        if(sectionIdx==sectionList!!.size-1){
            fragmentGameFinishBinding.rightButton.visibility = View.INVISIBLE
        }
    }

    private fun synchronize(){
        ApplicationClass.sectionRecyclerView = fragmentGameFinishBinding.recyclerViewSection
        ApplicationClass.sectionRecyclerViewAdapter = SectionRecyclerViewAdapter(fContext, sectionList!!, sectionIdx)
        ApplicationClass.sectionRecyclerView.apply {
            adapter = ApplicationClass.sectionRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    //스케치북 보기
    private fun getAllSection(gameChannelId: Long) {
        SectionService().getAllSection(gameChannelId, object :
            RetrofitCallback<ListResult<SectionAllRetrieveResDTO>> {
            override fun onSuccess(code: Int, responseData: ListResult<SectionAllRetrieveResDTO>) {
                if (responseData.output == 1) {
                    sectionList = responseData.data
                    leftRightArrow()
                    synchronize()

                } else {
                    Toast.makeText(context, "문제가 발생하였습니다. 다시 시도해주세요.1", Toast.LENGTH_SHORT).show()
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
}
