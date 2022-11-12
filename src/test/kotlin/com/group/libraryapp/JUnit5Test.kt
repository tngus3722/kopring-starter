package com.group.libraryapp

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JUnit5Test {

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAllTest() {
            println("모든 테스트 시작 전")
        }

        @AfterAll
        @JvmStatic
        fun afterAllTest() {
            println("모든 테스트 종료 후")
        }
    }

    @BeforeEach
    fun beforeEachTest() {
        println("각 테스트 시작 전")
    }

    @AfterEach
    fun afterEachTest() {
        println("각 테스트 종료 후")
    }

    @Test
    fun test1() {
        println("test1")
    }

    @Test
    fun test2() {
        println("test2")
    }
}