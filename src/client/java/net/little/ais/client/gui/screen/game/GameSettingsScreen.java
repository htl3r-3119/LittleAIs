package net.little.ais.client.gui.screen.game;

import net.little.ais.client.gui.widget.ValidatedEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameSettingsScreen extends Screen {

    /*
    Page: INITIAL
    Number of Players
    Number of Rounds
    Page: LOBBY
    Player Names
    Send Invites Button
    */

    Component head = Component.literal("Game Settings").withStyle(
            style -> style.withUnderlined(true).withBold(true)
    );
    List<EditBox> editBoxes = new ArrayList<>();

    int xOff = 20;
    private int eH = 20;
    private int minYOff = 3;

    private PageType pageType;

    public int rounds;
    public int players;

    private static final int BACKGROUND_COLOR = new Color(0,0,0,0.6f).getRGB();

    public GameSettingsScreen() {
        this(Component.literal("game-settings"), PageType.INITIAL);
    }

    public GameSettingsScreen(int rounds, int players) {
        this(Component.literal("game-settings"), PageType.LOBBY);
        this.rounds = rounds;
        this.players = players;
    }

    private GameSettingsScreen(Component c, PageType p) {
        super(c);
        this.pageType = p;
    }

    private boolean checkedAllRequired() {
        return this.editBoxes.stream().mapToInt(value -> value.getValue().isEmpty() ? 0 : 1).min().orElse(0) == 1;
    }

    @Override
    protected void init() {
        super.init();
        this.editBoxes.clear();

        /*
        int eW = this.font.width(FormattedText.of("0123456789"))+8;
        int eH = 20;
        EditBox editBox = new EditBox(
                this.font,
                this.width/2-eW/2,this.height/2-eH/2,
                eW,eH,
                Component.literal("editbox")
        );
        editBox.setMaxLength(10);
        this.addRenderableWidget(editBox);
        */

        /*
        EditBox name0 = new EditBox(
                this.font,
                this.font.width("_".repeat(10)),
                this.eH,
                Component.literal("Name")
        );
        this.addEditBox(name0);
        */

        if (this.pageType == PageType.INITIAL) {
            this.addEditBox(1,"Players (1-6)", "[1-6]");
            this.addEditBox(1,"Rounds (1-6)", "[1-6]");
        }
        else {
            this.addEditBox(16,"Name1", "^[a-zA-Z0-9_]+$");
            assert Minecraft.getInstance().player != null;
            this.editBoxes.getLast().setValue(Minecraft.getInstance().player.getPlainTextName());
            this.editBoxes.getLast().setEditable(false);
            for (int i = 1; i < this.players; i++) {
                this.addEditBox(16,"Name" + (i + 1), "^[a-zA-Z0-9_]+$");
            }
        }

        int buttonW = 120;
        int buttonH = 20;

        Button button = Button.builder(
                Component.literal(this.pageType == PageType.INITIAL ? "Continue" : "Send Invites"),
                _ -> {
                    if (!checkedAllRequired()) {
                        this.minecraft.gui.toastManager().addToast(
                                new SystemToast(
                                        SystemToast.SystemToastId.NARRATOR_TOGGLE,
                                        Component.nullToEmpty("Can't continue yet"),
                                        Component.nullToEmpty("Fill in all fields")
                                )
                        );
                        return;
                    }
                    Screen screen;
                    if (this.pageType == PageType.INITIAL) {
                        if (this.getNumPlayers() == 1) {
                            screen = new GameScreen();
                        }
                        else screen = new GameSettingsScreen(this.getNumRounds(), this.getNumPlayers());
                    } else {
                        screen = new GameLobbyScreen(this.getNumRounds(), this.getPlayerNames());
                    }
                    Minecraft.getInstance().setScreenAndShow(screen);
                }
        ).bounds(
                this.width/2-buttonW/2,
                Math.max(
                        this.height/8+this.editBoxes.size()*(this.eH+this.minYOff)+this.eH,
                        this.height*7/8
                ) - buttonH - this.eH/2,
                buttonW,
                buttonH
        ).build();

        this.addRenderableWidget(button);

        this.editBoxes.forEach(this::addRenderableWidget);
    }

    private List<String> getPlayerNames() {
        return this.editBoxes.stream().map(EditBox::getValue).toList();
    }

    private int getNumPlayers() {
        if (this.pageType == PageType.LOBBY) return this.players;
        String value = this.editBoxes.getFirst().getValue();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private int getNumRounds() {
        if (this.pageType == PageType.LOBBY) return this.rounds;
        String value = this.editBoxes.get(1).getValue();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private void addEditBox(int maxLen, String label, String regex) {
        EditBox x = new ValidatedEditBox(
                this.font,
                this.width/4,
                this.eH,
                Component.literal(label),
                regex
        );
        x.setCentered(true);
        x.setMaxLength(maxLen);
        this.addEditBox(x);
    }

    private void addEditBox(EditBox ed) {
        this.editBoxes.add(ed);
        int minSettingsH = this.editBoxes.size()*(this.eH+this.minYOff);
        int max = this.editBoxes.stream().mapToInt(EditBox::getWidth).max().orElse(0);
        int baseY = this.height/8 + this.eH;
        int baseX = this.width/2 + this.xOff - max/4;
        double yOff = Math.max(minSettingsH,this.height/2.)/this.editBoxes.size();
        for (int i = 0; i < this.editBoxes.size(); i++) {
            EditBox edit = this.editBoxes.get(i);
            edit.setPosition(baseX, (int) (baseY+yOff*i)-this.eH/2);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(this.width/6,this.height/8, this.width*5/6,Math.max(this.height/8+this.editBoxes.size()*(this.eH+this.minYOff)+this.eH,this.height*7/8), BACKGROUND_COLOR);
        graphics.text(
                this.font,
                this.head,
                this.width/2-this.font.width(this.head)/2,
                this.height/8-this.font.lineHeight*3/2,
                Color.WHITE.getRGB()
        );
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.editBoxes.forEach(editBox -> {

            String string = editBox.getMessage().getString();

            graphics.text(
                    this.font,
                    string,
                    editBox.getX()-this.font.width(string)-this.xOff,
                    editBox.getY()+this.eH/2-this.font.lineHeight/2,
                    Color.WHITE.getRGB()
            );

        });
    }



}
