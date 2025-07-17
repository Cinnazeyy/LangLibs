package li.cinnazeyy.langlibsVelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

@Plugin(
        id = "langlibs-velocity",
        name = "langlibs-velocity",
        version = "1.0-SNAPSHOT",
        description = "A plot system migration plugin for upgrading plot versions",
        authors = {"Cinnazeyy"}
)
public class LanglibsVelocity {
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("langlibs:main");

    @Inject
    private Logger logger;
    @Inject
    private ProxyServer proxyServer;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getChannelRegistrar().register(IDENTIFIER);
    }

    @Subscribe
    public void onPluginMessageFromBackend(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(IDENTIFIER)) return;
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        // only attempt parsing the data if the source is a backend server
        if (!(event.getSource() instanceof ServerConnection backend)) return;
        String message = new String(event.getData(), StandardCharsets.UTF_8);
        String[] args = message.split(";");

        switch (args[0]) {
            case "getPlayerLang" -> logger.info("getPlayerLang");
            case "setPlayerLang" -> logger.info("setPlayerLang");
            default -> logger.error("invalid method!");
        }
    }
}
