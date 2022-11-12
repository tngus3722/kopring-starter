package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CalculatorTest {

    @Test
    fun addTest() {
        // given
        val calculator: Calculator = Calculator(5)
        // when
        calculator.add(3)
        // then
        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    fun minusTest() {
        // given
        val calculator: Calculator = Calculator(5)
        // when
        calculator.minus(3)
        // then
        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun multipleTest() {
        // given
        val calculator: Calculator = Calculator(5)
        // when
        calculator.multiple(3)
        // then
        assertThat(calculator.number).isEqualTo(15)
    }

    @Test
    fun divideTest() {
        // given
        val calculator: Calculator = Calculator(5)
        // when
        calculator.divide(3)
        // then
        assertThat(calculator.number).isEqualTo(1)
    }

    @Test
    fun divideExceptionTest() {
        // given
        val calculator: Calculator = Calculator(5)
        // when & then
        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.apply {
            assertThat(this.message).isEqualTo("can not divide zero")
        }

    }
}