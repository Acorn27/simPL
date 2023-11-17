package simpl.interpreter;

import java.beans.Expression;

import simpl.parser.Symbol;
import simpl.parser.ast.Expr;

public class ThunkValue extends Value {

    public final State s;
    public final Expr e;
    private Value v;

    public ThunkValue(State s, Expr e) {
        this.s = s;
        this.e = e;
        this.v = null;
    }

    public String toString() {
        return "thunk";
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    public Value eval() throws RuntimeError {
        if (v == null) {
            v = e.eval(s);
        }
        return v;
    }
}
