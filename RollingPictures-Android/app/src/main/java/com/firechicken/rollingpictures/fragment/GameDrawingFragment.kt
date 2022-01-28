package com.firechicken.rollingpictures.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.firechicken.rollingpictures.R
import com.firechicken.rollingpictures.activity.GameActivity
import com.firechicken.rollingpictures.activity.MainActivity
import com.firechicken.rollingpictures.databinding.FragmentGameDrawingBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class GameDrawingFragment : Fragment() {

    private lateinit var binding: FragmentGameDrawingBinding
    private var isBrushSettingOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_drawing, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrawTools()
        colorSelector()
        setBrushWidth()


        binding.completeButton.setOnClickListener {
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
        // TODO: 2022-01-24 갤러리에 저장하는 코드인데, 갤러리 저장 대신 서버로 전송되게 변경해야 합니다 !!
        val bStream = ByteArrayOutputStream()
        val bitmap = binding.drawView.getBitmap()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)

        val filename = "RP_${System.currentTimeMillis()}"
        saveImage(bitmap, filename)
    }

    private fun saveImage(bitmap: Bitmap, fileName: String) {
        val imageDir = "${Environment.DIRECTORY_PICTURES}/RollingPictures/"
        val path = Environment.getExternalStoragePublicDirectory(imageDir)
        Log.e("path", path.toString())
        val file = File(path, "$fileName.png")
        path.mkdirs()
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
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

}