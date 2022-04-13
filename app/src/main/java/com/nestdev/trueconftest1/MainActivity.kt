package com.nestdev.trueconftest1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchMainFragment()
    }


    private fun launchMainFragment() {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true) //TODO  погуглить про свойство
            replace(R.id.main_activity_container, MainFragment.create())
            commitAllowingStateLoss()
        }
    }
}