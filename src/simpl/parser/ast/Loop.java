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

        var e1Tr = e1.typecheck(E);
        var e2Tr = e2.typecheck(E);
        var subst = e1Tr.s.compose(e2Tr.s);

        var e1Ty = subst.apply(e1Tr.t);
        var e2Ty = subst.apply(e2Tr.t);

        try {
            subst = subst.compose(e1Ty.unify(Type.BOOL));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), e1.toString(), Type.BOOL, e1Ty);
            throw new TypeError(errorMessage);
        }

        try {
            subst = subst.compose(e2Ty.unify(Type.UNIT));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), e2.toString(), Type.UNIT, e2Ty);
            throw new TypeError(errorMessage);
        }

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
