package simpl.typing;

import simpl.parser.Symbol;
import simpl.parser.Symbols;

public abstract class TypeEnvTest {

    public abstract String get(String x);

    public static TypeEnvTest of(final TypeEnvTest E, final String x, final String t) {
        return new TypeEnvTest() {
            public String get(String x1) {
                if (x == x1) return t;
                return E.get(x1);
            }

            public String toString() {
                return x + ":" + t + ";" + E;
            }
        }; 
    }

    public static final TypeEnvTest empty = new TypeEnvTest() {
        @Override
        public String get(String x) {
            return null;
        }
    };

    public static void main(String[] args) {
        TypeEnvTest myEnv = TypeEnvTest.empty;
        System.out.println(myEnv.get("z"));
        myEnv = myEnv.of(myEnv, "z", "int");
        System.out.println(myEnv.get("z"));
        myEnv = myEnv.of(myEnv, "a", "String");
        System.out.println(myEnv.get("a"));
    }
}
