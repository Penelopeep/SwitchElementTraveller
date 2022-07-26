package test.gc.switchele.commands;

import emu.grasscutter.GameConstants;
import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.data.GameData;
import emu.grasscutter.data.excels.AvatarSkillDepotData;
import emu.grasscutter.game.avatar.Avatar;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.server.packet.send.PacketSceneEntityAppearNotify;
import emu.grasscutter.utils.Position;
import test.gc.switchele.Switchele;
import test.gc.switchele.VersionSupportHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Command(label = "switchelement", usage = "<White|Anemo|Geo|Electro|Dendro>", aliases = {"se"}, threading = true)
public class SwitchElement implements CommandHandler {

    @Nullable
    private static final Method getPositionMethod = VersionSupportHelper.getPositionMethod();
    private static final String failedSuccessfullyMessage = "Successfully changed traveller to %s, but failed to reload scene. Manually reload scene to see the changed";

    private Element getElementFromString(String elementString) {
        return switch (elementString.toLowerCase()) {
            case "white", "common" -> Element.elementless;
            case "fire", "pyro" -> Element.pyro;
            case "water", "hydro" -> Element.hydro;
            case "wind", "anemo", "air" -> Element.anemo;
            case "ice", "cryo" -> Element.cryo;
            case "rock", "geo" -> Element.geo;
            case "electro" -> Element.electro;
            case "grass", "dendro", "plant" -> Element.dendro;
            default -> null;
        };
    }

    private boolean changeAvatarElement(Player player, int avatarId, Element element) {
        Avatar avatar = player.getAvatars().getAvatarById(avatarId);
        AvatarSkillDepotData skillDepot = GameData.getAvatarSkillDepotDataMap().get(element.getSkillRepoId(avatarId));
        if (avatar == null || skillDepot == null) {
            return false;
        }
        avatar.setSkillDepotData(skillDepot);
        avatar.setCurrentEnergy(1000);
        avatar.save();
        return true;
    }

    @Override
    public void execute(Player sender, Player targetPlayer, List<String> args) {
        if (args.size() != 1) {
            sendUsageMessage(sender);
            return;
        }

        Element element = getElementFromString(args.get(0));
        if (element == null) {
            CommandHandler.sendMessage(sender, "Invalid element");
            return;
        }

        boolean maleSuccess = changeAvatarElement(targetPlayer, GameConstants.MAIN_CHARACTER_MALE, element);
        boolean femaleSuccess = changeAvatarElement(targetPlayer, GameConstants.MAIN_CHARACTER_FEMALE, element);
        if (maleSuccess || femaleSuccess) {
            if (getPositionMethod == null) {
                String message = String.format(failedSuccessfullyMessage, element.name());
                CommandHandler.sendMessage(sender, message);
                return;
            }
            int scene = targetPlayer.getSceneId();
            String message;
            try {
                Position senderPos = (Position) getPositionMethod.invoke(targetPlayer);
                targetPlayer.getWorld().transferPlayerToScene(targetPlayer, 1, senderPos);
                targetPlayer.getWorld().transferPlayerToScene(targetPlayer, scene, senderPos);
                targetPlayer.getScene().broadcastPacket(new PacketSceneEntityAppearNotify(targetPlayer));
                message = String.format("Successfully changed traveller to %s", element.name());
            } catch (IllegalAccessException | InvocationTargetException e) {
                message = String.format(failedSuccessfullyMessage, element.name());
            }
            CommandHandler.sendMessage(sender, message);
        } else {
            CommandHandler.sendMessage(sender, "Failed to change the Element.");
        }
    }
}