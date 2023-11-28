package simpl.parser.ast;

import javax.xml.catalog.CatalogFeatures.Feature;

import simpl.interpreter.Env;
import simpl.interpreter.Features;
import simpl.interpreter.FunValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.ThunkValue;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.ArrowType;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class LetMR extends Expr {
    public Symbol x;
    public Symbol y;
    public Expr e1, e2, e3;

    public LetMR(Symbol x, Expr e1, Symbol y, Expr e2, Expr e3) {
        this.x = x;
        this.y = y;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public String toString() {
        return "(let " + x + " = " + e1 + "," + y + "=" + e2 + " in " + e3 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        // asign temporary type to y
        var yTv = new TypeVar(true);

        // compose new typpeEnv
        TypeEnv newE = TypeEnv.of(E, y, yTv);

        // type check e1 under new environment
        var e1Tr = e1.typecheck(newE);

        // update enviroment with type result of x
        newE = TypeEnv.of(newE, x, e1Tr.t);

        // type check e2 under new environmet
        var e2Tr = e2.typecheck(newE);

        // compose substitution
        var subst = e1Tr.s;
        subst = subst.compose(e2Tr.s);
        subst = subst.compose(yTv.unify(e2Tr.t));

        var e1Ty = subst.apply(e1Tr.t);
        var e2Ty = subst.apply(e2Tr.t);

        newE = TypeEnv.of(newE, x, e1Ty);
        newE = TypeEnv.of(newE, y, e2Ty);

        var finalTr = e2.typecheck(newE);
        var finalTy = subst.apply(finalTr.t);
        return TypeResult.of(subst, finalTy);
    }

    @Override
    public Value eval(State s) throws RuntimeError {

        var e1Val = e1.eval(s);
        if (!(e1Val instanceof FunValue))
            throw new RuntimeError("v1 is not a function");
        var e2Val = e2.eval(s);
        if (!(e2Val instanceof FunValue))
            throw new RuntimeError("v2 is not a function");

        // compose mutual enviroment
        var newEnv = Env.of(Env.of(s.E, x, e1Val), y, e2Val);

        // unify to the mutual environment
        ((FunValue) e1Val).E = newEnv;
        ((FunValue) e2Val).E = newEnv;

        // Evaluate the rest
        return e3.eval(State.of(newEnv, s.M, s.p));
    }
}
