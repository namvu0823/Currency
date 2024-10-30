package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var sourceAmountEditText: EditText
    private lateinit var targetAmountEditText: EditText
    private lateinit var sourceCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner

    // Giả sử bảng tỷ giá nội bộ
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "VND" to 24000.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceAmountEditText = findViewById(R.id.sourceAmountEditText)
        targetAmountEditText = findViewById(R.id.targetAmountEditText)
        sourceCurrencySpinner = findViewById(R.id.sourceCurrencySpinner)
        targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner)

        sourceAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (sourceAmountEditText.isFocused) {
                    updateTargetAmount()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        targetAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (targetAmountEditText.isFocused) {
                    updateSourceAmount()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        sourceCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateTargetAmount()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        targetCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateSourceAmount()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateTargetAmount() {
        val sourceAmount = sourceAmountEditText.text.toString().toDoubleOrNull()
        if (sourceAmount != null) {
            val sourceCurrency = sourceCurrencySpinner.selectedItem.toString()
            val targetCurrency = targetCurrencySpinner.selectedItem.toString()
            val convertedAmount = convertCurrency(sourceAmount, sourceCurrency, targetCurrency)
            targetAmountEditText.setText(DecimalFormat("#.##").format(convertedAmount))
        } else {
            targetAmountEditText.setText("")
        }
    }

    private fun updateSourceAmount() {
        val targetAmount = targetAmountEditText.text.toString().toDoubleOrNull()
        if (targetAmount != null) {
            val sourceCurrency = sourceCurrencySpinner.selectedItem.toString()
            val targetCurrency = targetCurrencySpinner.selectedItem.toString()
            val convertedAmount = convertCurrency(targetAmount, targetCurrency, sourceCurrency)
            sourceAmountEditText.setText(DecimalFormat("#.##").format(convertedAmount))
        } else {
            sourceAmountEditText.setText("")
        }
    }

    private fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val fromRate = exchangeRates[fromCurrency] ?: 1.0
        val toRate = exchangeRates[toCurrency] ?: 1.0
        return amount * (toRate / fromRate)
    }
}
