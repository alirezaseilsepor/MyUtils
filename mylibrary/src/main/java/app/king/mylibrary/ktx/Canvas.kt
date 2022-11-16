package app.king.mylibrary.ktx

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.drawString(text: String, x: Float, y: Float, paint: Paint, extraSpaceLine: Float = 0f) {
    var y1 = y
    if (text.contains("\n")) {
        val texts = text.split("\n").toTypedArray()
        for (txt in texts) {
            drawText(txt, x, y1, paint)
            y1 += paint.textSize + extraSpaceLine
        }
    } else {
        drawText(text, x, y1, paint)
    }
}