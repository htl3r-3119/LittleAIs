package net.little.ais.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class ValidatedEditBox extends EditBox {

    String pattern;

    public ValidatedEditBox(Font font, Component narration, String pattern) {
        super(font, narration);
        this.pattern = pattern;
    }

    public ValidatedEditBox(Font font, int width, int height, Component narration, String pattern) {
        super(font, width, height, narration);
        this.pattern = pattern;
    }

    public ValidatedEditBox(Font font, int x, int y, int width, int height, Component narration, String pattern) {
        super(font, x, y, width, height, narration);
        this.pattern = pattern;
    }

    public ValidatedEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox oldBox, Component narration, String pattern) {
        super(font, x, y, width, height, oldBox, narration);
        this.pattern = pattern;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        String prev = this.getValue();
        boolean res = super.keyPressed(event);
        if (!this.getValue().isEmpty() &&!this.getValue().matches(this.pattern)) {
            this.setValue(prev);
            return false;
        }
        return res;
    }

    @Override
    public void insertText(String input) {
        String prev = this.getValue();
        super.insertText(input);
        if (!this.getValue().matches(this.pattern)) {
            this.setValue(prev);
        }
    }
}
