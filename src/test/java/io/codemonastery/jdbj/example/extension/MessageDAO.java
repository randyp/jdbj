package io.codemonastery.jdbj.example.extension;

import io.codemonastery.jdbj.JDBJ;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@SuppressWarnings("unused")
public class MessageDAO {

    private final Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public Message insert(NewMessage newMessage) throws SQLException {
        final Long id = JDBJ.string("INSERT INTO message(content, time) VALUES (:content, :time)")
                .insert(rs -> rs.getLong(1))
                .bindValues(newMessage::bindings)
                .execute(connection).stream().findFirst().get();
        return byId(id).get();
    }

    private Optional<Message> byId(long id) throws SQLException {
        return JDBJ.string("SELECT id, content, time FROM message where id = :id")
                .query()
                .map(Message::from)
                .first()
                .bindLong(":id", id)
                .execute(connection);
    }
}
