package com.firechicken.rollingpictures.draw

import android.graphics.Color

/**
 * SPDX-FileCopyrightText: © 2018 Divyanshu Bhargava <https://github.com/divyanshub024/AndroidDraw>
 * SPDX-License-Identifier: Apache-2.0 License
 */

data class PaintOptions(var color: Int = Color.BLACK, var strokeWidth: Float = 8f, var alpha: Int = 255, var isEraserOn: Boolean = false)
