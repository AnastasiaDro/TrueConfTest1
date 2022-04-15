package com.nestdev.trueconftest1

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator

class AnimListener(private val nextAnimator: ObjectAnimator) : AnimatorListenerAdapter()
{
    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        super.onAnimationEnd(animation, isReverse)
        when  {
            animationCounter % 2 != 0 -> nextAnimator.setFloatValues(0f - marginTop, newFloatValue)
            animationCounter % 2 == 0  -> nextAnimator.setFloatValues(newFloatValue, 0f - marginTop)
        }
        if (animationCounter == 1) nextAnimator.startDelay = 0
        nextAnimator.start()
        animationCounter++
    }

    companion object {
        var marginTop = 0f
        var newFloatValue = 0f
        var animationCounter = 0
    }
}
