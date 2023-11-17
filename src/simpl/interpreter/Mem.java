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

        // System.out.println(String.format("p is %s, mem limit is %s", s.p.get(),
        // Features.getMem()));

        // GC: coppy collection implementation
        if (Features.GC && s.p.get() >= Features.getMem()) {

            // System.out.println("call GC");

            // PHASE 1: copy && store in continuous spacce
            // create a second halves to be used an temporary storage
            var tempStorage = new ArrayList<Value>();
            var idx = 0;
            var env = s.E;
            // traversing the environment looking for active cell
            while (env != null) {
                var value = env.getVal();

                // if (value instanceof ThunkValue) {
                // value = ((ThunkValue) value).eval();
                // }

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

            // System.out.println("Temp storage size is: " + tempStorage.size());

            // if all the memory are in used => throw error
            if (tempStorage.size() >= Features.getMem()) {
                throw new RuntimeError("Runtime Eror: Running out of memory");
            }

            // PHASE 2: coppy from temporary storage back to heap
            for (int i = 0; i < tempStorage.size(); i++) {
                write(i, tempStorage.get(i));
            }

            // set next pointer to lastIdx + 1
            s.p.set(tempStorage.size());
            return s.p.get();
        } else {
            // if GC is dissable
            var ptr = s.p.get();
            s.p.set(ptr + 1);
            return ptr;
        }
    }
}
