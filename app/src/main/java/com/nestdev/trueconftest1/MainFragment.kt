package com.nestdev.trueconftest1

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.whenResumed
import com.nestdev.trueconftest1.R
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
    private val textMovingAnim: Animation =
        AnimationUtils.loadAnimation(requireContext(), R.anim.up_down_anim)

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


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
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

