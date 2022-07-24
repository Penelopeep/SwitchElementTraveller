package test.gc.switchele.commands;

import emu.grasscutter.GameConstants;

public enum Element {
    COMMON(501, 701),
    FIRE(502, 702),
    WATER(503, 703),
    WIND(504, 704),
    ICE(505, 705),
    ROCK(506, 706),
    ELECTRO(507, 707),
    GRASS(508, 708);
    private final int boyId;
    private final int girlId;

    Element(int boyId, int girlId) {
        this.boyId = boyId;
        this.girlId = girlId;
    }

    public int getSkillRepoId(boolean isBoy) {
        return isBoy ? boyId : girlId;
    }

    public int getSkillRepoId(int avatarId) {
        return avatarId == GameConstants.MAIN_CHARACTER_MALE ? boyId : girlId;
    }
}
