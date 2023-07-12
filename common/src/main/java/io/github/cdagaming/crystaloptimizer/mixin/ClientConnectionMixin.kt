package io.github.cdagaming.crystaloptimizer.mixin

import net.minecraft.client.Minecraft
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.boss.enderdragon.EndCrystal
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.TieredItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Connection::class)
class ClientConnectionMixin {
    @Inject(method = ["send(Lnet/minecraft/network/protocol/Packet;)V"], at = [At("HEAD")])
    private fun onPacketSend(packet: Packet<*>, ci: CallbackInfo) {
        val mc: Minecraft = Minecraft.getInstance()
        if (packet is ServerboundInteractPacket) {
            val interactPacket = packet as ServerboundInteractPacket
            interactPacket.dispatch(object : ServerboundInteractPacket.Handler {
                override fun onInteraction(hand: InteractionHand) {
                    // N/A
                }

                override fun onInteraction(hand: InteractionHand, pos: Vec3) {
                    // N/A
                }

                override fun onAttack() {
                    val entityHitResult: EntityHitResult
                    val entity: Entity
                    val hitResult: HitResult = mc.hitResult ?: return
                    if (hitResult.type === HitResult.Type.ENTITY && (entity = (entityHitResult = hitResult as EntityHitResult).entity) is EndCrystal) {
                        val weakness: MobEffectInstance? = mc.player.getEffect(MobEffects.WEAKNESS)
                        val strength: MobEffectInstance? = mc.player.getEffect(MobEffects.DAMAGE_BOOST)
                        if (weakness == null || strength != null && strength.amplifier > weakness.amplifier || isTool(mc.player.mainHandItem)) {
                            return
                        }
                        entity.kill()
                        entity.removed(Entity.RemovalReason.KILLED)
                        entity.onClientRemoval()
                    }
                }
            })
        }
    }

    private fun isTool(itemStack: ItemStack): Boolean {
        if (itemStack.item !is TieredItem || itemStack.item is HoeItem) {
            return false
        }
        val material: Tier = (itemStack.item as TieredItem).tier
        return material === Tiers.DIAMOND || material === Tiers.NETHERITE
    }
}
