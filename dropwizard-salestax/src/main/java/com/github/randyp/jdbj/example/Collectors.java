package com.github.randyp.jdbj.example;

import com.google.common.collect.ImmutableList;

import java.util.stream.Collector;

public class Collectors {
    
    public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList(){
        return Collector.of(ImmutableList::builder, 
                ImmutableList.Builder::add, 
                (l, r)->l.addAll(r.build()), 
                ImmutableList.Builder::build);
    }

    private Collectors() {
    }
}
