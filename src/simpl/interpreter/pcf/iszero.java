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

// inherit from funValue class
// it is a function value so that we can get it by calling eval
// then return the result of evaluate the body of function Val in
// Env: mapping x with argument val, same M, same p
public class iszero extends FunValue {

    // constructor
    public iszero() {

        // empty env: no mapping between variable and value yet
        // symbol x: x is a input parameter
        // expr: body is the function
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
                return new BoolValue(((IntValue) paramValue).n == 0);
            }

        });
    }
}
