package com.example.smartattendancepro.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendancepro.R

class Home : AppCompatActivity() {

    private var txtatt: TextView? = null
    private var txtsys: TextView? = null
    private var relativeLayout: RelativeLayout? = null
    private var txtAnimation: Animation? = null
    private var layoutAnimation: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        txtAnimation = AnimationUtils.loadAnimation(this, R.anim.fall_down)
        layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)

        txtatt = findViewById(R.id.attendance)
        txtsys = findViewById(R.id.system)
        relativeLayout = findViewById(R.id.relMain)

        // First animation
        Handler(Looper.getMainLooper()).postDelayed({
            relativeLayout?.visibility = View.VISIBLE
            relativeLayout?.startAnimation(layoutAnimation)

            Handler(Looper.getMainLooper()).postDelayed({
                txtatt?.visibility = View.VISIBLE
                txtsys?.visibility = View.VISIBLE

                txtatt?.startAnimation(txtAnimation)
                txtsys?.startAnimation(txtAnimation)
            }, 500)

        }, 1000)

        // Navigate to LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@Home, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, 6000)
    }
}