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
        var predTr = e1.typecheck(E);
        var subst = predTr.s;
        var predTy = predTr.t;
        subst = subst.compose(predTy.unify(Type.BOOL));

        // check condition
        var trueTr = e2.typecheck(E);
        var falseTr = e3.typecheck(E);
        subst = subst.compose(trueTr.s).compose(falseTr.s);
        var trueTy = subst.apply(trueTr.t);
        var falseTy = subst.apply(falseTr.t);
        subst = subst.compose(trueTy.unify(falseTy));
        trueTy = subst.apply(trueTy);

        return TypeResult.of(subst, trueTy);
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
