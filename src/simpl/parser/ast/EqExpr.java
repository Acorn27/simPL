package simpl.parser.ast;

import simpl.typing.ListType;
import simpl.typing.PairType;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public abstract class EqExpr extends BinaryExpr {

    public EqExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        var lhsTr = l.typecheck(E);
        var rhsTr = l.typecheck(E);
        var subst = lhsTr.s.compose(rhsTr.s);
        var lhsTy = subst.apply(lhsTr.t);
        var rhsTy = subst.apply(rhsTr.t);

        subst = subst.compose(lhsTy.unify(rhsTy));
        lhsTy = subst.apply(lhsTy);
        rhsTy = subst.apply(rhsTy);
        if (!(lhsTy.isEqualityType())) {
            throw new TypeError("lhs is not equality type");
        }
        if (!(rhsTy.isEqualityType())) {
            throw new TypeError("rhs is not equality type");
        }
        return TypeResult.of(subst, Type.BOOL);
    }
}
