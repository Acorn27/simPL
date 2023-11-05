package simpl.interpreter.lib;

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

public class Fst extends FunValue {

    public Fst() {
        super(Env.empty, Symbol.symbol("x"), new Expr() {
            Symbol x = Symbol.symbol("x");

            @Override
            public TypeResult typeCheck(TypeEnv E) {
                // Type declaration is provided in `DefaultTypeEnv`. Type checking is done in
                // `App`.
                // Nothing to be done here.
                return null;
            }

            @Override
            public Value eval(State s) throws RuntimeError {
                var pairVal = s.E.get(x, s);
                if (!(pairVal instanceof PairValue)) {
                    throw new RuntimeError("not a pair");
                }
                return ((PairValue) pairVal).v1;
            }
        });
    }
}