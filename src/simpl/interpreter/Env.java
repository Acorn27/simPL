package simpl.interpreter;

import simpl.parser.Symbol;

public class Env {

    public final Env E;
    private final Symbol x;
    public final Value v;

    private Env() {
        E = null;
        x = null;
        v = null;
    }

    public static Env empty = new Env() {
        public Value get(Symbol y) {
            return null;
        }

        public Env clone() {
            return this;
        }

        public Value getVal() {
            return null;
        }
    };

    public Env(Env E, Symbol x, Value v) {
        this.E = E;
        this.x = x;
        this.v = v;
    }

    public Value get(Symbol y) {
        if (y == x) {
            return v;
        } else {
            return E.get(y);
        }
    }

    public Value getVal() {
        return v;
    }

    public Env clone() {
        return new Env(E, x, v);
    }

    public static Env of(Env E, Symbol x, Value v) {
        return new Env(E, x, v);
    }
}
