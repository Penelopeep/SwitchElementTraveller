package test.gc.switchele.commands;

import emu.grasscutter.GameConstants;
import test.gc.switchele.LanguageHelper;

public enum Element {
    elementless(1),
    pyro(2),
    hydro(3),
    anemo(4),
    cryo(5),
    geo(6),
    electro(7),
    dendro(8);
    private final int boyId;
    private final int girlId;
    private final int ElementId;
    public int plv=0;
    Element(int elementId) {
        this.ElementId=elementId;
        this.boyId = 500+ElementId;
        this.girlId = 700+ElementId;
    }
    public String getname(String Username){
        return  LanguageHelper.reader(name(), Username)+"lv"+String.valueOf(plv);
    }
    public int getTalentId(int index) {
        return (this.ElementId+3)*10+index+1;
    }
    public int getSkillRepoId(int avatarId) {
        return avatarId == GameConstants.MAIN_CHARACTER_MALE ? boyId : girlId;
    }
}
