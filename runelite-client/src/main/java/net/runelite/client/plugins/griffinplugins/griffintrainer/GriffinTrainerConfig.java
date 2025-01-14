package net.runelite.client.plugins.griffinplugins.griffintrainer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(GriffinTrainerPlugin.CONFIG_GROUP)
public interface GriffinTrainerConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0,
            closedByDefault = false
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "worldNumber",
            name = "World Number",
            description = "World Number",
            position = 0,
            section = generalSection
    )
    default int worldNumber() {
        return 316;
    }

    @ConfigItem(
            keyName = "minTotalTime",
            name = "Min Total Time (m)",
            description = "Min Total Time (m)",
            position = 1,
            section = generalSection
    )
    default int minTotalTime() {
        return 60;
    }

    @ConfigItem(
            keyName = "maxTotalTime",
            name = "Max Total Time (m)",
            description = "Max Total Time (m)",
            position = 2,
            section = generalSection
    )
    default int maxTotalTime() {
        return 90;
    }

    @ConfigItem(
            keyName = "minTaskTime",
            name = "Min Task Time (m)",
            description = "Min Task Time (m)",
            position = 3,
            section = generalSection
    )
    default int minTaskTime() {
        return 20;
    }

    @ConfigItem(
            keyName = "maxTaskTime",
            name = "Max Task Time (m)",
            description = "Max Task Time (m)",
            position = 4,
            section = generalSection
    )
    default int maxTaskTime() {
        return 30;
    }

    @ConfigSection(
            name = "Task Settings",
            description = "Task Settings",
            position = 1,
            closedByDefault = false
    )
    String tasksSection = "tasks";

    @ConfigItem(
            keyName = "trainCombat",
            name = "Train Combat",
            description = "Train Combat",
            position = 0,
            section = tasksSection
    )
    default boolean trainCombat() {
        return true;
    }

    @ConfigItem(
            keyName = "trainMining",
            name = "Train Mining",
            description = "Train Mining",
            position = 1,
            section = tasksSection
    )
    default boolean trainMining() {
        return true;
    }

    @ConfigSection(
            name = "Skill Levels",
            description = "Skill Levels",
            position = 2,
            closedByDefault = false
    )
    String skillsSection = "skills";

    @ConfigItem(
            keyName = "attackLevel",
            name = "Attack",
            description = " Attack Level",
            position = 0,
            section = skillsSection
    )
    default int attackLevel() {
        return 0;
    }

    @ConfigItem(
            keyName = "strengthLevel",
            name = "Strength",
            description = " Strength Level",
            position = 1,
            section = skillsSection
    )
    default int strengthLevel() {
        return 0;
    }

    @ConfigItem(
            keyName = "defenceLevel",
            name = "Defence",
            description = " Defence Level",
            position = 2,
            section = skillsSection
    )
    default int defenceLevel() {
        return 0;
    }

    @ConfigItem(
            keyName = "prayerLevel",
            name = "Prayer",
            description = " Prayer Level",
            position = 3,
            section = skillsSection
    )
    default int prayerLevel() {
        return 0;
    }

    @ConfigItem(
            keyName = "miningLevel",
            name = "Mining",
            description = " Mining Level",
            position = 4,
            section = skillsSection
    )
    default int miningLevel() {
        return 0;
    }

    @ConfigSection(
            name = "Mining Settings",
            description = "Mining Settings",
            position = 3,
            closedByDefault = false
    )
    String miningSettingsSection = "miningSettingsSection";


    @ConfigItem(
            keyName = "equipGear",
            name = "Equip Initial Gear",
            description = "Equip Initial Gear",
            position = 0,
            section = miningSettingsSection
    )
    default boolean equipGear() {
        return true;
    }


    @ConfigItem(
            keyName = "keepOre",
            name = "Keep Ore",
            description = "Keep Ore",
            position = 1,
            section = miningSettingsSection
    )
    default boolean keepOre() {
        return true;
    }

    @ConfigItem(
            keyName = "hopWorlds",
            name = "Hop Worlds",
            description = "Hop Worlds",
            position = 2,
            section = miningSettingsSection
    )
    default boolean hopWorlds() {
        return true;
    }

    @ConfigItem(
            keyName = "maxPlayers",
            name = "Max Players In Mining Area",
            description = "Max Players In Mining Area",
            position = 3,
            section = miningSettingsSection
    )
    default int maxPlayers() {
        return 2;
    }

    @ConfigItem(
            keyName = "maxWorldsToTry",
            name = "Max Worlds To Try",
            description = "Max Worlds To Try",
            position = 4,
            section = miningSettingsSection
    )
    default int maxWorldsToTry() {
        return 10;
    }

    @ConfigSection(
            name = "Combat Settings",
            description = "Combat Settings",
            position = 4,
            closedByDefault = false
    )
    String combatSettingsSection = "combatSettingsSection";

    @ConfigItem(
            keyName = "collectItems",
            name = "Collect Items",
            description = "Collect Items",
            position = 0,
            section = combatSettingsSection
    )
    default boolean collectItems() {
        return true;
    }


}
