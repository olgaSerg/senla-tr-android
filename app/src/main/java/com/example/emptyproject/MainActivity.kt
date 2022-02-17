package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.lang.IllegalArgumentException
import kotlin.contracts.ReturnsNotNull

class MainActivity : AppCompatActivity() {
    var textViewCurrentElement: TextView? = null
    var textViewCalculation: TextView? = null
    var buttonC: Button? = null
    var buttonZero: Button? = null
    var buttonOne: Button? = null
    var buttonTwo: Button? = null
    var buttonThree: Button? = null
    var buttonFour: Button? = null
    var buttonFive: Button? = null
    var buttonSix: Button? = null
    var buttonSeven: Button? = null
    var buttonEight: Button? = null
    var buttonNine: Button? = null
    var buttonDivision: Button? = null
    var buttonMultiplication: Button? = null
    var buttonPlus: Button? = null
    var buttonMinus: Button? = null
    var buttonEqual: Button? = null

    var currentExpression = mutableListOf<String>()
    var textToShowCurrentNumber = ""
    var isCalculationFinished = false

    fun setCurrentNumberText(currentText: String) {
        textToShowCurrentNumber = currentText
        textViewCurrentElement?.text = textToShowCurrentNumber
    }

    fun displayExpression() {
        textViewCalculation?.text = currentExpression.joinToString(separator = " ")
    }

    fun handleDigitPressed(digitButton: Button) {
        if (isCalculationFinished) {
            handleClearPressed()
        }

        val pressedDigit = digitButton.text.toString()
        setCurrentNumberText(textToShowCurrentNumber + pressedDigit)
    }

    fun handleClearPressed() {
        isCalculationFinished = false

        setCurrentNumberText("")

        currentExpression.clear()
        displayExpression()
    }

    fun handleOperationPressed(operationButton: Button) {
        if (isCalculationFinished) {
            handleClearPressed()
        }

        if (textToShowCurrentNumber == "") {
            return
        }

        val operation = operationButton.text.toString()
        currentExpression.add(textToShowCurrentNumber)
        currentExpression.add(operation)
        displayExpression()
        setCurrentNumberText("")
    }

    fun calculateOperation(leftOperand: Int, operation: String, rightOperand: Int): Int {
        when (operation) {
            "+" -> {
                return leftOperand + rightOperand
            }
            "-" -> {
                return leftOperand - rightOperand
            }
            "*" -> {
                return leftOperand * rightOperand
            }
            "/" -> {
                return leftOperand / rightOperand
            }
        }
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewCurrentElement = findViewById(R.id.text_view_current_element)
        textViewCalculation = findViewById(R.id.text_view_calculation)
        buttonC = findViewById(R.id.button_c)
        buttonZero = findViewById(R.id.button_zero)
        buttonOne = findViewById(R.id.button_one)
        buttonTwo = findViewById(R.id.button_two)
        buttonThree = findViewById(R.id.button_three)
        buttonFour = findViewById(R.id.button_four)
        buttonFive = findViewById(R.id.button_five)
        buttonSix = findViewById(R.id.button_six)
        buttonSeven = findViewById(R.id.button_seven)
        buttonEight = findViewById(R.id.button_eight)
        buttonNine = findViewById(R.id.button_nine)
        buttonDivision = findViewById(R.id.button_division)
        buttonMultiplication = findViewById(R.id.button_multiplication)
        buttonPlus = findViewById(R.id.button_plus)
        buttonMinus = findViewById(R.id.button_minus)
        buttonEqual = findViewById(R.id.button_equal)

        val buttonC = buttonC ?: return
        val buttonZero = buttonZero ?: return
        val buttonOne = buttonOne ?: return
        val buttonTwo = buttonTwo ?: return
        val buttonThree = buttonThree ?: return
        val buttonFour = buttonFour ?: return
        val buttonFive = buttonFive ?: return
        val buttonSix = buttonSix ?: return
        val buttonSeven = buttonSeven ?: return
        val buttonEight = buttonEight ?: return
        val buttonNine = buttonNine ?: return
        val buttonDivision = buttonDivision ?: return
        val buttonMultiplication = buttonMultiplication ?: return
        val buttonPlus = buttonPlus ?: return
        val buttonMinus = buttonMinus ?: return
        val buttonEqual = buttonEqual ?: return

        buttonC.setOnClickListener {
            handleClearPressed()
        }

        buttonZero.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonOne.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonTwo.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonThree.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonFour.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonFive.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonSix.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonSeven.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonEight.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonNine.setOnClickListener {
            handleDigitPressed(it as Button)
        }

        buttonDivision.setOnClickListener {
            handleOperationPressed(it as Button)
        }

        buttonMultiplication.setOnClickListener {
            handleOperationPressed(it as Button)
        }

        buttonPlus.setOnClickListener {
            handleOperationPressed(it as Button)
        }

        buttonMinus.setOnClickListener {
            handleOperationPressed(it as Button)
        }

        buttonEqual.setOnClickListener {
            if (textToShowCurrentNumber == "" || isCalculationFinished) {
                return@setOnClickListener
            }
            isCalculationFinished = true

            currentExpression.add(textToShowCurrentNumber)
            displayExpression()

            var expressionResult = currentExpression[0].toInt()
            for (i in 1 until currentExpression.size step 2) {
                val currentOperation = currentExpression[i]
                val currentNumber = currentExpression[i + 1].toInt()

                if (currentOperation == "/" && currentNumber == 0) {
                    setCurrentNumberText("ERROR")
                    return@setOnClickListener
                }

                expressionResult = calculateOperation(expressionResult, currentOperation, currentNumber)
            }

            setCurrentNumberText(expressionResult.toString())
        }
    }
}
