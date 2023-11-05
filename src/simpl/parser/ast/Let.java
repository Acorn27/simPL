package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Let extends Expr {

    public Symbol x;
    public Expr e1, e2;

    public Let(Symbol x, Expr e1, Expr e2) {
        this.x = x;
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return "(let " + x + " = " + e1 + " in " + e2 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        // type check e1
        var e1Tr = e1.typecheck(E);

        // type check e2 under e1 of type e1Tr.t
        var funTr = e2.typecheck(TypeEnv.of(E, x, e1Tr.t));

        // combine constraint
        var subst = e1Tr.s.compose(funTr.s);
        var funTy = subst.apply(funTr.t);
        return TypeResult.of(subst, funTy);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        // evaluate e1
        var v1 = e1.eval(s);
        // evaluate e2
        return e2.eval(State.of(Env.of(s.E, x, v1), s.M, s.p));
    }
}
