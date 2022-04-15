package com.nestdev.trueconftest1

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
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

const val ANIMATION_DELAY = 5000L
const val ANIMATION_DURATION = 3500L


class MainFragment : Fragment() {
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var _binding: FragmentAnimatedTextviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var toBottomAnimator: ObjectAnimator
    private lateinit var toTopAnimator: ObjectAnimator
    private lateinit var displayMetrics: DisplayMetrics
    private val coordsArray = Array<Int>(2) { 0 }
    private var layoutPlaceParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

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

        with(binding) {
            val height = deviceHeight.toFloat()
            toBottomAnimator =
                ObjectAnimator.ofFloat(animatedTextView, "translationY", 0f, height - 400f)
            toTopAnimator = ObjectAnimator.ofFloat(
                animatedTextView,
                "translationY",
                0f,
                0f
            )


            toBottomAnimator.addListener(AnimListener(toTopAnimator))
            toTopAnimator.addListener(AnimListener(toBottomAnimator))
            toBottomAnimator.duration = ANIMATION_DURATION
            toTopAnimator.duration = ANIMATION_DURATION
            toBottomAnimator.startDelay = ANIMATION_DELAY

            animatedTextView.setOnClickListener {
                toBottomAnimator.pause()
                toTopAnimator.pause()
            }

            mainFragmentFrame.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                animatedTextView.setTextColor(requireContext().getColor(R.color.clicked_text_color))
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    AnimListener.animationCounter = 0
                    view.performClick()
                    layoutPlaceParams.topMargin = motionEvent.y.toInt()
                    layoutPlaceParams.leftMargin = motionEvent.x.toInt()
                    animatedTextView.layoutParams = layoutPlaceParams
                    val pixels = convertSpToPixels(animatedTextView.textSize, requireContext())
                    AnimListener.newFloatValue = height - motionEvent.y - pixels*2
                    AnimListener.marginTop = motionEvent.y
                    toBottomAnimator.setFloatValues(0f, height - motionEvent.y - pixels*2)//convertSpToPixels(animatedTextView.textSize, requireContext()))
                    toTopAnimator.setFloatValues(height - motionEvent.y - pixels*2, 0f - motionEvent.y)
                    toBottomAnimator.start()
                    toBottomAnimator.startDelay = 0
                }
                return@OnTouchListener true
            })
        }
    }

    fun convertSpToPixels(sp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
            .toInt()
    }

    companion object {
        fun create() = MainFragment()
    }
}
