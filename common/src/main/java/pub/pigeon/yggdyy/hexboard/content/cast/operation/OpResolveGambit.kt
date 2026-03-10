package pub.pigeon.yggdyy.hexboard.content.cast.operation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance
import pub.pigeon.yggdyy.hexboard.content.interaction.resolve.ResolveHandler

class OpResolveGambit(val literal: Boolean): AbstractBoardOp() {
    override val argc: Int
        get() = 3
    override fun customExcute(
        args: List<Iota>,
        env: CastingEnvironment,
        board: BoardInstance,
        player: ServerPlayer
    ): List<Iota> {
        var left: Int = args.getIntBetween(1, 0, board.slots.size - 1, argc)
        var right: Int = args.getIntBetween(2, 0, board.slots.size - 1, argc)
        if(left > right) {
            val tmp = right
            right = left
            left = tmp
        }
        val resolved: MutableList<Iota> = mutableListOf()
        ResolveHandler.resolve(board, board.mainEntity.level as ServerLevel, left, right, resolved, literal)
        return listOf(ListIota(resolved))
    }
}