package com.example.emptyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val CURRENT_VALUE = "value"
private const val CURRENT_ELEMENT = "number"
private const val CURRENT_EXPRESSION = "expression"
private const val IS_CALCULATION_FINISHED = "isCalculationFinished"

class CalculatorActivity : AppCompatActivity() {
    private var textViewCurrentElement: TextView? = null
    private var textViewCalculation: TextView? = null
    private var buttonC: Button? = null
    private var buttonOk: Button? = null
    private var buttonZero: Button? = null
    private var buttonOne: Button? = null
    private var buttonTwo: Button? = null
    private var buttonThree: Button? = null
    private var buttonFour: Button? = null
    private var buttonFive: Button? = null
    private var buttonSix: Button? = null
    private var buttonSeven: Button? = null
    private var buttonEight: Button? = null
    private var buttonNine: Button? = null
    private var buttonDivision: Button? = null
    private var buttonMultiplication: Button? = null
    private var buttonPlus: Button? = null
    private var buttonMinus: Button? = null
    private var buttonEqual: Button? = null

    private var currentExpression = arrayListOf<String>()
    private var textToShowCurrentNumber = ""
    private var isCalculationFinished = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFields()

        val buttonC = buttonC ?: return
        val buttonOk = buttonOk ?: return
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

        setClickListeners(
            buttonC, buttonZero, buttonOne, buttonTwo,
            buttonThree, buttonFour, buttonFive, buttonSix,
            buttonSeven, buttonEight, buttonNine, buttonDivision,
            buttonMultiplication, buttonMinus, buttonPlus, buttonEqual, buttonOk)


    }
    private fun initializeFields() {
        textViewCurrentElement = findViewById(R.id.text_view_current_element)
        textViewCalculation = findViewById(R.id.text_view_calculation)
        buttonC = findViewById(R.id.button_c)
        buttonOk = findViewById(R.id.button_ok)
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

    private fun setClickListeners(
        buttonC: Button, buttonZero: Button, buttonOne: Button, buttonTwo: Button,
        buttonThree: Button, buttonFour: Button, buttonFive: Button, buttonSix: Button,
        buttonSeven: Button, buttonEight: Button, buttonNine: Button, buttonDivision: Button,
        buttonMultiplication: Button, buttonMinus: Button, buttonPlus: Button, buttonEqual: Button, buttonOk: Button) {
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
            if (textToShowCurrentNumber == "") {
                return@setOnClickListener
            }

            if (isCalculationFinished) {
                val lastNumber = currentExpression[currentExpression.size - 1].toInt()
                val lastOperation = currentExpression[currentExpression.size - 2]

                val expressionResult = calculateOperation(textToShowCurrentNumber.toInt(), lastOperation, lastNumber)
                setCurrentNumberText(expressionResult.toString())
            } else {

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
                    expressionResult =
                        calculateOperation(expressionResult, currentOperation, currentNumber)
                }
                isCalculationFinished = true
                setCurrentNumberText(expressionResult.toString())
            }
        }

        buttonOk.setOnClickListener {
            val intent = Intent()
            intent.putExtra(CURRENT_VALUE, textToShowCurrentNumber)
            setResult(RESULT_OK, intent)
            finish()
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



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_ELEMENT, textViewCurrentElement?.text.toString())
        outState.putSerializable(CURRENT_EXPRESSION, currentExpression)
        outState.putBoolean(IS_CALCULATION_FINISHED, isCalculationFinished)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val number = savedInstanceState.getString(CURRENT_ELEMENT)
        val expression = savedInstanceState.getSerializable(CURRENT_EXPRESSION)
        val isFinished = savedInstanceState.getBoolean(IS_CALCULATION_FINISHED)

        number ?: return
        expression ?: return

        setCurrentNumberText(number)

        currentExpression = expression as ArrayList<String>
        displayExpression()

        isCalculationFinished = isFinished
    }
}
