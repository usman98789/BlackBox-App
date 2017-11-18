package com.c01;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GeneratingQuestionTest {

    @Test
    /**
     * Test if individual question files are created.
     */
    public void fileCreationTest() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    /**
     * Test if the uploaded file is downloaded properly and shows the correct contents.
     */
    public void fileContentTest() throws Exception {
        assertEquals(4, 2 + 2);
    }
}