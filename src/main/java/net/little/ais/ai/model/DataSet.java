package net.little.ais.ai.model;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.tensor.Tensor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DataSet implements Iterable<Map.Entry<DataSet.Input, Integer>> {

    @Override
    public Iterator<Map.Entry<Input, Integer>> iterator() {
        return DataSet.this.TABLE.entrySet().iterator();
    }

    public Set<Map.Entry<Input, Integer>> entries() {
        return this.TABLE.entrySet();
    }

    public static record Input(int a, int b, int c) {}

    HashMap<Input, Integer> TABLE;

    public DataSet() {
        this.TABLE = new HashMap<>();
    }

    public void add(int a, int b, int c, int t) {
        TABLE.put(new Input(a,b,c), t);
    }

    public void remove(int a, int b, int c) {
        this.TABLE.remove(new Input(a,b,c));
    }

    public boolean isEmpty() {
        return this.TABLE.isEmpty();
    }

    public Tensor[] getTrainingData() {
        BatchMatrix inputs = new BatchMatrix(this.TABLE.size(), 1, 3);
        BatchMatrix targets = new BatchMatrix(this.TABLE.size(), 1, 16);
        int idx = 0;
        for (Map.Entry<Input, Integer> entry : this.TABLE.entrySet()) {
            Input key = entry.getKey();
            Integer val = entry.getValue();
            inputs.set(idx,0,0,key.a/15.); inputs.set(idx,0,1,key.b/15.); inputs.set(idx,0,2,key.c/15.);
            targets.set(idx++,0,val,1.);
        }
        return new Tensor[]{new Tensor(inputs), new Tensor(targets)};
    }

}
