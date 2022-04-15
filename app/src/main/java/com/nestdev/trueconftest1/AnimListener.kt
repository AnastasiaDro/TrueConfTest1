package com.nestdev.trueconftest1

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator

class AnimListener(private val nextAnimator: ObjectAnimator, private val animatorCode: AnimatorCode) : AnimatorListenerAdapter()
{
    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        super.onAnimationEnd(animation, isReverse)
        when (animatorCode) {
            AnimatorCode.TO_TOP -> nextAnimator.setFloatValues(0f - marginTop, newFloatValue)
            AnimatorCode.TO_BOTTOM  -> nextAnimator.setFloatValues(newFloatValue, 0f - marginTop)
        }
        if (animatorCode  == AnimatorCode.TO_TOP) nextAnimator.startDelay = 0
        nextAnimator.start()
    //    animationCounter++
    }

    companion object {
        var marginTop = 0f
        var newFloatValue = 0f
       // var animationCounter = 0
    }
}
