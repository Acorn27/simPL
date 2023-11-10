package simpl.parser.ast;

import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Seq extends BinaryExpr {

    public Seq(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " ; " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var exp1Tr = l.typecheck(E);
        var subst = exp1Tr.s;

        try {
            subst = subst.compose(exp1Tr.t.unify(Type.UNIT));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), l.toString(), Type.UNIT.toString(), exp1Tr.t.toString());
            throw new TypeError(errorMessage);
        }

        var exp2Tr = r.typecheck(E);
        subst = subst.compose(exp2Tr.s);
        var exp2Ty = subst.apply(exp2Tr.t);
        return TypeResult.of(subst, exp2Ty);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        l.eval(s);
        return r.eval(s);
    }
}
