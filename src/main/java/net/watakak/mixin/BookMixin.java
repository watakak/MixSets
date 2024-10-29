package net.watakak.mixin;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.screen.ScreenTexts;
import net.watakak.MixSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author mworzala (Implementation: KosmoMoustache)
 * @reason <a href="https://bugs.mojang.com/projects/MC/issues/MC-61489">Minecraft Bug Tracker</a>
 * @see <a href="https://gist.github.com/mworzala/9a8d86803784c9c81aac77d9a7f9fb2b">Gist</a>
 */
@Mixin(BookScreen.class)
public abstract class BookMixin extends Screen {

    protected BookMixin() {
        super(null);
    }

    @Shadow
    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);

    private int getY() {
        return (this.height - 192) / 3 + 196;
    }

    private int getY(int y) {
        return (this.height - 192) / 3 + y;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;getTextStyleAt(DD)Lnet/minecraft/text/Style;"),
            index = 1
    )
    public double fbg$getTextStyleAt(double y) {
        if (MixSets.getConfig().isCenteredBook()) {
            return y - (float) (this.height - 192) / 2;
        }
        return y;
    }

    @ModifyArg(method = "mouseClicked", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;getTextStyleAt(DD)Lnet/minecraft/text/Style;"),
            index = 1
    )
    public double fbg$mouseClicked(double y) {
        if (MixSets.getConfig().isCenteredBook()) {
            return y - (float) (this.height - 192) / 2;
        }
        return y;
    }

    @ModifyArg(method = "addCloseButton", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;")
    )
    public Element fbg$addCloseButton(Element par1) {
        if (MixSets.getConfig().isCenteredBook()) {
            return ButtonWidget.builder(ScreenTexts.DONE, button -> this.close())
                    .dimensions(this.width / 2 - 100, getY(), 200, 20).build();
        }
        return par1;
    }

    @Redirect(method = "addPageButtons", at = @At(value = "NEW",
            target = "(IIZLnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Z)Lnet/minecraft/client/gui/widget/PageTurnWidget;")
    )
    public PageTurnWidget fbg$addPageButtons(int x, int y, boolean isNextPageButton, ButtonWidget.PressAction action, boolean playPageTurnSound) {
        if (MixSets.getConfig().isCenteredBook()) {
            return new PageTurnWidget(x, getY(y), isNextPageButton, action, playPageTurnSound);
        }
        return new PageTurnWidget(x, y, isNextPageButton, action, playPageTurnSound);
    }

    // drawTexture
    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"),
            index = 2
    )
    public int fbg$render1(int y) {
        if (MixSets.getConfig().isCenteredBook()) {
            return getY(y);
        }
        return y;
    }

    // drawText (page index)
    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"),
            index = 3
    )
    public int fbg$render2(int y) {
        if (MixSets.getConfig().isCenteredBook()) {
            return getY(y);
        }
        return y;
    }

    // drawText (page content)
    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;IIIZ)I"),
            index = 3
    )
    public int fbg$render3(int y) {
        if (MixSets.getConfig().isCenteredBook()) {
            return getY(y);
        }
        return y;
    }
}