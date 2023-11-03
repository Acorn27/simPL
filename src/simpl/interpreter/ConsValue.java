package simpl.interpreter;

public class ConsValue extends Value {

    public final Value v1, v2;

    public ConsValue(Value v1, Value v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        return v1 + "::" + v2;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ConsValue)
                && (this.v1.equals(((ConsValue) other).v1) && (this.v2.equals(((ConsValue) other).v2)));
    }
}
