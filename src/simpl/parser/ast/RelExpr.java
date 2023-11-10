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

        var subst = lhsTr.s.compose(rhsTr.s);
        var lhsTy = subst.apply(lhsTr.t);
        var rhsTy = subst.apply(rhsTr.t);

        try {
            subst = subst.compose(lhsTy.unify(Type.INT));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), l.toString(), Type.INT.toString(), lhsTy.toString());
            throw new TypeError(errorMessage);
        }

        try {
            subst = subst.compose(rhsTy.unify(Type.INT));
        } catch (TypeError error) {
            String errorMessage = String.format(
                    "Type Error: Incompatible type in %s.%n"
                            + "Expected expression %s to have type '%s', but found '%s'.",
                    this.toString(), r.toString(), Type.INT.toString(), rhsTy.toString());
            throw new TypeError(errorMessage);
        }

        return TypeResult.of(subst, Type.BOOL);
    }
}
