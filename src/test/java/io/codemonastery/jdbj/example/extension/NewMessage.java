package io.codemonastery.jdbj.example.extension;

import io.codemonastery.jdbj.ValueBindings;
import org.joda.time.DateTime;

public class NewMessage {

    private final String content;
    private final DateTime time;

    public NewMessage(String content, DateTime time) {
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        if (time == null) {
            throw new IllegalArgumentException("time cannot be null");
        }
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public DateTime getTime() {
        return time;
    }

    public ValueBindings bindings() {
        return new ValueBindings()
                .bindString(":content", content)
                .bind(":time", new DateTimeBinding(time));
    }
}
