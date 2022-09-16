package com.wafflestudio.seminar

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import com.wafflestudio.seminar.test.main as doTheTest

@SpringBootTest
class SolutionTest {

    @ParameterizedTest
    @DisplayName("3번문제를 코틀린으로 풀어낼 수 있다")
    @ValueSource(ints = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun test(idx: Int) {
        // given
        val outputStream = ByteArrayOutputStream()
        val testCaseInputStream = readTc(idx).inputStream()

        // when
        testCaseInputStream.use { inputStream ->
            PrintStream(outputStream).use { printStream ->
                System.setIn(inputStream)
                System.setOut(printStream)
                doTheTest()
            }
        }

        // then
        val resultString = outputStream.toString().trim()
        assertThat(resultString).isEqualTo(readSol(idx).trim())
    }

    private fun readTc(idx: Int): File =
        ClassPathResource("prob3_testcases/prob3_tc$idx").file

    private fun readSol(idx: Int): String =
        ClassPathResource("prob3_solutions/prob3_sol$idx").file.readText()
}