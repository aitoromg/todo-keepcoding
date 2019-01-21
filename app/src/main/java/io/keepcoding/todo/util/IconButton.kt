package io.keepcoding.todo.util

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.TintableBackgroundView
import androidx.core.widget.ImageViewCompat
import io.keepcoding.todo.R

class IconButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    deffStyleAttr: Int = 0
): AppCompatImageButton(context, attributeSet, deffStyleAttr), TintableBackgroundView {

    private var touchableArea = 8

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        changeTouchableArea(touchableArea)
    }

    fun setColorDrawable(color: Int) {
        DrawableCompat.setTint(drawable, color)
    }

}