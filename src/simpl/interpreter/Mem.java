package simpl.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Mem extends HashMap<Integer, Value> {

    public static int memorySize = 3;
    private boolean GC = false;

    private static final long serialVersionUID = -1155291135560730618L;

    public void write(int ptr, Value val) {
        put(ptr, val);
    }

    public Value read(int ptr) {
        return get(ptr);
    }

    public int alloc(State s) throws RuntimeError {

        // GC: coppy collection implementation
        if (GC && s.p.get() >= memorySize) {

            // System.out.println(String.format("Run out of mem at p = %s", s.p.get()));

            // PHASE 1: copy && store in continuous spacce
            // create a second halves to be used an temporary storage
            var tempStorage = new ArrayList<Value>();
            var idx = 0;
            var env = s.E;
            // traversing the environment looking for active cell
            while (env != null) {
                var value = env.getVal();
                if (value instanceof RefValue) {
                    // write value to temporary storage
                    tempStorage.add(idx, s.M.get(((RefValue) value).p));
                    // update current pointer to the new address
                    ((RefValue) value).p = idx;
                    // update the next avaible address
                    idx += 1;
                }
                // go to the next Env
                env = env.E;
            }

            // if all the memory are in used => throw error
            if (tempStorage.size() >= memorySize) {
                throw new RuntimeError("Running out of memory");
            }

            // PHASE 2: coppy from temporary storage back to heap
            for (int i = 0; i < tempStorage.size(); i++) {
                // System.out.println(String.format("After gc: %s:%s", i, tempStorage.get(i)));
                // write approrirate value to each cell
                write(i, tempStorage.get(i));
            }

            // set next pointer to lastIdx + 1
            s.p.set(tempStorage.size());
            return s.p.get();
        } else {
            // not GC
            var ptr = s.p.get();
            s.p.set(ptr + 1);
            return ptr;
        }
    }
}
