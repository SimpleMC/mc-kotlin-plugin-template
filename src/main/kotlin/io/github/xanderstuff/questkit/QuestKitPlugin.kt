package io.github.xanderstuff.questkit

import org.bukkit.plugin.java.JavaPlugin

class QuestKitPlugin : JavaPlugin() {

    override fun onEnable() {
        // ensure config file exists
        saveDefaultConfig()

        logger.info("${description.name} version ${description.version} enabled!")
    }

    override fun onDisable() {
        logger.info("${description.name} disabled.")
    }
}
