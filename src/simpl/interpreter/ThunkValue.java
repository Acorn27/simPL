package simpl.interpreter;

import java.beans.Expression;

import simpl.parser.Symbol;
import simpl.parser.ast.Expr;

public class ThunkValue extends Value {

    // keep track of final state where this thunk value should be evaluated
    public final State s;
    // raw expression
    public final Expr e;
    // sharing value to avoid re-evaluation
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
        // if this is the firs time, then evaluation it
        if (v == null) {
            v = e.eval(s);
        }
        // otherwise, return the pre-computed value
        return v;
    }
}
