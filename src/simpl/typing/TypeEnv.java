package simpl.typing;

import simpl.parser.Symbol;
import simpl.parser.Symbols;

public abstract class TypeEnv {

    // mapping between symbol and its type
    public abstract Type get(Symbol x);

    // enlarge current TypeEnv by adding a new mapping rule between Symbol X and
    // Type t.
    public static TypeEnv of(final TypeEnv E, final Symbol x, final Type t) {
        return new TypeEnv() {
            // recursivly searching for symbol x1 and return its type
            // if not found, go to previous typeEnv E
            // base case: return Null
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

    // base case
    public static final TypeEnv empty = new TypeEnv() {
        @Override
        public Type get(Symbol x) {
            return null;
        }
    };
}
