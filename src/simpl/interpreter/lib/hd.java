package simpl.interpreter.lib;

import simpl.interpreter.ConsValue;
import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.PairValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class hd extends FunValue {

    public hd() {
        super(Env.empty, Symbol.symbol("x"), new Expr() {

            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError {

                // can't type check alone since we need argument type
                return TypeResult.of(new TypeVar(true));
            }

            public Value eval(State s) throws RuntimeError {

                var paramValue = s.E.get(Symbol.symbol("x"));
                if (!(paramValue instanceof ConsValue)) {
                    throw new RuntimeError("Parameter not a list value");
                }
                return ((ConsValue) paramValue).v1;
            }
        });
    }
}
