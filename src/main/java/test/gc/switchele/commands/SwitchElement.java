package test.gc.switchele.commands;

import emu.grasscutter.GameConstants;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.data.GameData;
import emu.grasscutter.data.excels.AvatarSkillDepotData;
import emu.grasscutter.game.avatar.Avatar;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.server.packet.send.PacketSceneEntityAppearNotify;
import emu.grasscutter.utils.Position;
import test.gc.switchele.LanguageHelper;
import test.gc.switchele.VersionSupportHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Command(label = "switchelement",usage="anemo|geo|electro|dendro",aliases = {"se"}, threading = true)
public class SwitchElement implements CommandHandler {

    @Nullable
    private static final Method getPositionMethod = VersionSupportHelper.getPositionMethod();

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
    public void execute(Player sender,Player targetPlayer, List<String> args) {
        if (args.size() != 1) {
            if (sender != null) {
                CommandHandler.sendMessage(targetPlayer, LanguageHelper.reader("usage", targetPlayer.getAccount().getUsername()));
            }
            else {
                Grasscutter.getLogger().info(LanguageHelper.reader("usage", targetPlayer.getAccount().getUsername()));
            }
            return;
        }
        Element element = getElementFromString(args.get(0));
        if (element == null) {
            if (sender != null) {
                CommandHandler.sendMessage(targetPlayer, LanguageHelper.reader("invalidElement", targetPlayer.getAccount().getUsername()));
            }
            else {
                Grasscutter.getLogger().info(LanguageHelper.reader("invalidElement", targetPlayer.getAccount().getUsername()));
            }
            return;
        }

        boolean maleSuccess = changeAvatarElement(targetPlayer, GameConstants.MAIN_CHARACTER_MALE, element);
        boolean femaleSuccess = changeAvatarElement(targetPlayer, GameConstants.MAIN_CHARACTER_FEMALE, element);
        if (maleSuccess || femaleSuccess) {
            if (getPositionMethod == null) {
                String message = String.format(LanguageHelper.reader("failedSuccess", targetPlayer.getAccount().getUsername()), element.name());
                if (sender != null) {
                    CommandHandler.sendMessage(targetPlayer, message);
                }
                else {
                    Grasscutter.getLogger().info(message);
                }
                return;
            }
            int scene = targetPlayer.getSceneId();
            String message;
            try {
                Position targetPlayerPos = (Position) getPositionMethod.invoke(targetPlayer);
                targetPlayer.getWorld().transferPlayerToScene(targetPlayer, 1, targetPlayerPos);
                targetPlayer.getWorld().transferPlayerToScene(targetPlayer, scene, targetPlayerPos);
                targetPlayer.getScene().broadcastPacket(new PacketSceneEntityAppearNotify(targetPlayer));
                message = String.format(LanguageHelper.reader("changeSuccess", targetPlayer.getAccount().getUsername()), element.name());
            } catch (IllegalAccessException | InvocationTargetException e) {
                message = String.format(LanguageHelper.reader("failedSuccess", targetPlayer.getAccount().getUsername()), element.name());
            }
            if (sender != null) {
                CommandHandler.sendMessage(targetPlayer, message);
            }
            else {
                Grasscutter.getLogger().info(message);
            }
        } else {
            if (sender != null) {
                CommandHandler.sendMessage(targetPlayer, LanguageHelper.reader("changeFailed", targetPlayer.getAccount().getUsername()));
            }
            else {
                Grasscutter.getLogger().info(LanguageHelper.reader("changeFailed", targetPlayer.getAccount().getUsername()));
            }
        }
    }
}