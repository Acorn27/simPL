package simpl.interpreter;

public class RefValue extends Value {

    public int p;

    public RefValue(int p) {
        this.p = p;
    }

    public String toString() {
        return "ref@" + p;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof RefValue) && (this.p == (((RefValue) other).p));
    }
}
