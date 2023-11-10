package simpl.interpreter.lib;

import static simpl.parser.Symbol.symbol;

import simpl.interpreter.BoolValue;
import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.IntValue;
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

public class fst extends FunValue {

    public fst() {
        super(Env.empty, Symbol.symbol("x"), new Expr() {

            @Override
            public TypeResult typecheck(TypeEnv E) throws TypeError {

                return null;
            }

            public Value eval(State s) throws RuntimeError {

                var paramValue = s.E.get(Symbol.symbol("x"));
                // extra caution
                if (!(paramValue instanceof PairValue)) {
                    throw new RuntimeError("Parameter is not a pair value");
                }

                return ((PairValue) paramValue).v1;
            }

        });
    }
}
