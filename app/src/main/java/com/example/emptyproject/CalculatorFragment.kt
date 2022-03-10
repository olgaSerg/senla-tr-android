package com.example.emptyproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

private const val NUMBER = "number"
private const val EXPRESSION = "expression"
private const val IS_CALCULATION_FINISHED = "isCalculationFinished"

class CalculatorFragment : Fragment() {

    private var textViewCurrentElement: TextView? = null
    private var textViewCalculation: TextView? = null
    private var buttonC: Button? = null
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
    private var expressionResult = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        setClickListeners(
            buttonC, buttonZero, buttonOne, buttonTwo,
            buttonThree, buttonFour, buttonFive, buttonSix,
            buttonSeven, buttonEight, buttonNine, buttonDivision,
            buttonMultiplication, buttonMinus, buttonPlus, buttonEqual)
    }



//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(NUMBER, textViewCurrentElement?.text.toString())
//        outState.putSerializable(EXPRESSION, currentExpression)
//        outState.putBoolean(IS_CALCULATION_FINISHED, isCalculationFinished)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        val number = savedInstanceState.getString(NUMBER)
//        val expression = savedInstanceState.getSerializable(EXPRESSION) as ArrayList<String>
//        val isFinished = savedInstanceState.getBoolean(IS_CALCULATION_FINISHED)
//
//        number ?: return
//
//        setCurrentNumberText(number)
//
//        currentExpression = expression
//        displayExpression()
//
//        isCalculationFinished = isFinished
//    }

    private fun initializeFields() {
        textViewCurrentElement = view?.findViewById(R.id.text_view_current_element)
        textViewCalculation = view?.findViewById(R.id.text_view_calculation)
        buttonC = view?.findViewById(R.id.button_c)
        buttonZero = view?.findViewById(R.id.button_zero)
        buttonOne = view?.findViewById(R.id.button_one)
        buttonTwo = view?.findViewById(R.id.button_two)
        buttonThree = view?.findViewById(R.id.button_three)
        buttonFour = view?.findViewById(R.id.button_four)
        buttonFive = view?.findViewById(R.id.button_five)
        buttonSix = view?.findViewById(R.id.button_six)
        buttonSeven = view?.findViewById(R.id.button_seven)
        buttonEight = view?.findViewById(R.id.button_eight)
        buttonNine = view?.findViewById(R.id.button_nine)
        buttonDivision = view?.findViewById(R.id.button_division)
        buttonMultiplication = view?.findViewById(R.id.button_multiplication)
        buttonPlus = view?.findViewById(R.id.button_plus)
        buttonMinus = view?.findViewById(R.id.button_minus)
        buttonEqual = view?.findViewById(R.id.button_equal)
    }

    private fun setClickListeners(
        buttonC: Button, buttonZero: Button, buttonOne: Button, buttonTwo: Button,
        buttonThree: Button, buttonFour: Button, buttonFive: Button, buttonSix: Button,
        buttonSeven: Button, buttonEight: Button, buttonNine: Button, buttonDivision: Button,
        buttonMultiplication: Button, buttonMinus: Button, buttonPlus: Button, buttonEqual: Button
    ) {
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
            logicCalculateExpression()
        }
    }

    private fun logicCalculateExpression() {
        if (textToShowCurrentNumber == "" || isCalculationFinished) {
            return
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
                return
            }
            expressionResult =
                calculateOperation(expressionResult, currentOperation, currentNumber)
        }
        isCalculationFinished = true
        setCurrentNumberText(expressionResult.toString())
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

}