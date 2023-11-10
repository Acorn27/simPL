package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;

public class Mul extends ArithExpr {

    public Mul(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " * " + r + ")";
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var lhsVal = l.eval(s);
        if (!(lhsVal instanceof IntValue)) {
            if (!(lhsVal instanceof IntValue)) {
                String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int.",
                        l.toString());
                throw new RuntimeError(errorMessage);
            }
        }
        var rhsVal = r.eval(s);
        if (!(rhsVal instanceof IntValue)) {
            if (!(lhsVal instanceof IntValue)) {
                String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int.",
                        r.toString());
                throw new RuntimeError(errorMessage);
            }
        }
        return new IntValue(((IntValue) lhsVal).n * ((IntValue) rhsVal).n);
    }
}
