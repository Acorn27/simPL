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

public class Cond extends Expr {

    public Expr e1, e2, e3;

    public Cond(Expr e1, Expr e2, Expr e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public String toString() {
        return "(if " + e1 + " then " + e2 + " else " + e3 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        var e1Tr = e1.typecheck(E);
        var e2Tr = e2.typecheck(E);
        var e3Tr = e3.typecheck(E);
        var subst = e1Tr.s.compose(e2Tr.s.compose(e3Tr.s));

        var e1Ty = subst.apply(e1Tr.t);
        var e2Ty = subst.apply(e2Tr.t);
        var e3Ty = subst.apply(e3Tr.t);

        // type error catch for e1
        try {
            subst = subst.compose(e1Ty.unify(Type.BOOL));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type 'bool', but found '%s'.",
                    this.toString(), e1.toString(), e1Ty);
            throw new TypeError(errorMessage);
        }

        // type error catch for e2
        try {
            subst = subst.compose(e2Ty.unify(e3Ty));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible types in %s.%n"
                            + "Expected the same type for expressions %s and %s, but found '%s' and '%s' respectively.",
                    this.toString(), e2.toString(), e3.toString(), e2Ty, e3Ty);
            throw new TypeError(errorMessage);
        }

        e2Ty = subst.apply(e2Ty);
        return TypeResult.of(subst, e2Ty);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var predVal = e1.eval(s);
        if (!(predVal instanceof BoolValue)) {
            throw new RuntimeError("predicate is not a boolean");
        }
        return (((BoolValue) predVal).b) ? e2.eval(s) : e3.eval(s);
    }
}
