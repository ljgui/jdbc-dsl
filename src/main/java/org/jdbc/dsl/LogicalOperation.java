package org.jdbc.dsl;

import reactor.function.Consumer3;
import reactor.function.Function3;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

public interface LogicalOperation<T extends LogicalOperation<?>> extends TermTypeConditionalSupport {


    default <E> T each(String column, String termType, Collection<E> list, Function<T, Accepter<T, E>> accepterGetter) {
        if (null != list)
            list.forEach(o -> accepterGetter.apply(castSelf()).accept(column, termType, o));
        return castSelf();
    }

    default <E, V> T each(String column,
                          Collection<E> list,
                          Function<T, SimpleAccepter<T, V>> accepterGetter,
                          Function<E, V> valueMapper) {
        if (null != list)
            list.forEach(o -> accepterGetter.apply(castSelf()).accept(column, valueMapper.apply(o)));
        return castSelf();
    }

    default <E, V, B> T each(StaticMethodReferenceColumn<B> column,
                             Collection<E> list,
                             Consumer3<T, String, V> accepterGetter,
                             Function<E, V> valueMapper) {
        if (null != list)
            list.forEach(o -> accepterGetter.accept(castSelf(), column.getColumn(), valueMapper.apply(o)));
        return castSelf();
    }

    default <E, B> T each(StaticMethodReferenceColumn<B> column,
                          Collection<E> list,
                          Consumer3<T, String, E> accepterGetter) {
        return each(column, list, accepterGetter, Function.identity());
    }

    default <E, V> T each(Collection<E> list,
                          Function<E, V> valueMapper,
                          BiConsumer<T, V> consumer) {
        if (null != list)
            list.forEach(o -> consumer.accept(castSelf(), valueMapper.apply(o)));
        return castSelf();
    }


    default <E> T each(Collection<E> list, BiConsumer<T, E> consumer) {
        if (null != list)
            list.forEach(o -> consumer.accept(castSelf(), o));
        return castSelf();
    }



    default <K, V> T each(Map<K, V> mapParam, Function3<T, K, V, T> accepter) {
        T self = castSelf();
        if (null != mapParam) {
            for (Map.Entry<K, V> kvEntry : mapParam.entrySet()) {
                self = accepter.apply(self, kvEntry.getKey(), kvEntry.getValue());
            }
        }
        return self;
    }


    default T when(boolean condition, Consumer<T> consumer) {
        if (condition) {
            consumer.accept(castSelf());
        }
        return castSelf();
    }

    default T when(BooleanSupplier condition, Consumer<T> consumer) {
        return when(condition.getAsBoolean(), consumer);
    }


    default <V> T when(boolean condition, String column, Consumer3<T, String, V> accepter, V value) {
        if (condition) {
            accepter.accept(castSelf(), column, value);
        }
        return castSelf();
    }

    default <V, B> T when(boolean condition, StaticMethodReferenceColumn<B> column, Consumer3<T, String, V> accepter, V value) {
        return when(condition, column.getColumn(), accepter, value);
    }

    default <V> T when(boolean condition, Consumer3<T, String, V> accepter, MethodReferenceColumn<V> column) {

        return when(condition, column.getColumn(), accepter, column.get());
    }


    @SuppressWarnings("all")
    default <V> T when(Optional<V> value, BiConsumer<T, V> consumer) {
        value.ifPresent(v -> consumer.accept(castSelf(), v));
        return castSelf();
    }

    default <R> R as(Function<T, R> function) {
        return function.apply(castSelf());
    }

    default T accept(Consumer<T> consumer) {
        consumer.accept(castSelf());
        return castSelf();
    }

    default <V> T accept(V v, BiConsumer<T, V> consumer) {
        consumer.accept(castSelf(), v);
        return castSelf();
    }

    default <V> T accept(MethodReferenceColumn<V> column, BiConsumer<T, V> consumer) {
        V v = column.get();
        if (v != null) {
            consumer.accept(castSelf(), v);
        }
        return castSelf();
    }

    default T castSelf() {
        return (T) this;
    }
}
