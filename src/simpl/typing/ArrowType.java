package simpl.typing;

public final class ArrowType extends Type {

    public Type t1, t2;

    public ArrowType(Type t1, Type t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public boolean isEqualityType() {
        return false;
    }

    @Override
    public Substitution unify(Type t) throws TypeError {
        // don't unify constructor type with type var
        if (t instanceof TypeVar) {
            return t.unify(this);
        } else if (t instanceof ArrowType) {
            Substitution s1 = this.t1.unify(((ArrowType) t).t1);
            Substitution s2 = this.t2.unify(((ArrowType) t).t2);
            return s1.compose(s2);
        } else {
            throw new TypeMismatchError();
        }
    }

    @Override
    public boolean contains(TypeVar tv) {
        // either t1 or t2 contains?
        return (this.t1.contains(tv) || (this.t2.contains(tv)));
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        // replace in both
        return new ArrowType(this.t1.replace(a, t), this.t2.replace(a, t));
    }

    public String toString() {
        return "(" + t1 + " -> " + t2 + ")";
    }
}
