package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Not extends UnaryExpr {

    public Not(Expr e) {
        super(e);
    }

    public String toString() {
        return "(not " + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var expTr = e.typecheck(E);
        var subst = expTr.s.compose(expTr.t.unify(Type.BOOL));
        return TypeResult.of(subst, Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var val = e.eval(s);
        if (!(val instanceof BoolValue)) {
            throw new RuntimeError("not a boolean value");
        }
        return new BoolValue(!((BoolValue) val).b);
    }
}
