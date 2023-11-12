package simpl.parser.ast;

import simpl.interpreter.RefValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Deref extends UnaryExpr {

    public Deref(Expr e) {
        super(e);
    }

    public String toString() {
        return "!" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var refTr = e.typecheck(E);
        if (refTr.t instanceof RefType) {
            return TypeResult.of(refTr.s, ((RefType) refTr.t).t);
        } else if (refTr.t instanceof TypeVar) {
            var cellTv = new TypeVar(true);
            var subst = refTr.s;
            subst = subst.compose(refTr.t.unify(new RefType(cellTv)));
            subst.apply(cellTv);
            return TypeResult.of(subst, cellTv);
        }
        throw new TypeError("not a reference");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var refVal = e.eval(s);
        if (!(refVal instanceof RefValue)) {
            throw new RuntimeError("not a reference");
        }
        return s.M.read(((RefValue) refVal).p);
    }
}
