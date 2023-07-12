package io.github.pajama10000.crystaloptimizer.fabric;

import io.github.pajama10000.crystaloptimizer.CrystalOptimizer;
import net.fabricmc.api.ModInitializer;

public class CrystalOptimizerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CrystalOptimizer.init();
    }
}
