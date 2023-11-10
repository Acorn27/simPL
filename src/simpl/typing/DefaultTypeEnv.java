package simpl.typing;

import simpl.parser.Symbol;

public class DefaultTypeEnv extends TypeEnv {

    private TypeEnv E;

    // constructor
    public DefaultTypeEnv() {
        E = new TypeEnv() {
            @Override
            public Type get(Symbol x) {
                if (x.toString() == "iszero") {
                    return new ArrowType(Type.INT, Type.BOOL);
                } else if (x.toString() == "pred" || x.toString() == "succ") {
                    return new ArrowType(Type.INT, Type.INT);
                } else if (x.toString() == ("fst") || x.toString() == ("snd")) {
                    var e1Ty = new TypeVar(true);
                    var e2Ty = new TypeVar(true);
                    var pairTy = new PairType(e1Ty, e2Ty);
                    if (x.toString() == "fst") {
                        return new ArrowType(pairTy, e1Ty);
                    } else {
                        return new ArrowType(pairTy, e2Ty);
                    }
                } else if (x.toString() == "hd") {
                    var elemTy = new TypeVar(true);
                    var listTy = new ListType(elemTy);
                    return new ArrowType(listTy, elemTy);
                } else if (x.toString() == "tl") {
                    var elemTy = new TypeVar(true);
                    var listTy = new ListType(elemTy);
                    return new ArrowType(listTy, listTy);
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public Type get(Symbol x) {
        return E.get(x);
    }
}
