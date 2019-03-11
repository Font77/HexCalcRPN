package com.alloydflanagan.hexcalcrpn.main

/**
 * An interface for test suites that test MainActivity, of which we'll need several.
 *
 * Mainly to make sure I don't forget a test...
 */
interface MainActivityTest {
    fun testClear()
    fun testEntry()
    fun testAdd()
    fun testDivide()
    fun testMultiply()
    fun testSubtract()
    fun testAND()
    fun testOR()
    fun testInvert()
    fun test2sComplement()
    fun testClearNull()
    fun testInvertCurrent()
    fun test2sCompCurrent()
}
