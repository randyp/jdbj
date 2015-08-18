package com.github.randyp.jdbj;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class PreparedColumnTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    @Test
    public void isNotSet() throws Exception {
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){

            final PreparedColumn pc = new PreparedColumn(ps, 1);
            assertFalse(pc.isSet());
            pc.setObject(null);
            assertTrue(pc.isSet());
        }
    }


    @Ignore
    @Test
    public void createArray() throws Exception {
        final Long[] expected = {1L};
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){
            {
                final PreparedColumn pc = new PreparedColumn(ps, 1);
                final Array array = pc.createArrayOf("bigint", expected);
                pc.setArray(array);
            }
            try (ResultSet rs = ps.executeQuery()){
                assertTrue(rs.next());
                final Array array = rs.getArray(1);
                assertNotNull(array);
                assertArrayEquals(expected, (Long[]) array.getArray());
                assertFalse(rs.next());
            }
        }
    }

    @Test
    public void createBlob() throws Exception {
        final byte[] expected = "abcde".getBytes();
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){
            {
                final PreparedColumn pc = new PreparedColumn(ps, 1);
                final Blob blob = pc.createBlob();
                blob.setBytes(1, expected);
                pc.setBlob(blob);
            }
            try (ResultSet rs = ps.executeQuery()){
                assertTrue(rs.next());
                final Blob blob = rs.getBlob(1);
                assertNotNull(blob);
                assertArrayEquals(expected, blob.getBytes(0, expected.length));
                assertFalse(rs.next());
            }
        }
    }

    @Test
    public void createClob() throws Exception {
        final String expected = "abcde";
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){
            {
                final PreparedColumn pc = new PreparedColumn(ps, 1);
                final Clob clob = pc.createClob();
                clob.setString(1, expected);
                pc.setClob(clob);
            }
            try (ResultSet rs = ps.executeQuery()){
                assertTrue(rs.next());
                final Clob clob = rs.getClob(1);
                assertNotNull(clob);
                assertEquals(expected, clob.getSubString(1, expected.length()));
                assertFalse(rs.next());
            }
        }
    }

    @Test
    public void createNClob() throws Exception {
        final String expected = "abcde";
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){
            {
                final PreparedColumn pc = new PreparedColumn(ps, 1);
                final NClob clob = pc.createNClob();
                clob.setString(1, expected);
                pc.setClob(clob);
            }
            try (ResultSet rs = ps.executeQuery()){
                assertTrue(rs.next());
                final NClob clob = rs.getNClob(1);
                assertNotNull(clob);
                assertEquals(expected, clob.getSubString(1, expected.length()));
                assertFalse(rs.next());
            }
        }
    }

    @Ignore
    @Test
    public void createSQLXML() throws Exception {
        final String expected = "<a></a>";
        try(Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT ? as binding")){
            {
                final PreparedColumn pc = new PreparedColumn(ps, 1);
                final SQLXML sqlxml = pc.createSQLXML();
                sqlxml.setString(expected);
                pc.setSQLXML(sqlxml);
            }
            try (ResultSet rs = ps.executeQuery()){
                assertTrue(rs.next());
                final SQLXML sqlxml = rs.getSQLXML(1);
                assertNotNull(sqlxml);
                assertEquals(expected, sqlxml.getString());
                assertFalse(rs.next());
            }
        }
    }
}