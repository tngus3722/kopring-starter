package com.group.libraryapp.calculator

fun main() {
    val calculatorTest: CalculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multipleTest()
    calculatorTest.divideExceptionTest()
}

internal class CalculatorTest {

    fun addTest() {
        // given
        val calculator: Calculator = Calculator(5)

        // when
        calculator.add(3)

        // then
        if (calculator.number != 8) {
            throw IllegalStateException("not equal add")
        }
    }

    fun minusTest() {
        // given
        val calculator: Calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        if (calculator.number != 2) {
            throw IllegalStateException("not equal add")
        }
    }

    fun multipleTest() {
        // given
        val calculator: Calculator = Calculator(5)

        // when
        calculator.multiple(3)

        // then
        if (calculator.number != 15) {
            throw IllegalStateException("not equal add")
        }
    }

    fun divideTest() {
        // given
        val calculator: Calculator = Calculator(5)

        // when
        calculator.divide(3)

        // then
        if (calculator.number != 1) {
            throw IllegalStateException("not equal add")
        }
    }

    fun divideExceptionTest() {
        // given
        val calculator: Calculator = Calculator(5)

        // when
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            return
        } catch (e: Exception) {
            throw IllegalStateException("not expected Exception")
        }

        throw IllegalStateException("not expected Exception")
    }
}