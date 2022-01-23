package com.firechicken.rollingpictures_android

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.firechicken.rollingpictures_android.databinding.FragmentGameDrawingBinding

class GameDrawingFragment : Fragment() {

    private lateinit var binding: FragmentGameDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_drawing, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrawTools()
        colorSelector()

    }

    private fun setDrawTools() {
        binding.clearImageButton.setOnClickListener {
            binding.drawView.clearCanvas()
        }


        binding.brushImageButton.setOnClickListener {
            binding.drawView.toggleEraser(false)

        }

        binding.eraserImageButton.setOnClickListener {
            binding.drawView.toggleEraser(true)
            // TODO: 2022-01-21 eraser 클릭됐을 때 색상 바꾸기 
        }

        binding.undoImageButton.setOnClickListener {
            binding.drawView.undo()
        }

        binding.redoImageButton.setOnClickListener {
            binding.drawView.redo()
        }
    }

    private fun colorSelector() {
        binding.colorPalette.redImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_red,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.apricotImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_apricot,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.brownImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_brown,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.orangeImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_orange,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.yellowImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_yellow,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.lightGreenImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_light_green,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.greenImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_green,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.skyImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_sky,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.blueImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_blue,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.purpleImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_purple,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.blackImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_black,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.whiteImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_white,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.lightGrayImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_light_gray,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }

        binding.colorPalette.grayImageView.setOnClickListener {
            val color = ResourcesCompat.getColor(resources, R.color.palette_gray,null)
            binding.drawView.setColor(color)
            binding.circleView.setColor(color)
        }
    }

}