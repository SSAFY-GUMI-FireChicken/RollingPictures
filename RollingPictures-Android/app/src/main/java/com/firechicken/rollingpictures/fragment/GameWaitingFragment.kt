package com.firechicken.rollingpictures.fragment

import android.content.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.adapter.PlayerRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.channelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.fragmentNum
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.loginUserResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerList
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.playerRecyclerViewAdapter
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.prefs
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.recyclerView
import com.firechicken.rollingpictures.databinding.FragmentGameWaitingBinding
import com.firechicken.rollingpictures.dialog.GameExitDialog
import com.firechicken.rollingpictures.dialog.GameSettingDialog
import com.firechicken.rollingpictures.dto.*
import com.firechicken.rollingpictures.service.GameChannelService
import com.firechicken.rollingpictures.service.SectionService
import com.firechicken.rollingpictures.util.RetrofitCallback
import kotlinx.android.synthetic.main.fragment_game_waiting.*

class GameWaitingFragment : Fragment() {

    private lateinit var fragmentGameWaitingBinding: FragmentGameWaitingBinding
    private lateinit var fContext:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentGameWaitingBinding = FragmentGameWaitingBinding.inflate(inflater, container, false)
        fContext = container!!.context
        return fragmentGameWaitingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentNum = 0
        prefs.setEnteredChannel(channelResDTO.data.code)

        for(player in channelResDTO.data.users){
            if(player.id== loginUserResDTO.data.id){
                if(player.isLeader=="Y"){
                    startGameButton.setText("START GAME")
                    startGameButton.setEnabled(true)
                    settingImageButton.visibility = VISIBLE
                }else{
                    startGameButton.setText("WAITING FOR GAME TO START...")
                    startGameButton.setTextSize(16F)
                    startGameButton.setEnabled(false)
                    settingImageButton.visibility = GONE
                }
                break
            }
        }

        if(settingImageButton.isVisible){
            settingImageButton.setOnClickListener {
                val dialog = GameSettingDialog(requireContext())
                dialog.showDialog()
                dialog.setOnClickListener(object : GameSettingDialog.OnDialogClickListener {
                    override fun onDialogOkClick(changedChannel: MakeChannelReqDTO) {
                        GameActivity().updateChannel(changedChannel)
                        fragmentGameWaitingBinding.playerCountTextView.text = "${playerRecyclerViewAdapter.itemCount}/${channelResDTO.data.maxPeopleCnt}"
                        dialog.dialog.dismiss()
                    }
                })
            }
        }

        fragmentGameWaitingBinding.exitRoom.setOnClickListener {
            val dialog = GameExitDialog(requireContext())
            dialog.showDialog()
            dialog.setOnClickListener(object : GameExitDialog.OnDialogClickListener {
                override fun onDialogOkClick() {
                    GameActivity().outChannel(channelResDTO.data.code, prefs.getId()!!)
                    prefs.setEnteredChannel("none")
                    dialog.dialog.dismiss()
                    requireActivity().finish()
                }
            })
        }

        fragmentGameWaitingBinding.startGameButton.setOnClickListener {
            if(playerList.size == 1){
                toast( "최소 두명 이상이 있어야 게임을 진행할 수 있습니다.")
            }else{
                makeGameChannel()
            }
        }

        fragmentGameWaitingBinding.roomCodeTextView.text = channelResDTO.data.code

        recyclerView = fragmentGameWaitingBinding.recyclerViewPlayer
        playerRecyclerViewAdapter = PlayerRecyclerViewAdapter(fContext, playerList)
        recyclerView.apply {
            adapter = playerRecyclerViewAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        fragmentGameWaitingBinding.playerCountTextView.text = "${playerRecyclerViewAdapter.itemCount}/${channelResDTO.data.maxPeopleCnt}"

        fragmentGameWaitingBinding.copyImageButton.setOnClickListener {
            val clipboard = fContext.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", fragmentGameWaitingBinding.roomCodeTextView.text.toString())
            clipboard.setPrimaryClip(clip)
            toast( "클립보드에 복사되었습니다.")
        }

        fragmentGameWaitingBinding.shareImageButton.setOnClickListener {
            try {
                val sendText = "Rolling Pictures 초대 방 코드 : ${channelResDTO.data.code}"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText)
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "방 코드 공유"))
            } catch (ignored: ActivityNotFoundException) { }
        }
    }

    private fun makeGameChannel() {
        val req = MakeGameChannelReqDTO(channelResDTO.data.id, loginUserResDTO.data.id)

        GameChannelService().makeGameChannel(req, object :
            RetrofitCallback<SingleResult<GameChannelResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<GameChannelResDTO>) {
                if (responseData.data.id > 0L) {
                    gameChannelResDTO = responseData
                    makeSection()
                } else {
                    toast("m문제가 발생하였습니다. 다시 시도해주세요.",)
                }
            }

            override fun onFailure(code: Int) {
                toast("문제가 발생하였습니다. 다시 시도해주세요.")
            }

            override fun onError(t: Throwable) {
                toast( "문제가 발생하였습니다. 다시 시도해주세요.")
            }
        })
    }

    //섹션 생성
    private fun makeSection() {
        val req = SectionReqDTO(gameChannelResDTO.data.id, loginUserResDTO.data.id)

        SectionService().makeSection(req, object : RetrofitCallback<ListResult<SectionResDTO>> {
            override fun onSuccess(code: Int, responseData: ListResult<SectionResDTO>) {
                if (responseData.data.isNotEmpty()) {

                } else {
                    toast("문제가 발생하였습니다. 다시 시도해주세요.",)
                }
            }

            override fun onFailure(code: Int) {
                toast("문제가 발생하였습니다. 다시 시도해주세요.")
            }

            override fun onError(t: Throwable) {
                toast("문제가 발생하였습니다. 다시 시도해주세요.")
            }
        })
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}
