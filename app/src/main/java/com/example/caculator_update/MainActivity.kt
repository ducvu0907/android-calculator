package com.example.caculator_update

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var textResult: TextView
    var state: Int = 1
    var isAns = false
    var op: Int? = null
    var op1: Int = 0
    var op2: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textResult = findViewById(R.id.text_result)

        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btnCE, R.id.btnC,
            R.id.btnBS, R.id.btnAdd, R.id.btnEqual,
            R.id.btnDivide, R.id.btnSubtract, R.id.btnMultiply,
            R.id.btnAddOrSubTract
        )

        buttonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        val id = view?.id
        when (id) {
            in R.id.btn0..R.id.btn9 -> {
                // Ensure 'id' is not null before performing the operation
                id?.let { addDigit(it - R.id.btn0) }
            }
            R.id.btnAdd -> handleOperation(1)
            R.id.btnSubtract -> handleOperation(2)
            R.id.btnMultiply -> handleOperation(3)
            R.id.btnDivide -> handleOperation(4)
            R.id.btnCE -> clear()
            R.id.btnC -> reset()
            R.id.btnBS -> backspace()
            R.id.btnAddOrSubTract -> toggleSign()
            R.id.btnEqual -> calculateResult()
        }
    }

    private fun handleOperation(operation: Int) {
        isAns = false
        if (state == 1) {
            op = operation
            state = 2
        } else if (state == 2) {
            val result = calculate()
            textResult.text = "$result"
            op1 = result
            op2 = 0
            op = operation
            state = 2
        }
    }

    private fun calculate(): Int {
        return when (op) {
            1 -> op1 + op2
            2 -> op1 - op2
            3 -> op1 * op2
            4 -> op1 / op2
            else -> op1
        }
    }

    private fun getOperationSymbol(op: Int): String {
        return when (op) {
            1 -> "+"
            2 -> "-"
            3 -> "*"
            4 -> "/"
            else -> ""
        }
    }

    private fun clear() {
        if (state == 1) {
            resetValues()
            textResult.text = "0"
        } else {
            op2 = 0
            textResult.text = "0"
        }
    }

    private fun reset() {
        state = 1
        resetValues()
        textResult.text = "$op1"
    }

    private fun backspace() {
        if (isAns) {
            reset()
        } else if (state == 1 && op1 != 0) {
            op1 /= 10
            textResult.text = "$op1"
        } else if (state == 2 && op2 != 0) {
            op2 /= 10
            textResult.text = "$op2"
        }
    }

    private fun toggleSign() {
        if (state == 1) {
            if (op == null) {
                op1 = -op1
                textResult.text = "$op1"
            }
        } else {
            op2 = -op2
            textResult.text = "$op2"
        }
    }

    private fun calculateResult() {
        val result = calculate()
        textResult.text = "$result"
        isAns = true
        state = 1
        op1 = result
        op2 = 0
        op = null
    }

    private fun addDigit(c: Int) {
        if (isAns) {
            state = 1
            resetValues()
            isAns = false
        }
        if (state == 1) {
            op1 = op1 * 10 + c
            textResult.text = "$op1"
        } else {
            op2 = op2 * 10 + c
            textResult.text = "$op2"
        }
    }

    private fun resetValues() {
        op1 = 0
        op2 = 0
        op = null
    }
}
