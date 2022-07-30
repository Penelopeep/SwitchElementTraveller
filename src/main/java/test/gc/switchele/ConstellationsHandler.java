package test.gc.switchele;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.game.avatar.Avatar;
import emu.grasscutter.game.entity.EntityAvatar;
import emu.grasscutter.game.player.Player;
import test.gc.switchele.commands.Element;

public class ConstellationsHandler {
    public static void change(Player targetPlayer, Element element, int constellation){
        Integer[] anemocons = {71,72,73,74,75,76};
        Integer[] geocons = {91,92,93,94,95,96};
        Integer[] electrocons = {101,102,103,104,105,106};
        Integer[] dendrocons = {111,112,113,114,115,116};
        Integer[] cons = {};

        switch (element){
            case anemo -> cons = anemocons;
            case geo -> cons = geocons;
            case electro -> cons = electrocons;
            case dendro -> cons = dendrocons;
        }

        try {
            EntityAvatar entity = targetPlayer.getTeamManager().getCurrentAvatarEntity();
            Avatar avatar = entity.getAvatar();
            avatar.getTalentIdList().clear();
            for (int i=0; i<constellation; i++){
                avatar.getTalentIdList().add(cons[i]);
            }
        } catch (Exception e){
            Grasscutter.getLogger().info("ConstellationHandler error");
        }
    }
}
