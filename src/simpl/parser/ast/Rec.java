package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.RecValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Rec extends Expr {

    public Symbol x;
    public Expr e;

    public Rec(Symbol x, Expr e) {
        this.x = x;
        this.e = e;
    }

    public String toString() {
        return "(rec " + x + "." + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        // true or false???
        var recTv = new TypeVar(false);

        var eTr = e.typecheck(TypeEnv.of(E, x, recTv));

        var subst = eTr.s.compose(recTv.unify(eTr.t));

        var resTy = subst.apply(eTr.t);
        return TypeResult.of(subst, resTy);
        
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        return e.eval(State.of(Env.of(s.E, x, new RecValue(s.E, x, e)), s.M, s.p));
    }
}
