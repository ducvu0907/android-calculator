package com.example.caculator_update

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textResult: TextView
    private lateinit var textExpression: TextView
    private var state: Int = 1 // 1 for first operand, 2 for second operand
    private var isAns = false
    private var operation: Int = 0 // 1: +, 2: -, 3: *, 4: /
    private var operand1: Int = 0
    private var operand2: Int = 0

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

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btnCE, R.id.btnC,
            R.id.btnBS, R.id.btnAdd, R.id.btnSubtract,
            R.id.btnMultiply, R.id.btnDivide, R.id.btnEqual,
            R.id.btnAddOrSubTract
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            in R.id.btn0..R.id.btn9 -> addDigit(id - R.id.btn0)
            R.id.btnAdd -> handleOperation(1, " + ")
            R.id.btnSubtract -> handleOperation(2, " - ")
            R.id.btnMultiply -> handleOperation(3, " * ")
            R.id.btnDivide -> handleOperation(4, " / ")
            R.id.btnCE -> clearEntry()
            R.id.btnC -> clearAll()
            R.id.btnBS -> backspace()
            R.id.btnAddOrSubTract -> toggleSign()
            R.id.btnEqual -> calculateResult()
        }
    }

    private fun handleOperation(op: Int, operator: String) {
        isAns = false
        if (state == 1) {
            if (operation != op) {
                textExpression.append(operator)
            }
            operation = op
            state = 2
        } else {
            calculateIntermediateResult()
            textExpression.append(operator)
            operation = op
            state = 2
        }
    }

    private fun calculateIntermediateResult() {
        val result = when (operation) {
            1 -> operand1 + operand2
            2 -> operand1 - operand2
            3 -> operand1 * operand2
            4 -> operand1 / operand2
            else -> 0
        }
        updateResultAndExpression(result)
        operand1 = result
        operand2 = 0
    }

    private fun addDigit(digit: Int) {
        if (isAns) {
            resetCalculator()
        }
        if (state == 1) {
            operand1 = operand1 * 10 + digit
            updateResultAndExpression(operand1)
        } else {
            operand2 = operand2 * 10 + digit
            updateResultAndExpression(operand2)
            textExpression.append("$digit")
        }
    }

    private fun updateResultAndExpression(value: Int) {
        textResult.text = value.toString()
        if (state == 1) {
            textExpression.text = value.toString()
        }
    }

    private fun clearEntry() {
        if (state == 1) {
            resetOperands()
            updateResultAndExpression(0)
        } else {
            operand2 = 0
            updateResultAndExpression(0)
            updateExpression()
        }
    }

    private fun clearAll() {
        resetCalculator()
        updateResultAndExpression(0)
    }

    private fun backspace() {
        if (isAns) {
            clearAll()
        } else if (state == 1 && operand1 != 0) {
            operand1 /= 10
            updateResultAndExpression(operand1)
            removeLastCharacter(textExpression)
        } else if (state == 2 && operand2 != 0) {
            operand2 /= 10
            updateResultAndExpression(operand2)
            removeLastCharacter(textExpression)
        }
    }

    private fun toggleSign() {
        if (state == 1 && operation == 0) {
            operand1 = -operand1
            updateResultAndExpression(operand1)
        } else if (state == 2) {
            operand2 = -operand2
            updateResultAndExpression(operand2)
            updateExpression()
        }
    }

    private fun calculateResult() {
        val result = when (operation) {
            1 -> operand1 + operand2
            2 -> operand1 - operand2
            3 -> operand1 * operand2
            4 -> operand1 / operand2
            else -> operand1
        }
        updateResultAndExpression(result)
        isAns = true
        resetOperands()
    }

    private fun updateExpression() {
        textExpression.text = "$operand1 ${getOperatorSymbol(operation)}"
    }

    private fun getOperatorSymbol(op: Int) = when (op) {
        1 -> "+"
        2 -> "-"
        3 -> "*"
        4 -> "/"
        else -> ""
    }

    private fun resetOperands() {
        operand1 = 0
        operand2 = 0
        operation = 0
    }

    private fun resetCalculator() {
        resetOperands()
        isAns = false
    }

    private fun removeLastCharacter(textView: TextView) {
        val currentText = textView.text.toString()
        if (currentText.isNotEmpty()) {
            textView.text = currentText.dropLast(1)
        }
    }
}
