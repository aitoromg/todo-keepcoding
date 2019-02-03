package io.keepcoding.todo.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.TintableBackgroundView
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
        val newDrawable = ContextCompat.getDrawable(context, R.drawable.ic_priority)

        newDrawable?.let {
            val finalDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(finalDrawable.mutate(), color)
            setImageDrawable(finalDrawable)
        }
    }

}