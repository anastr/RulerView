package com.github.anastr.rulerviewsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_one_dimension.*

class OneDimensionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_dimension)

        val settings = MySettings(this)

        oneDimensionRulerView.coefficient = settings.rulerCoefficient
    }
}
