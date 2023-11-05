package simpl.parser.ast;

import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public abstract class RelExpr extends BinaryExpr {

    public RelExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        var lhsTr = l.typecheck(E);
        var rhsTr = r.typecheck(E);
        var subst1 = lhsTr.s.compose(lhsTr.t.unify(Type.INT));
        var subst2 = rhsTr.s.compose(rhsTr.t.unify(Type.INT));
        var subst = subst1.compose(subst2);

        return TypeResult.of(subst, Type.BOOL);
    }
}
