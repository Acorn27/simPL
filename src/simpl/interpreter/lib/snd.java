package simpl.interpreter.lib;

import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.PairValue;
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

public class snd extends FunValue {

    public snd() {
        super(Env.empty, Symbol.symbol("x"), new Expr() {

            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError {

                return null;
            }

            public Value eval(State s) throws RuntimeError {

                var paramValue = s.E.get(Symbol.symbol("x"));

                // if this is thunk, evaluate it first
                if (paramValue instanceof ThunkValue) {
                    paramValue = ((ThunkValue) paramValue).eval();
                }
                // extra caution
                if (!(paramValue instanceof PairValue)) {
                    throw new RuntimeError(
                            "Runtime Error: Parameter passed to function \"snd\" can not be evaluated to a pair value");
                }
                return ((PairValue) paramValue).v2;
            }

        });
    }
}
