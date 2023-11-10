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
        var rhsTr = r.typecheck(E);
        var subst = lhsTr.s.compose(rhsTr.s);
        var lhsTy = subst.apply(lhsTr.t);
        var rhsTy = subst.apply(rhsTr.t);

        try {
            subst = subst.compose(lhsTy.unify(rhsTy));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), r.toString(), lhsTy.toString(), rhsTy.toString());
            throw new TypeError(errorMessage);
        }

        lhsTy = subst.apply(lhsTy);
        rhsTy = subst.apply(rhsTy);

        if (!(lhsTy.isEqualityType())) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have an equality type, but found '%s'.",
                    this.toString(), l.toString(), lhsTy.toString());
            throw new TypeError(errorMessage);
        }
        if (!(rhsTy.isEqualityType())) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have an equality type, but found '%s'.",
                    this.toString(), r.toString(), rhsTy.toString());
            throw new TypeError(errorMessage);
        }

        return TypeResult.of(subst, Type.BOOL);
    }
}
