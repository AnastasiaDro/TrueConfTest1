package com.nestdev.trueconftest1

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
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
class MainFragment : Fragment() {
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var _binding: FragmentAnimatedTextviewBinding? = null
    private val binding get() = _binding!!
//    private val textMovingAnim: Animation =
//        AnimationUtils.loadAnimation(requireContext(), R.anim.up_down_anim)
    //private lateinit var topToBottomAnimation : TranslateAnimation
    private lateinit var toBottomAnimator: ObjectAnimator
    private lateinit var toTopAnimator: ObjectAnimator
    private lateinit var displayMetrics : DisplayMetrics

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

        val wm = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val metric = wManager.currentWindowMetrics
//        val windowInsets = metric.windowInsets
//        val insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())
//        insets.

//
//
        val deviceHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wm.currentWindowMetrics.bounds.height()
        } else {
            val point = Point()
            wm.defaultDisplay.getSize(point)
            point.x
        }


//        toBottomAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.to_bottom_animator) as ObjectAnimator
//        toTopAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.to_top_animator) as ObjectAnimator
//        toBottomAnimator.propertyName()
        //val screenHeight =
        with(binding) {
            val height = deviceHeight.toFloat()
            toBottomAnimator = ObjectAnimator.ofFloat(animatedTextView,"translationY", 0f, height - 400f)
            toTopAnimator = ObjectAnimator.ofFloat(animatedTextView,"translationY",coordsArray[1].toFloat(), 0f)
            println("HERE ${view.height.toFloat()}")
               toBottomAnimator.duration = 70000
                toBottomAnimator.startDelay = 1000
                toTopAnimator.duration = 7000

            toBottomAnimator.addListener {
                onEnd -> toTopAnimator.start()
            }
            toTopAnimator.addListener {
                onEnd -> toBottomAnimator.start()
            }
//            val topToBottomAnimation = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, height - )
//           topToBottomAnimation.duration = 5000
            animatedTextView.setOnClickListener {
                //TODO
            }
            mainFragmentFrame.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    coordsArray[0] = motionEvent.x.toInt()
                    coordsArray[1] = motionEvent.y.toInt()
                    view.performClick()
                    layoutPlaceParams.topMargin = coordsArray[1]
                    layoutPlaceParams.leftMargin = coordsArray[0]
                    animatedTextView.layoutParams = layoutPlaceParams

                    toBottomAnimator = ObjectAnimator.ofFloat(animatedTextView,"translationY", 0f, height - coordsArray[1].toFloat() - 100)
                    toTopAnimator = ObjectAnimator.ofFloat(animatedTextView,"translationY",coordsArray[1].toFloat(), 0f)
                    toBottomAnimator.start()
            //        val topToBottomAnimation = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_PARENT, coordsArray[1].toFloat(), TranslateAnimation.RELATIVE_TO_SELF, height -  coordsArray[1] - 30)
//                    topToBottomAnimation.duration = 5000
//                    animatedTextView.startAnimation(topToBottomAnimation)
                }
                return@OnTouchListener true
            })

        }
    }

    private fun performAnimation() {

    }

    private fun moveToPoint(textView: TextView) {

    }

    companion object {
        fun create() = MainFragment()
    }
}

