package net.watakak;

import net.minecraft.text.*;


public class ToolTipText implements CharacterVisitor {
    private final MutableText text = Text.empty();

    @Override
    public boolean accept(int index, Style style, int codePoint) {
        String car = new String(Character.toChars(codePoint));
        text.append(Text.literal(car).setStyle(style));
        return true;
    }

    public Text getText() {
        return text;
    }

    public static Text get(OrderedText text) {
        ToolTipText visitor = new ToolTipText();
        text.accept(visitor);
        return visitor.getText();
    }
}