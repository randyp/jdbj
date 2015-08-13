package oof.jdbc;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(Enclosed.class)
public class BindingsBuilderTest {


    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class BindingNotFound {
        @Test(expected = IllegalArgumentException.class)
        public void value() throws Exception {
            new BindingsBuilder(statement).bind(":not_the_mama", pc->pc.setInt(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void list() throws Exception {
            new BindingsBuilder(statement).bindList(":not_the_mama", new ArrayList<>());
        }
    }

    public static class CheckBinding {
        @Test
        public void value() throws Exception {
            new BindingsBuilder(statement).bindList(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test
        public void list() throws Exception {
            new BindingsBuilder(statement).bindList(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test(expected = IllegalStateException.class)
        public void missing() throws Exception {
            new BindingsBuilder(statement).checkAllBindingsPresent();
        }
    }
}