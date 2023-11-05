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

        // type check and unify e1 to BOOL
        var e1Tr = e1.typecheck(E);
        var subst = e1Tr.s.compose(e1Tr.t.unify(Type.BOOL));

        // create a new environment to type check e2 and e3
        var newEnv = subst.compose(E);

        // type check e2 and e3 under new environment
        var e2Tr = e2.typecheck(newEnv);
        var e3Tr = e3.typecheck(newEnv);

        // comnpose substitution and re-apply to e2 and e3
        subst = subst.compose(e2Tr.s).compose(e3Tr.s);
        var e2Ty = subst.apply(e2Tr.t);
        var e3Ty = subst.apply(e3Tr.t);

        // unify e2 and e3 to the same type
        subst = subst.compose(e2Ty.unify(e3Ty));
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
