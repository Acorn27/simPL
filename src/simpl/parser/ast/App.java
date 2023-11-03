package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.ArrowType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class App extends BinaryExpr {

    public App(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        // type check
        var fnTr = this.l.typecheck(E);
        var argTr = this.r.typecheck(E);
        var subst = fnTr.s.compose(argTr.s);
        var funTy = subst.apply(fnTr.t);
        var argTy = subst.apply(argTr.t);

        // infer type result
        if (funTy instanceof ArrowType) {
            var paramTy = ((ArrowType) funTy).t1;
            subst = subst.compose(paramTy.unify(argTy));
            var resTy = subst.apply(((ArrowType) funTy).t2);
            return TypeResult.of(subst, resTy);
        } else if (funTy instanceof TypeVar) {
            var resTv = new TypeVar(true);
            subst = subst.compose(funTy.unify(new ArrowType(argTy, resTv)));
            var resTy = subst.apply(resTv);
            return TypeResult.of(subst, resTy);
        }
        throw new TypeError("Lhs is not a function");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var lhsVal = l.eval(s);
        if (!(lhsVal instanceof FunValue)) {
            throw new RuntimeError("Lhs is not a function value");
        }
        var fnVal = (FunValue) lhsVal;
        var argVal = r.eval(s);
        return fnVal.e.eval(State.of(Env.of(fnVal.E, fnVal.x, argVal), s.M, s.p));
    }
}
