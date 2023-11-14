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

public class AndAlso extends BinaryExpr {

    public AndAlso(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " andalso " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var e1Tr = l.typecheck(E);
        var e2Tr = r.typecheck(E);
        var subst = e1Tr.s.compose(e2Tr.s);

        var e1Ty = subst.apply(e1Tr.t);
        var e2Ty = subst.apply(e2Tr.t);

        // unification
        try {
            subst = subst.compose(e1Ty.unify(Type.BOOL));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), l.toString(), Type.BOOL.toString(), e1Ty.toString());
            throw new TypeError(errorMessage);
        }
        try {
            subst = subst.compose(e2Ty.unify(Type.BOOL));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), r.toString(), Type.BOOL.toString(), e2Ty.toString());
            throw new TypeError(errorMessage);
        }
        
        return TypeResult.of(subst, Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var e1Val = l.eval(s);
        if (!(e1Val instanceof BoolValue)) {
            throw new RuntimeError(l.toString() + "can not be evaluated to a function value");
        }

        var e2Val = r.eval(s);
        if (!(e2Val instanceof BoolValue)) {
            throw new RuntimeError(l.toString() + "can not be evaluated to a function value");
        }

        boolean res =  ((BoolValue) e1Val).b && ((BoolValue) e2Val).b;
        return new BoolValue(res);
    }
}
