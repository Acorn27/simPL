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
import simpl.typing.ArrowType;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

import simpl.typing.*;

public class iszero extends FunValue {

    public iszero() {

        super(Env.empty, Symbol.symbol("x"), new Expr() {

            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError {
                return null;
            }

            public Value eval(State s) throws RuntimeError {

                var paramValue = s.E.get(Symbol.symbol("x"));

                // extra caution
                if (paramValue instanceof ThunkValue) {
                    paramValue = ((ThunkValue) paramValue).eval();
                }
                if (!(paramValue instanceof IntValue)) {
                    throw new RuntimeError("Parameter is not an integer value");
                }
                return new BoolValue(((IntValue) paramValue).n == 0);
            }

        });
    }
}
