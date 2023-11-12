package simpl.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Mem extends HashMap<Integer, Value> {

    public static int memorySize = 5;

    private static final long serialVersionUID = -1155291135560730618L;

    public void write(int ptr, Value val) {
        put(ptr, val);
    }

    public Value read(int ptr) {
        return get(ptr);
    }

    public int alloc(State s) throws RuntimeError {

        // begin garbage collection
        if (s.p.get() > memorySize) {

            // mark step
            var env = s.E;
            var tempStorage = new ArrayList<Value>();
            var idx = 0;
            while (env != null) {
                var value = env.getVal();
                if (value instanceof RefValue) {
                    tempStorage.add(idx, s.M.get(((RefValue) value).p));
                    ((RefValue) value).p = idx;
                    idx += 1;
                }
                env = env.E;
            }

            // sweep/rewrite step
            for (int i = 0; i < tempStorage.size(); i++) {
                write(i, tempStorage.get(i));
            }

            s.p.set(tempStorage.size());
            return s.p.get();
        } else {
            var ptr = s.p.get();
            s.p.set(ptr + 1);
            return ptr;
        }
    }
}
