package pub.pigeon.yggdyy.hexboard.content.cast.operation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.DeleteOperation
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation

class OpDeleteGambit: AbstractBoardOp() {
    override val argc: Int
        get() = 3
    override fun getOperation(
        args: List<Iota>,
        env: CastingEnvironment,
        board: BoardInstance,
        player: ServerPlayer
    ): Operation {
        var l: Int = args.getIntBetween(1, 0, board.slots.size - 1, argc)
        var r: Int = args.getIntBetween(2, 0, board.slots.size - 1, argc)
        if(l > r) {
            val tmp = l
            l = r
            r = tmp
        }
        val toDelete: MutableList<ItemStack> = mutableListOf()
        for (i in l..r) {
            toDelete.add(board.slots.get(i).stack.copy())
        }
        return DeleteOperation(toDelete, l)
    }
}