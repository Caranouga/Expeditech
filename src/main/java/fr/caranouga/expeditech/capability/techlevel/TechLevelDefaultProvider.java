package fr.caranouga.expeditech.capability.techlevel;

public class TechLevelDefaultProvider implements ITechLevel {
    private int techLevel;
    private int techXp;

    @Override
    public int getTechLevel() {
        return this.techLevel;
    }

    @Override
    public int getTechXp() {
        return this.techXp;
    }

    @Override
    public void setTechLevel(int techLevel) {
        this.techLevel = techLevel;
        this.techXp = getXpForLevel(techLevel);
    }

    @Override
    public void setTechXp(int techXp) {
        this.techXp = techXp;
        this.techLevel = getLevelForXp(techXp);
    }

    @Override
    public void addTechXp(int techXp) {
        setTechXp(this.techXp + techXp);
    }

    @Override
    public void addTechLevel(int techLevel) {
        setTechLevel(this.techLevel + techLevel);
    }

    @Override
    public int getTechXpToNextLevel() {
        return techXp - getXpForLevel(techLevel);
    }

    @Override
    public int getTotalXpToNextLevel() {
        return getXpForLevel(techLevel + 1) - getXpForLevel(techLevel);
    }

    /**
     * Returns the level for the given amount of xp
     * @param level the level to reach
     * @return the total amount of xp needed to reach the given level
     */
    private int getXpForLevel(int level){
        // Based on the Minecraft XP formula
        if(level >= 0 && level <= 16){
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if(level > 16 && level <= 31){
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else if(level > 31){
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        }
        return 0;
    }

    /**
     * Returns the level for the given amount of xp
     * @param xp the amount of xp
     * @return the level for the given amount of xp
     */
    private int getLevelForXp(int xp) {
        // Based on the Minecraft XP formula
        if(xp >= 0 && xp <= 352){
            return (int) (Math.sqrt(xp + 9) - 3);
        } else if(xp > 352 && xp <= 1507) {
            return (int) (80.0 / 10 * Math.sqrt(2.0 / 5 * (xp - 7839.0 / 40)));
        } else if(xp > 1507){
            return (int) (325.0 / 18 * Math.sqrt(2.0 / 9 * (xp - 54215.0 / 72)));
        } else {
            return 0;
        }
    }
}
