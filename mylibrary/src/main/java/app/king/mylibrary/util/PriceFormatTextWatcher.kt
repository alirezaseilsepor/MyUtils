package app.king.mylibrary.util

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ReplacementSpan
import android.widget.TextView
import java.text.DecimalFormatSymbols


class PriceFormatTextWatcher() : TextWatcher {
    private var paddingPx: Int = 0
    private var internalStopFormatFlag: Boolean = false
    private val decimalFormatSymbols = DecimalFormatSymbols()
    private var separatorCharacter = decimalFormatSymbols.groupingSeparator.toString()

    /**
     * Change the padding, do not take effect until next text change
     *
     * @param paddingPx padding in pixels unit
     */
    fun setPaddingPx(paddingPx: Int) {
        this.paddingPx = paddingPx
    }

    /**
     * Change the padding, do not take effect until next text change
     *
     * @param textView the widget you want to format
     * @param em       padding in em unit (character size unit)
     */
    fun setPaddingEm(textView: TextView, separator: String = ",", em: Float = 1f) {
        this.separatorCharacter = separator
        val emSize = textView.paint.measureText(separatorCharacter)
        setPaddingPx((em * emSize).toInt())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (internalStopFormatFlag) {
            return
        }
        internalStopFormatFlag = true
        formatPhoneNumber(s, paddingPx, separatorCharacter)
        internalStopFormatFlag = false
    }


    class SeparatorSpan(
        private val mPadding: Int,
        private val separator: String,
    ) :
        ReplacementSpan() {

        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?,
        ): Int {
            val widths = FloatArray(end - start)
            paint.getTextWidths(text, start, end, widths)
            var sum = mPadding
            for (i in widths.indices) {
                sum += widths[i].toInt()
            }

            return sum
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint,
        ) {


            canvas.drawText(text, start, end, x, y.toFloat(), paint)
            if (mPadding > 0) {
                val drawingTextWidth = paint.measureText(text, start, end)
                canvas.drawText(
                    separator,
                    0,
                    separator.length,
                    x + drawingTextWidth,
                    y.toFloat(),
                    paint
                )
            }

        }

    }

    companion object {


        fun formatPhoneNumber(
            ccNumber: Editable,
            paddingPx: Int,
            separator: String,

            ) {
            val textLength = ccNumber.length
            // first remove any previous span
            val spans = ccNumber.getSpans(0, ccNumber.length, SeparatorSpan::class.java)
            for (i in spans.indices) {
                ccNumber.removeSpan(spans[i])
            }

            for (i in 0 until textLength) {
                val padding = if (textLength >= 4 && textLength.minus(i)
                        .rem(3) == 1 && i < textLength - 1
                ) paddingPx else 0


                val span =
                    SeparatorSpan(padding, separator)
                ccNumber.setSpan(span, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }


        }
    }
}