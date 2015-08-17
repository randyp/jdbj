package com.github.randyp.jdbj.optional;

import com.github.randyp.jdbj.consumer.ByteConsumer;
import com.github.randyp.jdbj.supplier.ByteSupplier;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class OptionalByte {
    /**
     * Common instance for {@code empty()}.
     */
    private static final OptionalByte EMPTY = new OptionalByte();

    /**
     * If true then the value is present, otherwise indicates no value is present
     */
    private final boolean isPresent;
    private final byte value;

    /**
     * Construct an empty instance.
     *
     * @implNote generally only one empty instance, {@link OptionalByte#EMPTY},
     * should exist per VM.
     */
    private OptionalByte() {
        this.isPresent = false;
        this.value = 0;
    }

    /**
     * Returns an empty {@code Optionalbyte} instance.  No value is present for this
     * OptionalByte.
     *
     * @apiNote Though it may be tempting to do so, avoid testing if an object
     * is empty by comparing with {@code ==} against instances returned by
     * {@code Option.empty()}. There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     *  @return an empty {@code OptionalByte}.
     */
    public static OptionalByte empty() {
        return EMPTY;
    }

    /**
     * Construct an instance with the value present.
     *
     * @param value the boolean value to be present.
     */
    private OptionalByte(byte value) {
        this.isPresent = true;
        this.value = value;
    }

    /**
     * Return an {@code OptionalByte} with the specified value present.
     *
     * @param value the value to be present
     * @return an {@code OptionalByte} with the value present
     */
    public static OptionalByte of(byte value) {
        return new OptionalByte(value);
    }

    /**
     * If a value is present in this {@code OptionalByte}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the value held by this {@code OptionalByte}
     * @throws NoSuchElementException if there is no value present
     *
     * @see OptionalByte#isPresent()
     */
    public byte getAsByte() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * Have the specified consumer accept the value if a value is present,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is
     * null
     */
    public void ifPresent(ByteConsumer consumer) {
        if (isPresent)
            consumer.accept(value);
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present
     * @return the value, if present, otherwise {@code other}
     */
    public byte orElse(byte other) {
        return isPresent ? value : other;
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code ByteSupplier} whose result is returned if no value
     * is present
     * @return the value if present otherwise the result of {@code other.getAsByte()}
     * @throws NullPointerException if value is not present and {@code other} is
     * null
     */
    public byte orElseGet(ByteSupplier other) {
        return isPresent ? value : other.getAsByte();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * @apiNote A method reference to the exception constructor with an empty
     * argument list can be used as the supplier. For example,
     * {@code IllegalStateException::new}
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     * be thrown
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and
     * {@code exceptionSupplier} is null
     */
    public<X extends Throwable> byte orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this OptionalByte. The
     * other object is considered equal if:
     * <ul>
     * <li>it is also an {@code OptionalByte} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code Byte.compare() == 0}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof OptionalByte)) {
            return false;
        }

        OptionalByte other = (OptionalByte) obj;
        return (isPresent && other.isPresent)
                ? Byte.compare(value, other.value) == 0
                : isPresent == other.isPresent;
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return isPresent ? Byte.hashCode(value) : 0;
    }

    /**
     * {@inheritDoc}
     *
     * Returns a non-empty string representation of this object suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * @implSpec If a value is present the result must include its string
     * representation in the result. Empty and present instances must be
     * unambiguously differentiable.
     *
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return isPresent
                ? String.format("OptionalByte[%s]", value)
                : "OptionalByte.empty";
    }
}
