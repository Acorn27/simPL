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
        var subst = expTr.s;

        try {
            subst = expTr.s.compose(expTr.t.unify(Type.BOOL));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), e.toString(), Type.BOOL.toString(), expTr.t.toString());
            throw new TypeError(errorMessage);
        }

        return TypeResult.of(subst, Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var val = e.eval(s);
        if (!(val instanceof BoolValue)) {
            String errorMessage = String.format("Runtime Error: Expression %s can not be evaluate to an bool value.",
                    e.toString());
            throw new RuntimeError(errorMessage);
        }
        return new BoolValue(!((BoolValue) val).b);
    }
}
