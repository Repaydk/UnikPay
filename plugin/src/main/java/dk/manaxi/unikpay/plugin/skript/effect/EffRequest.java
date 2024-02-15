package dk.manaxi.unikpay.plugin.skript.effect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dk.manaxi.unikpay.plugin.API.Internal;
import dk.manaxi.unikpay.plugin.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffRequest extends Effect {
    private Expression<Player> player;
    private Expression<Number> amount;
    private Expression<String> pakke;
    private Expression<String> id;


    static {
        Skript.registerEffect(EffRequest.class, "[unikpay] (request|anmod) %player% [om ]%number% em[(eralds|s|eralder|erald)] for %string%[ (med id|with id) %-string%]");
    }

    @Override
    protected void execute(@NotNull Event event) {
        final Player player = this.player.getSingle(event);
        final Number amount = this.amount.getSingle(event);
        final String pakke = this.pakke.getSingle(event);
        final String id = this.id != null ? this.id.getSingle(event) : pakke;

        if (Main.getAPIKEY() == null) {
            Skript.error("Du mangler at putte din apikey ind i config.yml");
            return;
        }

        if (player == null || pakke == null)
            return;

        Internal.sendPackageRequest(player, pakke, amount, id);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "[unikpay] (request|anmod) %player% [om ]%number% em(eralds|s|eralder) for %string%[ (med id|with id) %-string%]";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        this.player = (Expression<Player>) expressions[0];
        this.amount = (Expression<Number>) expressions[1];
        this.pakke = (Expression<String>) expressions[2];
        this.id = (Expression<String>) expressions[3];
        return true;
    }
}
