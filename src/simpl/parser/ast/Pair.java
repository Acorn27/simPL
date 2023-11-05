package simpl.parser.ast;

import simpl.interpreter.PairValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.PairType;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Pair extends BinaryExpr {

    public Pair(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(pair " + l + " " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var lhsTr = l.typecheck(E);
        var rhsTr = r.typecheck(E);
        var subst = lhsTr.s.compose(rhsTr.s);
        var lhsTy = subst.apply(lhsTr.t);
        var rhsTy = subst.apply(rhsTr.t);
        return TypeResult.of(subst, new PairType(lhsTy, rhsTy));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var lhsVal = l.eval(s);
        var rhsVal = r.eval(s);
        return new PairValue(lhsVal, rhsVal);
    }
}
