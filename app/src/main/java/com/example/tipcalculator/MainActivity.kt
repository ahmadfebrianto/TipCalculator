package com.example.tipcalculator

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.tipcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvFinalCost.text = getString(R.string.final_cost, "0.00")
        binding.edtCost.addTextChangedListener {
            val value = binding.edtCost.text
            if (value != null) {
                binding.btCalculate.isEnabled = value.isNotEmpty()
            }
        }
        binding.btCalculate.setOnClickListener { calculateTip() }
        binding.tilCost.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
    }


    private fun calculateTip() {
        val costOfService = binding.edtCost.text.toString().toDouble()
        val tipPercentage = when (binding.rgServiceQuality.checkedRadioButtonId) {
            binding.rbServiceAmazing.id -> 0.2
            binding.rbServiceGood.id -> 0.18
            else -> 0.15
        }
        var tip = costOfService * tipPercentage
        if (binding.swRoundUpTip.isChecked) {
            tip = kotlin.math.ceil(tip)
        }

        val finalCost = (costOfService + tip).round()
        binding.tvFinalCost.text = getString(R.string.final_cost, finalCost.toString())
    }

    private fun Double.round(decimals: Int = 2): Double {
        return "%.${decimals}f".format(this).toDouble()
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}