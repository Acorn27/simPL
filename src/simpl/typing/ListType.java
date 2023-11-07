package simpl.typing;

public final class ListType extends Type {

    public Type t;

    public ListType(Type t) {
        this.t = t;
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
        } else if (t instanceof ListType) {
            // unify list's element type
            return this.t.unify(((ListType) t).t);
        }
        throw new TypeMismatchError();
    }

    @Override
    public boolean contains(TypeVar tv) {
        return (this.t.contains(tv));
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        return new ListType(this.t.replace(a, t));
    }

    public String toString() {
        return t + " list";
    }
}
