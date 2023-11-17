package simpl.interpreter.pcf;

import simpl.interpreter.BoolValue;
import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.ThunkValue;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class pred extends FunValue {

    public pred() {
        super(Env.empty, Symbol.symbol("x"), new Expr() {

            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError {
                return null;
            }

            public Value eval(State s) throws RuntimeError {

                var paramValue = s.E.get(Symbol.symbol("x"));
                // if this is a thunk, then evaluate it
                if (paramValue instanceof ThunkValue) {
                    paramValue = ((ThunkValue) paramValue).eval();
                }
                // extra caution
                if (!(paramValue instanceof IntValue)) {
                    throw new RuntimeError(
                            "Runtime Error: Parameter passed to function \"pred\" can not be evaluated to an integer value");
                }
                int res = ((IntValue) paramValue).n - 1;
                return new IntValue(res < 0 ? 0 : res);
            }
        });
    }
}
