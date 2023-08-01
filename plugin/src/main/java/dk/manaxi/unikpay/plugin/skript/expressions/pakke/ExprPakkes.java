package dk.manaxi.unikpay.plugin.skript.expressions.pakke;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import dk.manaxi.unikpay.api.classes.Pakke;
import dk.manaxi.unikpay.plugin.event.OnBetaling;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprPakkes extends SimpleExpression<Pakke> {
    static {
        Skript.registerExpression(ExprPakkes.class, Pakke.class, ExpressionType.SIMPLE, "(pakke|pakker|packages|package)");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        ParserInstance parserInstance = new ParserInstance();
        if (!parserInstance.isCurrentEvent(OnBetaling.class)) {
            Skript.error("The expression 'pakkes' can only be used in unikpay betaling event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    @Override
    protected Pakke @NotNull [] get(@NotNull Event e) {
        if (!(e instanceof OnBetaling)) {
            return new Pakke[]{};
        }

        return ((OnBetaling) e).getPakker();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Pakke> getReturnType() {
        return Pakke.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the pakkes";
    }


}
