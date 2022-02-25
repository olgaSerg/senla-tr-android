package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val NUMBER = "number"
private const val EXPRESSION = "expression"
private const val IS_CALCULATION_FINISHED = "isCalculationFinished"

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

    var currentExpression = arrayListOf<String>()
    var textToShowCurrentNumber = ""
    var isCalculationFinished = false
    var expressionResult = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFields()

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
            handleDigitPressed(buttonZero)
        }

        buttonOne.setOnClickListener {
            handleDigitPressed(buttonOne)
        }

        buttonTwo.setOnClickListener {
            handleDigitPressed(buttonTwo)
        }

        buttonThree.setOnClickListener {
            handleDigitPressed(buttonThree)
        }

        buttonFour.setOnClickListener {
            handleDigitPressed(buttonFour)
        }

        buttonFive.setOnClickListener {
            handleDigitPressed(buttonFive)
        }

        buttonSix.setOnClickListener {
            handleDigitPressed(buttonSix)
        }

        buttonSeven.setOnClickListener {
            handleDigitPressed(buttonSeven)
        }

        buttonEight.setOnClickListener {
            handleDigitPressed(buttonEight)
        }

        buttonNine.setOnClickListener {
            handleDigitPressed(buttonNine)
        }

        buttonDivision.setOnClickListener {
            handleOperationPressed(buttonDivision)
        }

        buttonMultiplication.setOnClickListener {
            handleOperationPressed(buttonMultiplication)
        }

        buttonPlus.setOnClickListener {
            handleOperationPressed(buttonPlus)
        }

        buttonMinus.setOnClickListener {
            handleOperationPressed(buttonMinus)
        }

        buttonEqual.setOnClickListener {
            if (textToShowCurrentNumber == "" || isCalculationFinished) {
                return@setOnClickListener
            }

            isCalculationFinished = true

            currentExpression.add(textToShowCurrentNumber)
            displayExpression()

            expressionResult = currentExpression[0].toInt()
            for (i in 1 until currentExpression.size step 2) {
                val currentOperation = currentExpression[i]
                val currentNumber = currentExpression[i + 1].toInt()

                if (currentOperation == "/" && currentNumber == 0) {
                    setCurrentNumberText("ERROR")
                    return@setOnClickListener
                }
                expressionResult =
                    calculateOperation(expressionResult, currentOperation, currentNumber)
            }
            isCalculationFinished = true
            setCurrentNumberText(expressionResult.toString())
        }
    }

    private fun setCurrentNumberText(currentText: String) {
        textToShowCurrentNumber = currentText
        textViewCurrentElement?.text = textToShowCurrentNumber
    }

    private fun displayExpression() {
        textViewCalculation?.text = currentExpression.joinToString(separator = " ")
    }

    private fun handleDigitPressed(digitButton: Button) {
        if (isCalculationFinished) {
            handleClearPressed()
        }

        val pressedDigit = digitButton.text.toString()
        setCurrentNumberText(textToShowCurrentNumber + pressedDigit)
    }

    private fun handleClearPressed() {
        isCalculationFinished = false

        setCurrentNumberText("")

        currentExpression.clear()
        displayExpression()
    }

    private fun handleOperationPressed(operationButton: Button) {
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

    private fun calculateOperation(leftOperand: Int, operation: String, rightOperand: Int): Int {
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

    private fun initializeFields() {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(NUMBER, textViewCurrentElement?.text.toString())
        outState.putSerializable(EXPRESSION, currentExpression)
        outState.putBoolean(IS_CALCULATION_FINISHED, isCalculationFinished)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val number = savedInstanceState.getString(NUMBER)
        val expression = savedInstanceState.getSerializable(EXPRESSION)
        val isFinished = savedInstanceState.getBoolean(IS_CALCULATION_FINISHED)

        number ?: return
        expression ?: return

        setCurrentNumberText(number)

        currentExpression = expression as ArrayList<String>
        displayExpression()

        isCalculationFinished = isFinished
    }
}
