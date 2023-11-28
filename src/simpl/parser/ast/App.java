package simpl.parser.ast;

import javax.xml.catalog.CatalogFeatures.Feature;

import simpl.interpreter.Env;
import simpl.interpreter.Features;
import simpl.interpreter.FunValue;
import simpl.interpreter.PairValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.ThunkValue;
import simpl.interpreter.Value;
import simpl.interpreter.pcf.iszero;
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

        var e1Tr = this.l.typecheck(E);
        var e2Tr = this.r.typecheck(E);

        var subst = e1Tr.s.compose(e2Tr.s);
        var e1Ty = subst.apply(e1Tr.t);
        var e2Ty = subst.apply(e2Tr.t);

        if (e1Ty instanceof ArrowType) {
            var paramTy = ((ArrowType) e1Ty).t1;
            try {
                subst = subst.compose(paramTy.unify(e2Ty));
            } catch (TypeError error) {
                String errorMessage = String.format(
                        "Type Error: Incompatible type in %s.%n"
                                + "Expected expression %s to have type '%s', but found '%s'.",
                        this.toString(), r.toString(), paramTy.toString(), e2Ty.toString());
                throw new TypeError(errorMessage);
            }
            var resTy = subst.apply(((ArrowType) e1Ty).t2);
            return TypeResult.of(subst, resTy);
        } else if (e1Ty instanceof TypeVar) {
            var resTv = new TypeVar(true);
            subst = subst.compose(e1Ty.unify(new ArrowType(e2Ty, resTv)));
            var resTy = subst.apply(resTv);
            return TypeResult.of(subst, resTy);
        }
        throw new TypeError("Type error: Lhs is not of function type");
    }

    @Override
    public Value eval(State s) throws RuntimeError {

        // evaluate left hand side and check if it is a function value
        var lhsVal = l.eval(s);
        if (!(lhsVal instanceof FunValue)) {
            throw new RuntimeError("Runtime error: " + l.toString() + "can not be evaluated to a function value");
        }
        // type cast to func value
        var fnVal = (FunValue) lhsVal;

        if (Features.LAZY) {
            var thunk = new ThunkValue(s, r);
            return fnVal.e.eval(State.of(Env.of(fnVal.E, fnVal.x, thunk), s.M, s.p));
        } else {
            var argVal = r.eval(s);
            return fnVal.e.eval(State.of(Env.of(fnVal.E, fnVal.x, argVal), s.M, s.p));
        }
    }
}
