package simpl.parser.ast;

import simpl.interpreter.ConsValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.ListType;
import simpl.typing.Substitution;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Cons extends BinaryExpr {

    public Cons(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " :: " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        var elemTr = l.typecheck(E);
        var listTr = r.typecheck(E);
        var subst = listTr.s.compose(elemTr.s);
        var listTy = subst.apply(listTr.t);
        var elemTy = subst.apply(elemTr.t);

        subst = subst.compose(listTy.unify(new ListType(elemTy)));
        listTy = subst.apply(listTy);
        return TypeResult.of(subst, listTy);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var elementVal = l.eval(s);
        var listVal = r.eval(s);
        return new ConsValue(elementVal, listVal);
    }
}
