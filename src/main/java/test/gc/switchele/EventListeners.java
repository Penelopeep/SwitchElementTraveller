package test.gc.switchele;

import com.google.protobuf.InvalidProtocolBufferException;
import emu.grasscutter.game.inventory.GameItem;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.net.packet.PacketOpcodes;
import emu.grasscutter.net.proto.UseItemReqOuterClass.UseItemReq;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.game.ReceivePacketEvent;
import test.gc.switchele.commands.SwitchElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing all event handlers.
 * Syntax in event handler methods are similar to CraftBukkit.
 * To register an event handler, create a new instance of {@link EventHandler}.
 * Pass through the event class you want to handle. (ex. `new EventHandler<>(PlayerJoinEvent.class);`)
 * You can change the point at which the handler method is invoked with {@link EventHandler#priority(HandlerPriority)}.
 * You can set whether the handler method should be invoked when another plugin cancels the event with {@link EventHandler#ignore(boolean)}.
 */
public final class EventListeners {

    private static String getElementFromId(int itemID){
        return switch (itemID) {
            case 111013 -> "pyro";
            case 111014 -> "hydro";
            case 111016 -> "anemo";
            case 111015 -> "cryo";
            case 111018 -> "geo";
            case 111017 -> "electro";
            case 111019 -> "dendro";
            default -> null;
        };
    }
    public static void onPacket(ReceivePacketEvent event) {
        if (event.getPacketId() == PacketOpcodes.UseItemReq){
            try {
                UseItemReq req = UseItemReq.parseFrom(event.getPacketData());
                GameItem useItem = event.getGameSession().getServer().getInventorySystem().useItem(event.getGameSession().getPlayer(), req.getTargetGuid(), req.getGuid(), req.getCount(), req.getOptionIdx());
                SwitchElement se = new SwitchElement();
                Player player = event.getGameSession().getPlayer();
                ConstellationsHandler.change(player, 6);
                List<String> args = new ArrayList<>();
                args.add(getElementFromId(useItem.getItemId()));
                se.execute(null, player, args);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        }
    }
}