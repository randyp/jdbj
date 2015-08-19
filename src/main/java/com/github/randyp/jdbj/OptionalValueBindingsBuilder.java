package com.github.randyp.jdbj;

import com.github.randyp.jdbj.optional.OptionalBoolean;
import com.github.randyp.jdbj.optional.OptionalByte;
import com.github.randyp.jdbj.optional.OptionalFloat;
import com.github.randyp.jdbj.optional.OptionalShort;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public interface OptionalValueBindingsBuilder<E> extends DefaultValueBindingsBuilder<E> {
    
    E requireDefaultedBindingForOptional(String name);

    default E bindOptionalArray(String name, Optional<Array> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindArray(name, x.get()) : required;
    }

    default E bindOptionalAsciiStream(String name, Optional<InputStream> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindAsciiStream(name, x.get()) : required;
    }

    default E bindOptionalAsciiStream(String name, Optional<InputStream> x, int length) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindAsciiStream(name, x.get(), length) : required;
    }

    default E bindOptionalAsciiStream(String name, Optional<InputStream> x, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindAsciiStream(name, x.get(), length) : required;
    }

    default E bindOptionalBigDecimal(String name, Optional<BigDecimal> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBigDecimal(name, x.get()) : required;
    }

    default E bindOptionalBinaryStream(String name, Optional<InputStream> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBinaryStream(name, x.get()) : required;
    }

    default E bindOptionalBinaryStream(String name, Optional<InputStream> x, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBinaryStream(name, x.get(), length) : required;
    }

    default E bindOptionalBinaryStream(String name, Optional<InputStream> x, int length) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBinaryStream(name, x.get(), length) : required;
    }

    default E bindOptionalBlob(String name, Optional<InputStream> inputStream) {
        final E required = requireDefaultedBindingForOptional(name);
        return inputStream.isPresent() ? bindBlob(name, inputStream.get()) : required;
    }

    default E bindOptionalBlob(String name, Optional<InputStream> inputStream, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return inputStream.isPresent() ? bindBlob(name, inputStream.get(), length) : required;
    }

    default E bindOptionalBoolean(String name, Optional<Boolean> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBooleanPrimitive(name, x.get()) : required;
    }

    default E bindOptionalBoolean(String name, OptionalBoolean x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBooleanPrimitive(name, x.getAsBoolean()) : required;
    }

    default E bindOptionalByte(String name, Optional<Byte> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBytePrimitive(name, x.get()) : required;
    }

    default E bindOptionalByte(String name, OptionalByte x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindBytePrimitive(name, x.getAsByte()) : required;
    }

    default E bindOptionalByteArray(String name, Optional<byte[]> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindByteArray(name, x.get()) : required;
    }

    default E bindOptionalCharacterStream(String name, Optional<Reader> reader) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindCharacterStream(name, reader.get()) : required;
    }

    default E bindOptionalCharacterStream(String name, Optional<Reader> reader, int length) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindCharacterStream(name, reader.get(), length) : required;
    }

    default E bindOptionalCharacterStream(String name, Optional<Reader> reader, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindCharacterStream(name, reader.get(), length) : required;
    }

    default E bindOptionalClob(String name, Optional<Reader> reader) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindClob(name, reader.get()) : required;
    }

    default E bindOptionalClob(String name, Optional<Reader> reader, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindClob(name, reader.get(), length) : required;
    }

    default E bindOptionalDate(String name, Optional<Date> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindDate(name, x.get()) : required;
    }

    default E bindOptionalDate(String name, Optional<Date> x, Calendar cal) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindDate(name, x.get(), cal) : required;
    }

    default E bindOptionalDouble(String name, Optional<Double> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindDoublePrimitive(name, x.get()) : required;
    }

    default E bindOptionalDouble(String name, OptionalDouble x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindDoublePrimitive(name, x.getAsDouble()) : required;
    }

    default E bindOptionalFloat(String name, Optional<Float> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindFloatPrimitive(name, x.get()) : required;
    }

    default E bindOptionalFloat(String name, OptionalFloat x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindFloatPrimitive(name, x.getAsFloat()) : required;
    }

    default E bindOptionalInteger(String name, Optional<Integer> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindIntegerPrimitive(name, x.get()) : required;
    }

    default E bindOptionalInteger(String name, OptionalInt x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindIntegerPrimitive(name, x.getAsInt()) : required;
    }

    default E bindOptionalInt(String name, Optional<Integer> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindIntegerPrimitive(name, x.get()) : required;
    }

    default E bindOptionalInt(String name, OptionalInt x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindIntegerPrimitive(name, x.getAsInt()) : required;
    }

    default E bindOptionalLong(String name, Optional<Long> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindLongPrimitive(name, x.get()) : required;
    }

    default E bindOptionalLong(String name, OptionalLong x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindLongPrimitive(name, x.getAsLong()) : required;
    }

    default E bindOptionalNCharacterStream(String name, Optional<Reader> reader) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindNCharacterStream(name, reader.get()) : required;
    }

    default E bindOptionalNCharacterStream(String name, Optional<Reader> reader, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindNCharacterStream(name, reader.get(), length) : required;
    }

    default E bindOptionalNClob(String name, Optional<Reader> reader) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindNClob(name, reader.get()) : required;
    }

    default E bindOptionalNClob(String name, Optional<Reader> reader, long length) {
        final E required = requireDefaultedBindingForOptional(name);
        return reader.isPresent() ? bindNClob(name, reader.get(), length) : required;
    }

    default E bindOptionalNString(String name, Optional<String> value) {
        final E required = requireDefaultedBindingForOptional(name);
        return value.isPresent() ? bindNString(name, value.get()) : required;
    }

    default E bindOptionalObject(String name, Optional<Object> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindObject(name, x.get()) : required;
    }

    default E bindOptionalObject(String name, Optional<Object> x, int targetSqlType) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindObject(name, x.get(), targetSqlType) : required;
    }

    default E bindOptionalObject(String name, Optional<Object> x, SQLType targetSqlType) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindObject(name, x.get(), targetSqlType) : required;
    }

    default E bindOptionalObject(String name, Optional<Object> x, int targetSqlType, int scaleOrLength) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindObject(name, x.get(), targetSqlType, scaleOrLength) : required;
    }

    default E bindOptionalObject(String name, Optional<Object> x, SQLType targetSqlType, int scaleOrLength) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindObject(name, x.get(), targetSqlType, scaleOrLength) : required;
    }

    default E bindOptionalShort(String name, Optional<Short> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindShortPrimitive(name, x.get()) : required;
    }

    default E bindOptionalShort(String name, OptionalShort x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindShortPrimitive(name, x.getAsShort()) : required;
    }

    default E bindOptionalSQLXML(String name, Optional<SQLXML> xmlObject) {
        final E required = requireDefaultedBindingForOptional(name);
        return xmlObject.isPresent() ? bindSQLXML(name, xmlObject.get()) : required;
    }

    default E bindOptionalString(String name, Optional<String> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindString(name, x.get()) : required;
    }

    default E bindOptionalTime(String name, Optional<Time> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindTime(name, x.get()) : required;
    }

    default E bindOptionalTime(String name, Optional<Time> x, Calendar cal) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindTime(name, x.get(), cal) : required;
    }

    default E bindOptionalTimestamp(String name, Optional<Timestamp> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindTimestamp(name, x.get()) : required;
    }

    default E bindOptionalTimestamp(String name, Optional<Timestamp> x, Calendar cal) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindTimestamp(name, x.get(), cal) : required;
    }

    default E bindOptionalURL(String name, Optional<URL> x) {
        final E required = requireDefaultedBindingForOptional(name);
        return x.isPresent() ? bindURL(name, x.get()) : required;
    }

}
