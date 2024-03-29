package io.github.randyp.jdbj.example.extension;

import io.github.randyp.jdbj.SmartResult;
import org.joda.time.DateTime;

import java.sql.SQLException;

import static io.github.randyp.jdbj.example.extension.ResultSetGetters.getDateTime;

public class Message extends NewMessage {

    public static Message from(SmartResult rs) throws SQLException {
        return new Message(
                rs.getLongPrimitive("id"),
                rs.getString("content"),
                getDateTime(rs, "time")
        );
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
