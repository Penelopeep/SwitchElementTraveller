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

@Command(label = "switchelement", usage = "switchelement [White/Anemo/Geo/Electro/Dendro]", aliases = {"se"}, threading = true)
public class SwitchElement implements CommandHandler {

    @Nullable
    private static final Method getPositionMethod = VersionSupportHelper.getPositionMethod();
    private static final String failedSuccessfullyMessage = "Successfully changed element to %s, but failed to reload scene. Manually reload scene to see the changed";

    private Element getElementFromString(String elementString) {
        return switch (elementString.toLowerCase()) {
            case "white", "common" -> Element.COMMON;
            case "fire", "pyro" -> Element.FIRE;
            case "water", "hydro" -> Element.WATER;
            case "wind", "anemo", "air" -> Element.WIND;
            case "ice", "cryo" -> Element.ICE;
            case "rock", "geo" -> Element.ROCK;
            case "electro" -> Element.ELECTRO;
            case "grass", "dendro", "plant" -> Element.GRASS;
            default -> null;
        };
    }

    private boolean changeAvatarElement(Player sender, int avatarId, Element element) {
        Avatar avatar = sender.getAvatars().getAvatarById(avatarId);
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
            CommandHandler.sendMessage(sender, "Usage: /se OR /switchelement [White/Anemo/Geo/Electro/Dendro]");
            return;
        }
        if (sender == null) {
            Switchele.getInstance().getLogger().info("SwitchElement command couldn't be called by console.");
            return;
        }

        Element element = getElementFromString(args.get(0));
        if (element == null) {
            CommandHandler.sendMessage(sender, "Invalid element");
            return;
        }

        boolean maleSuccess = changeAvatarElement(sender, GameConstants.MAIN_CHARACTER_MALE, element);
        boolean femaleSuccess = changeAvatarElement(sender, GameConstants.MAIN_CHARACTER_FEMALE, element);
        if (maleSuccess || femaleSuccess) {
            if (getPositionMethod == null) {
                String message = String.format(failedSuccessfullyMessage, element.name());
                CommandHandler.sendMessage(sender, message);
                return;
            }
            int scene = sender.getSceneId();
            String message;
            try {
                Position senderPos = (Position) getPositionMethod.invoke(sender);
                sender.getWorld().transferPlayerToScene(sender, 1, senderPos);
                sender.getWorld().transferPlayerToScene(sender, scene, senderPos);
                sender.getScene().broadcastPacket(new PacketSceneEntityAppearNotify(sender));
                message = String.format("Successfully changed element to %s", element.name());
            } catch (IllegalAccessException | InvocationTargetException e) {
                message = String.format(failedSuccessfullyMessage, element.name());
            }
            CommandHandler.sendMessage(sender, message);
        } else {
            CommandHandler.sendMessage(sender, "Failed to change the Element.");
        }
    }
}