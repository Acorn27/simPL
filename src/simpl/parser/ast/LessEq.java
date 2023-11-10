package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class LessEq extends RelExpr {

    public LessEq(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " <= " + r + ")";
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var v1 = l.eval(s);
        if (!(v1 instanceof IntValue)) {
            String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int.",
                    l.toString());
            throw new RuntimeError(errorMessage);
        }
        var v2 = r.eval(s);
        if (!(v2 instanceof IntValue)) {
            String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int.",
                    r.toString());
            throw new RuntimeError(errorMessage);
        }
        return new BoolValue(((IntValue) v1).n <= ((IntValue) v2).n);
    }
}
