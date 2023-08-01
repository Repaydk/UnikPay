package dk.manaxi.unikpay.plugin.skript.expressions.subscription;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.manaxi.unikpay.api.classes.Subscription;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;

public class ExprnextPaymentOfSubscription extends SimpleExpression<Date> {
    private Expression<Subscription> subscription;
    static {
        Skript.registerExpression(ExprnextPaymentOfSubscription.class, Date.class, ExpressionType.SIMPLE, "[the] next payment of %subscription%");
    }

    @Override
    public @NotNull Class<? extends Date> getReturnType() {
        return Date.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        subscription = (Expression<Subscription>) exprs[0];
        return true;
    }

    @Override
    public @NotNull String toString(final @Nullable Event e, final boolean debug) {
        return "[the] player of %subscription%";
    }

    @Override
    protected Date @NotNull [] get(@NotNull Event e) {
        return new Date[]{Objects.requireNonNull(subscription.getSingle(e)).getNextPayment()};
    }
}
