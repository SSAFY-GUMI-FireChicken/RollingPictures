package com.firechicken.rollingpictures.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.config.ApplicationClass
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.fragmentNum
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.gameChannelResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.loginUserResDTO
import com.firechicken.rollingpictures.config.ApplicationClass.Companion.roundNum
import com.firechicken.rollingpictures.databinding.FragmentGameDrawingBinding
import com.firechicken.rollingpictures.dto.RoundReqDTO
import com.firechicken.rollingpictures.dto.RoundResDTO
import com.firechicken.rollingpictures.dto.SingleResult
import com.firechicken.rollingpictures.service.RoundService
import com.firechicken.rollingpictures.util.RetrofitCallback
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_game_drawing.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

class GameDrawingFragment : Fragment() {

    private lateinit var binding: FragmentGameDrawingBinding
    private var isBrushSettingOpen: Boolean = false
    lateinit var explainTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNum = 2
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_drawing, container, false)
        (activity as GameActivity).roundTextView.setText("Round ${roundNum}")
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roundView(gameChannelResDTO.data.id, loginUserResDTO.data.id, roundNum)

        setDrawTools()
        colorSelector()
        setBrushWidth()

        Handler(Looper.getMainLooper()).postDelayed({
            if(fragmentNum==2 && binding.completeButton.isEnabled){
                explainTextView = view.findViewById(R.id.explainTextView)
                explainTextView.setTextColor(R.color.red_dark)
                explainTextView.setText("?????? ???????????? ????????? ??? ????????? ??????????????????!!!")
            }
        }, 60000)

        binding.completeButton.setOnClickListener {
            binding.completeButton.text = "SUBMITED"
            binding.completeButton.isEnabled = false
            savePhoto()
        }
    }

    private fun setDrawTools() {
        binding.clearImageButton.setOnClickListener {
            brushSettingClose()
            binding.drawView.clearCanvas()
        }

        binding.circleView.setOnClickListener {
            setBrush()
        }

        binding.brushImageButton.setOnClickListener {
            setBrush()
        }

        binding.eraserImageButton.setOnClickListener {
            brushSettingClose()
            binding.drawView.toggleEraser(true)
        }

        binding.undoImageButton.setOnClickListener {
            brushSettingClose()
            binding.drawView.undo()
        }

        binding.redoImageButton.setOnClickListener {
            brushSettingClose()
            binding.drawView.redo()
        }
    }

    private fun setBrush() {
        if (isBrushSettingOpen) {
            binding.brushSetting.linearLayout.visibility = View.GONE
            isBrushSettingOpen = false
        } else {
            binding.brushSetting.linearLayout.visibility = View.VISIBLE
            isBrushSettingOpen = true
        }
        binding.drawView.toggleEraser(false)
    }

    private fun colorSelector() {
        binding.brushSetting.redImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_red, null)
            setColor(color)
        }

        binding.brushSetting.apricotImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_apricot, null)
            setColor(color)
        }

        binding.brushSetting.brownImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_brown, null)
            setColor(color)
        }

        binding.brushSetting.orangeImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_orange, null)
            setColor(color)
        }

        binding.brushSetting.yellowImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_yellow, null)
            setColor(color)
        }

        binding.brushSetting.lightGreenImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_light_green, null)
            setColor(color)
        }

        binding.brushSetting.greenImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_green, null)
            setColor(color)
        }

        binding.brushSetting.skyImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_sky, null)
            setColor(color)
        }

        binding.brushSetting.blueImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_blue, null)
            setColor(color)
        }

        binding.brushSetting.purpleImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_purple, null)
            setColor(color)
        }

        binding.brushSetting.blackImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_black, null)
            setColor(color)
        }

        binding.brushSetting.whiteImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_white, null)
            setColor(color)
        }

        binding.brushSetting.lightGrayImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_light_gray, null)
            setColor(color)
        }

        binding.brushSetting.grayImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_gray, null)
            setColor(color)
        }
    }

    private fun setBrushWidth() {
        binding.brushSetting.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.drawView.setStrokeWidth(progress.toFloat())
                binding.circleView.setCircleRadius(progress.toFloat())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun savePhoto() {
        val bStream = ByteArrayOutputStream()
        val bitmap = binding.drawView.getBitmap()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
        val filename = "RP_${System.currentTimeMillis()}"
        saveImage(bitmap, filename)
    }

    private fun saveImage(bitmap: Bitmap, fileName: String) {
        val imageDir = "${Environment.DIRECTORY_PICTURES}/RollingPictures/"
        val path = Environment.getExternalStoragePublicDirectory(imageDir)
        val file = File(path, "$fileName.png")
        path.mkdirs()
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val multipartFile = MultipartBody.Part.createFormData("multipartFile", "${fileName}.png", file.asRequestBody())
        val req = RoundReqDTO(gameChannelResDTO.data.id, ApplicationClass.prefs.getId()!!, null, roundNum)

        roundRegister(req, multipartFile)
    }

    private fun setColor(color: Int) {
        binding.drawView.setColor(color)
        binding.circleView.setColor(color)
    }

    private fun brushSettingClose() {
        if (isBrushSettingOpen) {
            binding.brushSetting.linearLayout.visibility = View.GONE
            isBrushSettingOpen = false
        }
    }

    //??? ??????
    private fun roundView(gameChannelId: Long, id: Long, roundNumber: Int) {
        RoundService().roundView(gameChannelId, id, roundNumber, object :
            RetrofitCallback<SingleResult<RoundResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<RoundResDTO>) {
                if (responseData.output == 1) {
                    writingTextView.setText(responseData.data.imgSrc)
                } else {
                    Toast.makeText(context, "????????? ?????????????????????. ?????? ??????????????????.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(context, "????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(context, "????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    //?????? ??????
    private fun roundRegister(req: RoundReqDTO, multipartFile: MultipartBody.Part?) {
        RoundService().roundRegister(req, multipartFile, object : RetrofitCallback<SingleResult<RoundResDTO>> {
            override fun onSuccess(code: Int, responseData: SingleResult<RoundResDTO>) {
                if (responseData.output==1) {

                } else {
                    Toast.makeText(context, "????????? ?????????????????????. ?????? ??????????????????.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(code: Int) {
                Toast.makeText(context, "????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(context, "????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
