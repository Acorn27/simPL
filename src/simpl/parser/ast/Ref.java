package simpl.parser.ast;

import simpl.interpreter.RefValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.RefType;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Ref extends UnaryExpr {

    public Ref(Expr e) {
        super(e);
    }

    public String toString() {
        return "(ref " + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var expTr = e.typecheck(E);
        return TypeResult.of(expTr.s, new RefType(expTr.t));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        var ptr = s.M.alloc(s);

        var cellVal = e.eval(s);
        s.M.write(ptr, cellVal);
        return new RefValue(ptr);
    }
}
