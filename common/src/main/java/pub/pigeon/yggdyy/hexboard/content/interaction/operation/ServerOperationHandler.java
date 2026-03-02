package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;

public class ServerOperationHandler implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        ResourceLocation operationID = buf.readResourceLocation();
        if(Operations.REGISTRY.containsKey(operationID) && Operations.REGISTRY.get(operationID) != null) {
            BlockPos boardPos = buf.readBlockPos();
            Operation operation = buf.readWithCodec(NbtOps.INSTANCE, Operations.REGISTRY.get(operationID).CODEC);
            context.queue(() -> {
                Player player = context.getPlayer();
                Level level = player.level();
                BoardInstance instance = BoardInstance.create(level, boardPos);
                if(instance != null) {
                    instance.applyOperation(operation, player);
                }
            });
        }
    }
}
