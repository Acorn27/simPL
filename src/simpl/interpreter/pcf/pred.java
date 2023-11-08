package simpl.interpreter.pcf;

import simpl.interpreter.BoolValue;
import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
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
                // can't type check alone since we need argument type
                return TypeResult.of(new TypeVar(true));
            }

            public Value eval(State s) throws RuntimeError {

                var paramValue = s.E.get(Symbol.symbol("x"));
                if (!(paramValue instanceof IntValue)) {
                    throw new RuntimeError("Parameter is not an integer value");
                }
                // can it go negative? => no, as example show in class
                int res = ((IntValue) paramValue).n - 1;
                if (res < 0) {
                    res = 0;
                }
                return new IntValue(res);
            }
        });
    }
}
