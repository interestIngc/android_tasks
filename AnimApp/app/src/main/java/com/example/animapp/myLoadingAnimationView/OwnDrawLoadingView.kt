package com.example.animapp.myLoadingAnimationView
import android.animation.*
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.example.animapp.MainActivity
import com.example.animapp.R
import kotlin.math.*

class OwnDrawLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var currentValue = 0


    private fun dp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }


    private var circleCnt = 10

    private var desiredWidth = dp(170f)

    private var desiredHeight = desiredWidth

    private var radius = dp(86f)
    private var centerXHorizontal = dp(260f) / 2
    private var centerYHorizontal = dp(126f) / 2
    private var centerXVertical = dp(157f) / 2
    private var centerYVertical = dp(230f) / 2
    private var centerX = centerXVertical
    private var centerY = centerYVertical

    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.OwnDrawLoadingView, defStyleAttr, 0
        )
        try {
            circleCnt = a.getInt(R.styleable.OwnDrawLoadingView_count, circleCnt)
            desiredWidth = a.getFloat(R.styleable.OwnDrawLoadingView_width, desiredWidth)
            desiredHeight = desiredWidth
            centerXVertical = a.getFloat(R.styleable.OwnDrawLoadingView_centerxVert, centerXVertical)
            centerYVertical = a.getFloat(R.styleable.OwnDrawLoadingView_centeryVert, centerYVertical)
            radius = a.getFloat(R.styleable.OwnDrawLoadingView_radius, radius)
            centerXHorizontal = a.getFloat(R.styleable.OwnDrawLoadingView_centerXHorizontal, centerXHorizontal)
            centerYHorizontal = a.getFloat(R.styleable.OwnDrawLoadingView_centerYHorizontal, centerYHorizontal)
        } finally {
            a.recycle()
        }
    }

    private fun get_paint(col : Int) : Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = col
            style = Paint.Style.FILL
        }
    }


    private val paintCols : Array<Paint> = arrayOf(get_paint(Color.BLUE),
        get_paint(Color.GREEN),
        get_paint(Color.RED),
        get_paint(Color.CYAN))

    private val getDegrees = arrayOf(0.5f, -0.5f, 0.5f, -0.5f)

    private val circleDegrees = arrayOf(0F, 0F, 0F, 0F)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getSize(widthMeasureSpec, desiredWidth.toInt()),
            getSize(heightMeasureSpec, desiredHeight.toInt())
        )
    }

    private fun rotateAnimation(canvas: Canvas, rad : Float, num : Int) {
        val save = canvas.save()
        canvas.translate(dp(centerX), dp(centerY))
        circleDegrees[num] += getDegrees[num]
        canvas.rotate(circleDegrees[num])
        val circle_rad = rad / 10
        for (i in 0 until circleCnt) {
            val t : Double = (2 * Math.PI / circleCnt) * i
            val x : Float = rad * cos(t).toFloat()
            val y : Float = rad * sin(t).toFloat()
            canvas.drawCircle(dp(x), dp(y), dp(circle_rad), paintCols[num])
        }
        canvas.restoreToCount(save)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        var i = 1.5F
        for (j in 0 until 4) {
            rotateAnimation(canvas, radius / i, j)
            i *= 1.5F
        }
        invalidate()
    }


    private fun getSize(measureSpec: Int, desired: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.AT_MOST -> min(size, desired)
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> desired
            else -> desired
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        if (centerX == centerXVertical) {
            centerX = centerXHorizontal
            centerY = centerYHorizontal
        } else {
            centerX = centerXVertical
            centerY = centerYVertical
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = CustomState(super.onSaveInstanceState())
        state.value = currentValue
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as CustomState
        super.onRestoreInstanceState(state.superState)
        currentValue = state.value
    }

    private class CustomState : BaseSavedState {
        var value: Int = 0

        constructor(superState: Parcelable?) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            value = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(value)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<CustomState> {
                override fun createFromParcel(source: Parcel): CustomState = CustomState(source)
                override fun newArray(size: Int): Array<CustomState?> = arrayOfNulls(size)
            }
        }
    }
}