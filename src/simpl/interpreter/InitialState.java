package simpl.interpreter;

import static simpl.parser.Symbol.symbol;
import simpl.interpreter.lib.hd;
import simpl.interpreter.lib.tl;
import simpl.interpreter.lib.fst;
import simpl.interpreter.lib.snd;
import simpl.interpreter.pcf.iszero;
import simpl.interpreter.pcf.pred;
import simpl.interpreter.pcf.succ;
import simpl.parser.Symbol;

public class InitialState extends State {

    public InitialState() {
        super(initialEnv(Env.empty), new Mem(), new Int(0));
    }

    private static Env initialEnv(Env E) {
        Env initEnv = new Env(E, Symbol.symbol("iszero"), new iszero());
        initEnv = new Env(initEnv, Symbol.symbol("pred"), new pred());
        initEnv = new Env(initEnv, Symbol.symbol("succ"), new succ());
        initEnv = new Env(initEnv, Symbol.symbol("fst"), new fst());
        initEnv = new Env(initEnv, Symbol.symbol("snd"), new snd());
        initEnv = new Env(initEnv, Symbol.symbol("hd"), new hd());
        initEnv = new Env(initEnv, Symbol.symbol("tl"), new tl());
        return initEnv;
    }
}
