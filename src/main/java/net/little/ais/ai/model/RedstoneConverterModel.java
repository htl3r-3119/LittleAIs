package net.little.ais.ai.model;

import net.little.ais.ai.layer.*;

import java.util.List;

public class RedstoneConverterModel {

    Layer model;

    public RedstoneConverterModel() {
        this.model = new Sequential(List.of(
                new Linear(3,8),
                new Relu(),
                new Linear(8,15),
                new Relu()
        ));
    }



}
