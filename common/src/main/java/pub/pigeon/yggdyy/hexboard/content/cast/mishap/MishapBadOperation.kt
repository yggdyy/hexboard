package pub.pigeon.yggdyy.hexboard.content.cast.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation

class MishapBadOperation(val operation: Operation?): Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment {
        return ctx.pigment
    }
    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component? {
        return Component.translatable("mishap.hexboard.bad_operation", errorCtx.name, operation?.type?.ID ?: "?")
    }
    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {

    }
}