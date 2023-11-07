package simpl.typing;

final class IntType extends Type {

    protected IntType() {
    }

    @Override
    public boolean isEqualityType() {
        return true;
    }

    @Override
    public Substitution unify(Type t) throws TypeError {
        // don't unify constructor type with type var
        if (t instanceof TypeVar) {
            return t.unify(this);
        } else if (t instanceof IntType) {
            return Substitution.IDENTITY;
        }
        throw new TypeMismatchError();
    }

    @Override
    public boolean contains(TypeVar tv) {
        // alway false
        return false;
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        // no replacement on constructor type
        return Type.INT;
    }

    public String toString() {
        return "int";
    }
}
