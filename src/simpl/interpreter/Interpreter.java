package simpl.interpreter;

import java.io.FileInputStream;
import java.io.InputStream;

import simpl.parser.Parser;
import simpl.parser.SyntaxError;
import simpl.parser.ast.App;
import simpl.parser.ast.Expr;
import simpl.typing.DefaultTypeEnv;
import simpl.typing.TypeError;

public class Interpreter {

    public void run(String filename) {
        try (InputStream inp = new FileInputStream(filename)) {

            Parser parser = new Parser(inp);
            java_cup.runtime.Symbol parseTree = parser.parse();
            Expr program = (Expr) parseTree.value;

            // print out type of the evaluated expression
            System.out.println(program.typecheck(new DefaultTypeEnv()).t);

            // print out value of evaluated expression
            System.out.println(program.eval(new InitialState()));

        } catch (SyntaxError e) {
            System.out.println("syntax error");
        } catch (TypeError e) {
            System.out.println(e.getMessage());
        } catch (RuntimeError e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private static void interpret(String filename) {
        Interpreter i = new Interpreter();
        System.out.println(filename);
        i.run(filename);
    }

    public static void main(String[] args) {
        // interpret(args[1]);
        // interpret("doc/examples/mtRecur.spl");
        // interpret("doc/examples/GC.spl");
        // interpret("doc/examples/plus.spl");
        // interpret("doc/examples/factorial.spl");
        // interpret("doc/examples/gcd1.spl");
        // interpret("doc/examples/gcd2.spl");
        // interpret("doc/examples/max.spl");
        // interpret("doc/examples/sum.spl");
        // interpret("doc/examples/map.spl");
        // interpret("doc/examples/pcf.sum.spl");
        // interpret("doc/examples/pcf.even.spl");
        // interpret("doc/examples/pcf.minus.spl");
        // interpret("doc/examples/pcf.factorial.spl");

        // interpret("doc/examples/pcf.fibonacci.spl");

        // interpret("doc/examples/pcf.twice.spl");
        // interpret("doc/examples/pcf.lists.spl");
        // interpret("doc/examples/true.spl");

        interpret("doc2/factorial.spl");
        interpret("doc2/gcd1.spl");
        interpret("doc2/gcd2.spl");
        interpret("doc2/letpoly.spl");
        interpret("doc2/map.spl");
        interpret("doc2/max.spl");
        interpret("doc2/pcf.even.spl");
        interpret("doc2/pcf.factorial.spl");
        interpret("doc2/pcf.fibonacci.spl");
        interpret("doc2/pcf.minus.spl");
        interpret("doc2/pcf.sum.spl");
        interpret("doc2/plus.spl");
        interpret("doc2/sum.spl");

    }
}
