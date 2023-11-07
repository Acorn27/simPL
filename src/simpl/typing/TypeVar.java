package simpl.typing;

import simpl.parser.Symbol;

public class TypeVar extends Type {

    private static int tvcnt = 0;

    private boolean equalityType;
    private Symbol name;

    public TypeVar(boolean equalityType) {
        this.equalityType = equalityType;
        name = Symbol.symbol("tv" + ++tvcnt);
    }

    @Override
    public boolean isEqualityType() {
        return equalityType;
    }

    @Override
    public Substitution unify(Type t) throws TypeCircularityError {
        // return identity substitution if unify to the same type
        if ((t instanceof TypeVar) && (((TypeVar) t).name == name)) {
            return Substitution.IDENTITY;
            // type circularity error
        } else if ((t instanceof TypeVar) && (((TypeVar) t).contains(this))) {
            throw new TypeCircularityError();
        } else {
            return Substitution.of(this, t);
        }

    }

    public String toString() {
        return "" + name;
    }

    @Override
    public boolean contains(TypeVar tv) {
        return name.equals(tv.name);
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        // self explain
        if (this.contains(a)) {
            return t;
        } else {
            return this;
        }

    }

}
