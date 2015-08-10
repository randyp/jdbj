package oof.jdbc;

import java.util.stream.Stream;

public interface StreamCallback<R, STREAM_RESULT> {

    STREAM_RESULT stream(Stream<R> stream);

}
