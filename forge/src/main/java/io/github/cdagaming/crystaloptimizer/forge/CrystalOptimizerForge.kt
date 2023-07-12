package io.github.pajama10000.crystaloptimizer.forge

import io.github.pajama10000.crystaloptimizer.CrystalOptimizer
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(CrystalOptimizer.MOD_ID)
class CrystalOptimizerForge {
    init {
        CrystalOptimizer.init()
    }
}
