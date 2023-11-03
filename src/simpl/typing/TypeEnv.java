package simpl.typing;

import simpl.parser.Symbol;
import simpl.parser.Symbols;

public abstract class TypeEnv {

    // mapping between symbol -> its type
    public abstract Type get(Symbol x);

    // create a new type enviroment bashed on x:t
    public static TypeEnv of(final TypeEnv E, final Symbol x, final Type t) {
        return new TypeEnv() {
            public Type get(Symbol x1) {
                if (x == x1)
                    return t;
                return E.get(x1);
            }

            public String toString() {
                return x + ":" + t + ";" + E;
            }
        };
    }

    public static final TypeEnv empty = new TypeEnv() {
        @Override
        public Type get(Symbol x) {
            return null;
        }
    };
}
