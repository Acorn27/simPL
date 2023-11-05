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

        // type check left hand side
        var lhsTr = l.typecheck(E);
        // type check right hand side under new environment
        var newE = lhsTr.s.compose(E);
        var rhsTr = r.typecheck(newE);
        // create composed substitution
        var subst = lhsTr.s.compose(rhsTr.s);
        // apply substitution on lhs, unify, and compose
        var lhsTy = subst.apply(lhsTr.t);
        subst = subst.compose(lhsTy.unify(Type.INT));
        // apply substitution on rhs, unify, and compose
        var rhsTy = subst.apply(rhsTr.t);
        subst = subst.compose(rhsTy.unify(Type.INT));
        // return final type
        return TypeResult.of(subst, Type.BOOL);
    }
}
