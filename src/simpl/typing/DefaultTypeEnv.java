package simpl.typing;

import simpl.parser.Symbol;

public class DefaultTypeEnv extends TypeEnv {

    private TypeEnv E;

    // constructor
    public DefaultTypeEnv() {
        // create an empt type enviroment as the starting point
        E = TypeEnv.empty;
    }

    @Override
    public Type get(Symbol x) {
        return E.get(x);
    }
}
