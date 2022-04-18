package com.nestdev.trueconftest1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nestdev.trueconftest1.databinding.FragmentAnimatedTextviewBinding


/**
 * Решила создать отдельный фрагмент для анимации
 * Как сделала бы в проекте на будущее
 * для более лёгкой масштабируемости
 *
 * @author Anastasia Drogunova
 */

const val ANIMATION_DELAY = 3000L
const val ANIMATION_DURATION = 3500L


class MainFragment : Fragment() {
    private var _binding: FragmentAnimatedTextviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var toBottomAnimator: ObjectAnimator
    private lateinit var toTopAnimator: ObjectAnimator
    private var layoutPlaceParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    private var animSet = AnimatorSet()
    var isCancelled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimatedTextviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val deviceHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.height()
        } else {
            val point = Point()
            windowManager.defaultDisplay.getSize(point)
            point.y
        }
        animSet.duration = ANIMATION_DURATION
        animSet.startDelay = ANIMATION_DELAY

        with(binding) {
            val height = deviceHeight.toFloat()
            toBottomAnimator = ObjectAnimator.ofFloat(animatedTextView, "translationY", 0f, height - 400f)
            toTopAnimator = ObjectAnimator.ofFloat(animatedTextView, "translationY", 0f, 0f)
            initClickListeners(animatedTextView, mainFragmentFrame, deviceHeight.toFloat())
        }
    }

    private fun convertSpToPixels(sp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
            .toInt() * 2
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners(animatedTextView: TextView, mainFragmentFrame: LinearLayout, height: Float) {
        val pixels = convertSpToPixels(animatedTextView.textSize, requireContext())
        animSet.setTarget(animatedTextView)
        animatedTextView.setOnClickListener {
            isCancelled = true
            animSet.removeAllListeners()
            animSet.cancel()
        }
         mainFragmentFrame.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
             animatedTextView.setTextColor(requireContext().getColor(R.color.clicked_text_color))
             if (motionEvent.action == MotionEvent.ACTION_UP) {
                 isCancelled = true
                 animSet.cancel()
                 applyNewViewCoords(motionEvent, animatedTextView)
                 val newFloatValue =  height - motionEvent.y - pixels
                 val marginTop = motionEvent.y
                 toBottomAnimator.setFloatValues(0f, height - motionEvent.y - pixels)
                 toTopAnimator.setFloatValues(height - motionEvent.y - pixels, 0f - motionEvent.y)
                 animSet.start()
                 animSet.cancel()
                 animSet.startDelay = ANIMATION_DELAY
                    animSet.playSequentially(toBottomAnimator, toTopAnimator)
                    animSet.start()
                 animSet.doOnEnd {
                     if (isCancelled) {
                         toBottomAnimator.setFloatValues(0f - marginTop, newFloatValue)
                         animSet.startDelay = 0
                     }
                    isCancelled = false
                     animSet.start()
                 }
                }
                return@OnTouchListener true
            })
    }

    private fun applyNewViewCoords(motionEvent: MotionEvent, view: View) {
        layoutPlaceParams = view.layoutParams as LinearLayout.LayoutParams
        layoutPlaceParams.topMargin = motionEvent.y.toInt()
        layoutPlaceParams.leftMargin = motionEvent.x.toInt()
        view.layoutParams = layoutPlaceParams
    }

    companion object {
        fun create() = MainFragment()
    }
}
