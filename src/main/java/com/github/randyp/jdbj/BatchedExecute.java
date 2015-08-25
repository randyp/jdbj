package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Not intended for external use.
 * <p>
 * Abstract class for to encapsulate batching behaviour for {@link BatchedExecuteInsert}, {@link BatchedExecuteUpdate}.
 * <p>
 * Is mutable, but instances of subclass {@link BatchedExecute} are {@link Immutable}.
 * @param <P> chaining type, so that {@link com.github.randyp.jdbj.BatchedExecute.Batch} can return {@code this} as the subclass.
 */
@NotThreadSafe
abstract class BatchedExecute<P> {

    final NamedParameterStatement statement;
    final List<ValueBindings> batches = new ArrayList<>();

    BatchedExecute(NamedParameterStatement statement) {
        Objects.requireNonNull(statement, "statement must not be null");
        this.statement = statement;
    }

    public Batch startBatch(){
        return new Batch();
    }
    
    abstract P chainThis();

    @Immutable
    @NotThreadSafe
    public class Batch implements ValueBindingsBuilder<Batch> {

        private final ValueBindings batch;

        Batch(){
            this(new ValueBindings());
        }

        Batch(ValueBindings batch) {
            this.batch = batch;
        }

        public Batch bindValues(Supplier<ValueBindings>supplier) {
            return new Batch(batch.addAll(supplier.get()));
        }

        @Override
        public Batch bind(String name, Binding binding) {
            return new Batch(batch.bind(name, binding));
        }
        
        public P addBatch(){
            statement.checkAllBindingsPresent(batch);
            batches.add(batch);
            return chainThis();
        }
    }
}
