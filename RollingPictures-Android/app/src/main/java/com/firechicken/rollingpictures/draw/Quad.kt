package com.firechicken.rollingpictures.draw

import android.graphics.Path
import java.io.Writer

/**
 * SPDX-FileCopyrightText: © 2018 Divyanshu Bhargava <https://github.com/divyanshub024/AndroidDraw>
 * SPDX-License-Identifier: Apache-2.0 License
 */

class Quad(private val x1: Float, private val y1: Float, private val x2: Float, private val y2: Float) : Action {

    override fun perform(path: Path) {
        path.quadTo(x1, y1, x2, y2)
    }

    override fun perform(writer: Writer) {
        writer.write("Q$x1,$y1 $x2,$y2")
    }
}