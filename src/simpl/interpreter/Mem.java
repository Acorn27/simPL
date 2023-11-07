package simpl.interpreter;

import java.util.HashMap;

public class Mem extends HashMap<Integer, Value> {

    private static final long serialVersionUID = -1155291135560730618L;

    // create/update value of entry with key = ptr in hashmap
    public void write(int ptr, Value val) {
        put(ptr, val);
    }

    // reach value of entry with key = ptr from hashmap
    public Value read(int ptr) {
        return get(ptr);
    }

    // return next key
    public int alloc(State s) {
        var ptr = s.p.get();
        s.p.set(ptr + 1);
        return ptr;
    }
}
