package simpl.interpreter;

import java.beans.Expression;

import simpl.parser.Symbol;
import simpl.parser.ast.Expr;

public class ThunkValue extends Value {

    public final State s;
    public final Expr e;

    public ThunkValue(State s, Expr e) {
        this.s = s;
        this.e = e;
    }

    public String toString() {
        return "thunk";
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    public Value eval() throws RuntimeError {
        // System.out.println(String.format("Call eval on thunk with expr = %s", e));
        return e.eval(s);
    }
}
