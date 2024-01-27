package dk.manaxi.unikpay.plugin.API;

import com.google.gson.JsonObject;
import dk.manaxi.unikpay.api.classes.DurationType;
import dk.manaxi.unikpay.api.classes.Package;
import dk.manaxi.unikpay.api.classes.Subscription;
import dk.manaxi.unikpay.plugin.manager.RequestManager;
import dk.manaxi.unikpay.plugin.skript.classes.AcceptId;
import org.bukkit.entity.Player;

import java.util.List;

public class Internal {
    public static void sendPackageRequest(Player player, Package[] packages) {
        RequestManager.sendPackageRequest(player, packages);
    }

    public static void sendPayRequest(Player player, String name, Number amount) {
        RequestManager.sendPayRequest(player, name, amount);
    }

    public static JsonObject sendSubscriptionRequest(Player player, Package Package, Number duration, DurationType durationType) {
        return RequestManager.sendSubscriptionRequest(player, Package, duration, durationType);
    }

    public static List<Subscription> getSubscriptionsRequest() {
        return RequestManager.getSubscriptionsRequest();
    }

    public static JsonObject cancelSubscription(Subscription subscription) {
        return RequestManager.cancelSubscription(subscription);
    }

    public static void acceptPackageRequest(String id) {
        RequestManager.acceptPackageReqeust(id);
    }

    public static void acceptPackageRequest(AcceptId Id) {
        RequestManager.acceptPackageReqeust(Id.toString());
    }
}
