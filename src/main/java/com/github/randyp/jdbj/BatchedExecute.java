package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;

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
            this(PositionalBindings.empty());
        }

        Batch(ValueBindings batch) {
            this.batch = batch;
        }

        @Override
        public Batch bind(String name, Binding binding) {
            return new Batch(batch.valueBinding(name, binding));
        }
        
        public P addBatch(){
            statement.checkAllBindingsPresent(batch);
            batches.add(batch);
            return chainThis();
        }
    }
}
