package pub.pigeon.yggdyy.hexboard.content.cast.operation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.server.level.ServerPlayer
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.MoveOperation
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation

class OpMoveGambit: AbstractBoardOp() {
    override val argc: Int
        get() = 4
    override fun getOperation(
        args: List<Iota>,
        env: CastingEnvironment,
        board: BoardInstance,
        player: ServerPlayer
    ): Operation {
        val left: Int = args.getIntBetween(1, 0, board.slots.size - 1, argc)
        val right: Int = args.getIntBetween(2, 0, board.slots.size - 1, argc)
        val target: Int = args.getIntBetween(3, 0, board.slots.size - 1, argc)
        return MoveOperation(left, right, target)
    }
}