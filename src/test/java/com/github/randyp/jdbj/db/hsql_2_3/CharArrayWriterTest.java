package com.github.randyp.jdbj.db.hsql_2_3;

import org.hsqldb.lib.CharArrayWriter;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class CharArrayWriterTest {

    @Test
    public void doesThisShitWork() throws Exception {
        final String expected = "abcde";
        final CharArrayWriter writer = new CharArrayWriter(new StringReader(expected), expected.length());
        assertEquals(expected, writer.toString());
    }
}
