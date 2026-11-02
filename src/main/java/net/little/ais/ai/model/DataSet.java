package net.little.ais.ai.model;

import io.netty.buffer.ByteBuf;
import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.tensor.Tensor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;
import java.util.stream.Collectors;

public class DataSet implements Iterable<Map.Entry<DataSet.Input, Integer>> {

    private static StreamCodec<ByteBuf, List<Integer>> LIST_CODEC = ByteBufCodecs.VAR_INT.apply(
            ByteBufCodecs.list()
    );

    public static StreamCodec<ByteBuf, DataSet> CODEC = StreamCodec.of(
            (output, value) -> {
                List<Integer> list = value.TABLE.entrySet().stream().map(e -> List.of(e.getKey().a, e.getKey().b, e.getKey().c, e.getValue())).flatMap(List::stream).toList();
                LIST_CODEC.encode(output, list);
            },
            input -> {
                List<Integer> decode = LIST_CODEC.decode(input);
                int keys = decode.size()/4;
                List<int[]> groups = new ArrayList<>();
                for (int i = 0; i < keys; i++) {
                    int[] arr = new int[4];
                    int off = i*4;
                    arr[0] = decode.get(off);
                    arr[1] = decode.get(off+1);
                    arr[2] = decode.get(off+2);
                    arr[3] = decode.get(off+3);
                    groups.add(arr);
                }
                Map<Input, Integer> entries = groups.stream().collect(Collectors.toMap(
                        ints -> new Input(ints[0], ints[1], ints[2]),
                        ints -> ints[3]
                ));
                return new DataSet(entries);
            }
    );

    @Override
    public Iterator<Map.Entry<Input, Integer>> iterator() {
        return DataSet.this.TABLE.entrySet().iterator();
    }

    public Set<Map.Entry<Input, Integer>> entries() {
        return this.TABLE.entrySet();
    }

    public void clear() {
        this.TABLE.clear();
    }

    public static record Input(int a, int b, int c) {}

    HashMap<Input, Integer> TABLE;

    public DataSet() {
        this.TABLE = new HashMap<>();
    }

    private DataSet(Map<Input, Integer> entries) {
        this.TABLE = new HashMap<>(entries);
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

    @Override
    public String toString() {
        return this.TABLE.toString();
    }
}
