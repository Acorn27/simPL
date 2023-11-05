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
            throw new RuntimeError("lhs is not an integer");
        }
        var rhsVal = r.eval(s);
        if (!(rhsVal instanceof IntValue)) {
            throw new RuntimeError("rhs is not an integer");
        }

        if (((IntValue) rhsVal).n == 0) {
            throw new RuntimeError("division by 0");
        }

        return new IntValue(((IntValue) lhsVal).n % ((IntValue) rhsVal).n);
    }
}
