package com.firechicken.rollingpictures_android

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

}