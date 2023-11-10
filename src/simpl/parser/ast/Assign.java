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

public class Assign extends BinaryExpr {

    public Assign(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return l + " := " + r;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        var lhsTr = l.typecheck(E);
        var subst = lhsTr.s;

        Type cellTy;

        if (lhsTr.t instanceof RefType) {
            cellTy = ((RefType) lhsTr.t).t;
        } else if (lhsTr.t instanceof TypeVar) {
            var cellTv = new TypeVar(true);
            subst = subst.compose(lhsTr.t.unify(new RefType(cellTv)));
            cellTy = subst.apply(cellTv);
        } else {
            throw new TypeError("not a reference type");
        }

        var rhsTr = r.typecheck(E);
        subst.compose(rhsTr.s);

        subst = subst.compose(rhsTr.t.unify(cellTy));

        return TypeResult.of(subst, Type.UNIT);

    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var refVal = l.eval(s);
        if (!(refVal instanceof RefValue)) {
            throw new RuntimeError("lhs not a reference");
        }
        var assignVal = r.eval(s);
        s.M.put(((RefValue) refVal).p, assignVal);
        return Value.UNIT;
    }
}
