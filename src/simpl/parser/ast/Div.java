package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class Div extends ArithExpr {

    public Div(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " / " + r + ")";
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var v1 = l.eval(s);
        if (!(v1 instanceof IntValue)) {
            throw new RuntimeError(String.format("Runtime error: %s can not be evaluated to an integer", l.toString()));
        }
        var v2 = r.eval(s);
        if (!(v2 instanceof IntValue)) {
            throw new RuntimeError(String.format("Runtime error: %s can not be evaluated to an integer", r.toString()));
        }
        if (((IntValue) v2).n == 0) {
            throw new RuntimeError(String.format("Runtime error: Division by zero in %s", this.toString()));
        }
        return new IntValue(((IntValue) v1).n / ((IntValue) v2).n);
    }
}
