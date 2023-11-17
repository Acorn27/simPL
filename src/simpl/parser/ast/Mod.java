package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class Mod extends ArithExpr {

    public Mod(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " % " + r + ")";
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var lhsVal = l.eval(s);
        if (!(lhsVal instanceof IntValue)) {
            String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int value.",
                    l.toString());
            throw new RuntimeError(errorMessage);
        }
        var rhsVal = r.eval(s);
        if (!(rhsVal instanceof IntValue)) {
            String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int value.",
                    r.toString());
            throw new RuntimeError(errorMessage);
        }

        if (((IntValue) rhsVal).n == 0) {
            String errorMessage = String.format("Runtime Error: Division by zero in %s.", this.toString());
            throw new RuntimeError(errorMessage);
        }

        return new IntValue(((IntValue) lhsVal).n % ((IntValue) rhsVal).n);
    }
}
