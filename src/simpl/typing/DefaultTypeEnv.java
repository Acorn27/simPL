package simpl.typing;

import simpl.parser.Symbol;

public class DefaultTypeEnv extends TypeEnv {

    private TypeEnv E;

    public DefaultTypeEnv() {
        // create a default type enviroment as the starting point
        E = new TypeEnv() {
            @Override
            public Type get(Symbol x) {
                return null;
            }
        };

    }

    @Override
    public Type get(Symbol x) {
        return E.get(x);
    }
}
