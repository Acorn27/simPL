package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Loop extends Expr {

    public Expr e1, e2;

    public Loop(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return "(while " + e1 + " do " + e2 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        // assume that pred is bool
        var predTr = e1.typecheck(E);
        var subst = predTr.s.compose(predTr.t.unify(Type.BOOL));

        var bodyTr = e2.typecheck(E);
        subst = subst.compose(bodyTr.s);
        subst = subst.compose(bodyTr.t.unify(Type.UNIT));

        return TypeResult.of(subst, Type.UNIT);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var predVal = e1.eval(s);
        if (!(predVal instanceof BoolValue)) {
            throw new RuntimeError("Predicate can not evaluated to a bool value");
        }
        if (((BoolValue) predVal).b) {
            return new Seq(e2, this).eval(s);
        } else {
            return Value.UNIT;
        }
    }
}
