package dk.manaxi.unikpay.plugin.API;

import dk.manaxi.unikpay.plugin.manager.RequestManager;
import org.bukkit.entity.Player;

public class Internal {
    public static void sendPackageRequest(Player player, String pakke, int price, String id) {
        RequestManager.sendPackageRequest(player, pakke, price, id);
    };
    public static void acceptPackageRequest(String id) {
        RequestManager.acceptPackageReqeust(id);
    };
}