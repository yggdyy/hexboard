package pub.pigeon.yggdyy.hexboard.content.cast.operation

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance
import pub.pigeon.yggdyy.hexboard.content.cast.mishap.MishapBadOperation
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation

abstract class AbstractBoardOp: ConstMediaAction {
    // assume that the args are like (vec, ...) ->
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val player: ServerPlayer = env.castingEntity as? ServerPlayer ?: throw MishapBadCaster()
        val boardPos: BlockPos = args.getBlockPos(0, argc)
        val level: ServerLevel = env.world
        val board: BoardInstance = BoardInstance.create(level, boardPos) ?: throw MishapBadBlock(boardPos, Component.translatable("block.hexboard.board"))
        return customExcute(args, env, board, player)
    }
    open fun customExcute(args: List<Iota>, env: CastingEnvironment, board: BoardInstance, player: ServerPlayer): List<Iota> {
        val operation: Operation? = getOperation(args, env, board, player)
        if(operation != null && operation.operate(board, player, true) == Operation.OperateResult.SUCCESSFUL) {
            board.applyOperation(operation, player)
        } else {
            throw MishapBadOperation(operation)
        }
        return listOf()
    }
    open fun getOperation(args: List<Iota>, env: CastingEnvironment, board: BoardInstance, player: ServerPlayer): Operation? {
        return null
    }
}