package pub.pigeon.yggdyy.hexboard.content.cast.operation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance
import pub.pigeon.yggdyy.hexboard.content.cast.mishap.MishapBadOperation
import pub.pigeon.yggdyy.hexboard.content.interaction.load.Loaders
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.AddOperation
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation

class OpLoadGambit: AbstractBoardOp() {
    override val argc: Int
        get() = 3
    override fun customExcute(
        args: List<Iota>,
        env: CastingEnvironment,
        board: BoardInstance,
        player: ServerPlayer
    ): List<Iota> {
        val start: Int = args.getIntBetween(1, 0, board.slots.size - 1, argc)
        val toLoadRaw: Iota = args.get(2)
        val toLoad: Iterable<Iota> = if(toLoadRaw.type.equals(HexIotaTypes.LIST)) {
            (toLoadRaw as? ListIota)?.list ?: throw Exception()
        } else {
            listOf(toLoadRaw)
        }
        val toAdd: MutableList<ItemStack> = mutableListOf()
        Loaders.load(toLoad, toAdd)
        val operation = AddOperation(toAdd, start)
        if(operation.operate(board, player, true) == Operation.OperateResult.SUCCESSFUL) {
            board.applyOperation(operation, player)
        } else {
            throw MishapBadOperation(operation)
        }
        return listOf()
    }
}