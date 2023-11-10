package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class Neq extends EqExpr {

    public Neq(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " <> " + r + ")";
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
        return new BoolValue(!v1.equals(v2));
    }
}
