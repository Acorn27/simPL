package simpl.parser.ast;

import simpl.interpreter.RecValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Name extends Expr {

    public Symbol x;

    public Name(Symbol x) {
        this.x = x;
    }

    public String toString() {
        return "" + x;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var ty = E.get(x);
        if (ty == null) {
            throw new TypeError(String.format("Type of symbol %s not found", x.toString()));
        } else {
            return TypeResult.of(ty);
        }
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var val = s.E.get(x);
        if (val == null) {
            throw new RuntimeError(String.format("Value of symbol %s not found", x));
        } else if (val instanceof RecValue) {
            var rec = new Rec(x, ((RecValue) val).e);
            return rec.eval(State.of(((RecValue) val).E, s.M, s.p));
        }
        return val;
    }
}
