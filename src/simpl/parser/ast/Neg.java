package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Neg extends UnaryExpr {

    public Neg(Expr e) {
        super(e);
    }

    public String toString() {
        return "~" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var expTr = e.typecheck(E);
        var subst = expTr.s;
        try {
            subst = subst.compose(expTr.t.unify(Type.INT));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), e.toString(), Type.INT.toString(), expTr.t.toString());
            throw new TypeError(errorMessage);
        }
        return TypeResult.of(subst, Type.INT);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var val = e.eval(s);
        if (!(val instanceof IntValue)) {
            String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an int.",
                    e.toString());
            throw new RuntimeError(errorMessage);
        }
        return new IntValue(-((IntValue) val).n);
    }
}
