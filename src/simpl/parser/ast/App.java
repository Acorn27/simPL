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

        // type check e1 and e2
        var e1Tr = this.l.typecheck(E);
        var e2Tr = this.r.typecheck(E);
        // produce combined constraint to re-type check e1
        var subst = e1Tr.s.compose(e2Tr.s);
        // produce returned type yield using by apply combined substitution on e1
        var e1Ty = subst.apply(e1Tr.t);
        // It mayt not be neccesary, but keep it for now
        var e2Ty = subst.apply(e2Tr.t);

        // if e1 type already an arrow type
        if (e1Ty instanceof ArrowType) {
            // get parameter type of e1 and unified it with type of e2
            var paramTy = ((ArrowType) e1Ty).t1;
            // produce new substitution
            subst = subst.compose(paramTy.unify(e2Ty));
            // retrieve final type by apply new substution on body of the function
            var resTy = subst.apply(((ArrowType) e1Ty).t2);
            return TypeResult.of(subst, resTy);
            // if e1 type is an typeVar, shape it to an arrow type
        } else if (e1Ty instanceof TypeVar) {
            // create new return type
            var resTv = new TypeVar(true);
            // e1Ty = e2Ty -> ResTv
            subst = subst.compose(e1Ty.unify(new ArrowType(e2Ty, resTv)));
            // produce type yield of resTV
            var resTy = subst.apply(resTv);
            return TypeResult.of(subst, resTy);
        }
        throw new TypeError("Lhs is not a function");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        // evaluate left hand side and check if it is a function value
        var lhsVal = l.eval(s);
        if (!(lhsVal instanceof FunValue)) {
            throw new RuntimeError("Lhs can not be evaluated to a function value");
        }
        // type cast to func value
        var fnVal = (FunValue) lhsVal;
        // evaluate argument
        var argVal = r.eval(s);

        // evaluate function body in a new environmet
        return fnVal.e.eval(State.of(Env.of(fnVal.E, fnVal.x, argVal), s.M, s.p));
    }
}
