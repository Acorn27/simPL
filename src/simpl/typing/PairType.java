package simpl.typing;

public final class PairType extends Type {

    public Type t1, t2;

    public PairType(Type t1, Type t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public boolean isEqualityType() {
        return true;
    }

    @Override
    public Substitution unify(Type t) throws TypeError {
        // don't unify with a less specific type
        if (t instanceof TypeVar) {
            return t.unify(this);
        } else if (t instanceof PairType) {
            Substitution s1 = this.t1.unify(((PairType) t).t1);
            Substitution s2 = this.t2.unify(((PairType) t).t2);
            return s1.compose(s2);
        }
        throw new TypeMismatchError();
    }

    @Override
    public boolean contains(TypeVar tv) {
        // either contains?
        return (this.t1.contains(tv) || (this.t2.contains(tv)));
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        // replace both
        return new PairType(this.t1.replace(a, t), this.t2.replace(a, t));
    }

    public String toString() {
        return "(" + t1 + " * " + t2 + ")";
    }
}
