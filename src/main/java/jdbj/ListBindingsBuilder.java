package jdbj;

import jdbj.lambda.Binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ListBindingsBuilder<E extends PositionalBindingsBuilder> extends ValueBindingsBuilder<E> {

    E bindList(String name, List<Binding> bindings);

    default E bindLongs(String name, long... xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLong(x));
        }

        return bindList(name, bindings);
    }

    default E bindStrings(String name, List<String> xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        final Function<String, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setString(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindList(name, bindings);
    }

    default E bindStrings(String name, String... xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        return bindStrings(name, Arrays.asList(xs));
    }
}
