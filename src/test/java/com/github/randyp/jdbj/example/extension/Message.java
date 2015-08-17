package com.github.randyp.jdbj.example.extension;

import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Message extends NewMessage {

    public static Message from(ResultSet rs) throws SQLException {
        return new Message(rs.getLong("id"), rs.getString("content"), new DateTime(rs.getTimestamp("time").getTime()));
    }

    private final long id;

    public Message(long id, String content, DateTime time) {
        super(content,time);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
