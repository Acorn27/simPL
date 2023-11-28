package simpl.interpreter;

public class Features {

    // Whether to use garbage collection
    public static final boolean GC = true;
    private static int memorySize = 5;

    // Whether to use lazy evaluation
    public static final boolean LAZY = false;

    // public function to set memory size
    public void setMem(int size) throws RuntimeError {
        if (GC) {
            memorySize = size;
        } else {
            throw new RuntimeError("Error: Cannot set memory size when garbage collection is disabled.");
        }
    }

    // public function to get memory size
    public static int getMem() throws RuntimeError {
        if (GC) {
            return memorySize;
        } else {
            throw new RuntimeError("Error: Cannot access memory size when garbage collection is disabled.");
        }
    }
}
