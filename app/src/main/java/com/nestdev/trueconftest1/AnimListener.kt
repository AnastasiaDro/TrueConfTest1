package com.nestdev.trueconftest1

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView

class AnimListener(private val nextAnimator: ObjectAnimator, private val animatorCode: AnimatorCode, private val textView: TextView) : AnimatorListenerAdapter()
{
    var params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    override fun onAnimationCancel(animation: Animator?) {
       isCanceled = true
        println("CANCELLED")
    }
    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        super.onAnimationEnd(animation, isReverse)
        when (animatorCode) {
            AnimatorCode.TO_TOP -> nextAnimator.setFloatValues(0f - marginTop, newFloatValue)
            AnimatorCode.TO_BOTTOM  -> nextAnimator.setFloatValues(newFloatValue, 0f - marginTop)
        }
        if (animatorCode  == AnimatorCode.TO_TOP) nextAnimator.startDelay = 0
        if (!isCanceled)
            nextAnimator.start()
    }

    companion object {
        var marginTop = 0f
        var newFloatValue = 0f
        var isCanceled = false
    }
}
