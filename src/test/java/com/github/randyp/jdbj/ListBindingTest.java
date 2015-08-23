package com.github.randyp.jdbj;

import com.github.randyp.jdbj.ListBinding;
import org.junit.Test;

import java.util.ArrayList;

public class ListBindingTest {

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableBindings() throws Exception {
        new ListBinding(new ArrayList<>()).getBindings().clear();
    }
}
