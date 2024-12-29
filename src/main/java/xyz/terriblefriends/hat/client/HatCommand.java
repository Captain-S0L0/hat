package xyz.terriblefriends.hat.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class HatCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        try {
            Class.forName("net.fabricmc.fabric.impl.command.client.ClientCommandInternals");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Could not find ClientCommandInternals, /hat command not available");
            return;
        }

        fabricClientCommandSourceCommandDispatcher.register(literal("hat")
                        .then(literal("head").executes(ctx -> equipHandItem(ctx.getSource(),3)))
                        .then(literal("chest").executes(ctx -> equipHandItem(ctx.getSource(),2)))
                        .then(literal("legs").executes(ctx -> equipHandItem(ctx.getSource(),1)))
                        .then(literal("feet").executes(ctx -> equipHandItem(ctx.getSource(),0)))
                        .executes(ctx -> equipHandItem(ctx.getSource(),3))
        );
    }

    private static int equipHandItem(FabricClientCommandSource source, int armorSlot) throws CommandSyntaxException {
        ClientPlayerEntity player = source.getPlayer();
        if (!player.getMainHandStack().isEmpty()) {
            source.getClient().interactionManager.clickSlot(player.currentScreenHandler.syncId, 36+player.getInventory().selectedSlot, 36+armorSlot, SlotActionType.SWAP, player);

            return 1;
        }
        source.sendError(Text.literal("Error! No item in main hand!"));
        return 0;
    }
}
