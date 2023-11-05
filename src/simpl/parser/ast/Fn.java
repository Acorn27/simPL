package simpl.parser.ast;

import simpl.interpreter.FunValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.ArrowType;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Fn extends Expr {

    public Symbol x;
    public Expr e;

    public Fn(Symbol x, Expr e) {
        this.x = x;
        this.e = e;
    }

    public String toString() {
        return "(fn " + x + "." + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        // define new type variable for function parameter
        var paramTv = new TypeVar(true);

        // infer function's type in new enviroment
        var funTr = e.typecheck(TypeEnv.of(E, x, paramTv));

        var paramTy = funTr.s.apply(paramTv);

        return TypeResult.of(funTr.s, new ArrowType(paramTy, funTr.t));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        // closure
        return new FunValue(s.E, x, e);
    }
}
