package com.github.anastr.rulerviewsample

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        rulerCalibrationView.onCoefficientChange = {
            newCoefficient -> rulerView.coefficient = newCoefficient
        }

        buttonSave.setOnClickListener {
            MySettings(this).rulerCoefficient = rulerCalibrationView.coefficient
            Snackbar.make(it, "Coefficient Saved", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        buttonOneDimension.setOnClickListener {
            startActivity(Intent(this, OneDimensionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val settings = MySettings(this)
        rulerView.coefficient = settings.rulerCoefficient
        rulerCalibrationView.coefficient = settings.rulerCoefficient
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
