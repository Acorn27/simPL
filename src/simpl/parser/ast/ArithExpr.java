package simpl.parser.ast;

import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public abstract class ArithExpr extends BinaryExpr {

    public ArithExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {

        var lhsTr = l.typecheck(E);
        var rhsTr = r.typecheck(E);
        var subst = lhsTr.s.compose(rhsTr.s);
        var lhsTy = subst.apply(lhsTr.t);
        var rhsTy = subst.apply(rhsTr.t);

        subst = subst.compose(lhsTy.unify(Type.INT));
        subst = subst.compose(rhsTy.unify(Type.INT));
        return TypeResult.of(subst, Type.INT);
    }
}
