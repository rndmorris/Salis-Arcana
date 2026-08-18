package dev.rndmorris.salisarcana.client.screens;

import static dev.rndmorris.salisarcana.config.SalisConfig.getGroupConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.Setting;
import dev.rndmorris.salisarcana.network.MessageLogin;

public class ScreenMismatchedConfig extends GuiScreen {

    private final GuiScreen parent;

    // Lines to display along with their indent level
    private final List<Tuple> lines = new ArrayList<>();

    // used to check for screen resize
    private int lastWidth = 0;
    private int lastHeight = 0;

    private boolean needShowDetailsButton = false;
    private boolean showDetailsPressed = false;

    private int buttonY = 0;

    private GuiButton yesButton = null;
    private GuiButton showDetailsButton = null;
    private GuiButton noButton = null;

    public ScreenMismatchedConfig(GuiScreen parentScreen) {
        this.parent = parentScreen;

        // arraylists stay in insertion order (unless set/sort called) so we can precompute linesToDisplay
        for (Map.Entry<ConfigGroup, HashMap<String, List<Setting>>> groupEntry : MessageLogin.settingGroups
            .entrySet()) {
            ConfigGroup group = groupEntry.getKey();
            HashMap<String, List<Setting>> categoryMap = groupEntry.getValue();

            // add config group name
            lines.add(new Tuple(group.getGroupName(), 0));
            for (Map.Entry<String, List<Setting>> categoryEntry : categoryMap.entrySet()) {
                String category = categoryEntry.getKey();
                List<Setting> settings = categoryEntry.getValue();
                // add category name
                lines.add(new Tuple("- " + category, 1));
                for (Setting setting : settings) {
                    // add setting name and expected vs actual value
                    lines.add(
                        new Tuple(
                            String.format(
                                "* %s: expected %s, got %s",
                                setting.getName(),
                                !setting.isEnabled(),
                                setting.isEnabled()),
                            2));
                }
            }
        }
        // We could write this during compute, but it is done here for clarity. Only has O(n) instead of O(a lot) like
        // above so who cares
        File file = new File("mismatched_configs.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Tuple line : lines) {
                String text = (String) line.getFirst();
                int indentLevel = (int) line.getSecond();
                for (int i = 0; i < indentLevel; i++) {
                    writer.write("\t");

                }
                writer.write(text);
                writer.newLine();
            }
        } catch (IOException ignored) {
            // We don't really care if this fails
        }
    }

    @Override
    public void initGui() {
        // Buttons: Yes | Show Details | No
        // Show Details only appears if needed, when text is too long or when toggled
        yesButton = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("gui.yes"));
        showDetailsButton = new GuiButton(
            2,
            this.width / 2 - 50,
            this.height - 52,
            100,
            20,
            I18n.format("salisarcana:showdetails"));
        noButton = new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("gui.no"));

        this.buttonList.add(yesButton);
        this.buttonList.add(showDetailsButton);
        this.buttonList.add(noButton);
        this.buttonY = this.height - 52;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        if (this.width != lastWidth || this.height != lastHeight) {
            lastWidth = this.width;
            lastHeight = this.height;
            // recompute on resize
            needShowDetailsButton = getTextY() + (lines.size() + 1) * (this.fontRendererObj.FONT_HEIGHT + 2)
                > this.buttonY;
            showDetailsPressed = showDetailsPressed && needShowDetailsButton;
        }

        if (!showDetailsPressed) {
            int k = this.height / 2 - 50;
            for (int i = 1; i <= 3; i++) {
                this.drawCenteredString(
                    this.fontRendererObj,
                    I18n.format("salisarcana:configmismatch." + i),
                    this.width / 2,
                    k,
                    16777215);
                k += this.fontRendererObj.FONT_HEIGHT;
            }
            // k at this point = getTextY()
            this.buttonY = Math.min(this.buttonY, (lines.size() + 1) * (this.fontRendererObj.FONT_HEIGHT + 2) + k + 20);
            needShowDetailsButton = k + (lines.size() + 1) * (this.fontRendererObj.FONT_HEIGHT + 2) > this.buttonY;
        }

        this.yesButton.yPosition = this.buttonY;
        this.showDetailsButton.yPosition = this.buttonY;
        this.noButton.yPosition = this.buttonY;

        // if button has been pressed, or button is needed, show/enable the button
        this.showDetailsButton.visible = this.showDetailsButton.enabled = needShowDetailsButton || showDetailsPressed;

        if (showDetailsPressed || !needShowDetailsButton) {

            // write config details if either toggled or always if they fit
            int y = needShowDetailsButton ? 20 : getTextY() + 20;
            for (Tuple line : lines) {
                String text = (String) line.getFirst();
                int indentLevel = (int) line.getSecond();
                int prevStringLength = this.fontRendererObj.getStringWidth(I18n.format("salisarcana:configmismatch.3"));
                int x = Math.max(this.width / 2 - prevStringLength / 2 - 40, 10);
                this.drawString(this.fontRendererObj, text, x + indentLevel * 10, y, 16777215);
                y += this.fontRendererObj.FONT_HEIGHT + 2;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 1:
                    this.yes();
                    break;
                case 2:
                    this.showDetails();
                    break;
                case 3:
                    this.no();
                    break;
                default:
                    break;
            }
        }
    }

    private void yes() {

        for (Map.Entry<ConfigGroup, HashMap<String, List<Setting>>> groupEntry : MessageLogin.settingGroups
            .entrySet()) {

            ConfigGroup group = groupEntry.getKey();
            HashMap<String, List<Setting>> categoryMap = groupEntry.getValue();
            final Configuration groupConfig = getGroupConfig(group);
            File file = groupConfig.getConfigFile();
            // backup existing config file
            File backupFile = new File(file.getAbsolutePath() + ".old");
            try {
                Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (Map.Entry<String, List<Setting>> categoryEntry : categoryMap.entrySet()) {
                String category = categoryEntry.getKey();
                List<Setting> settings = categoryEntry.getValue();
                for (Setting setting : settings) {
                    // not entirely sure what to do about default values here, so just use setting.isEnabled() for now
                    Property prop = groupConfig.get(category, setting.getName(), setting.isEnabled());
                    prop.set(!setting.isEnabled());
                }
            }

            if (groupConfig.hasChanged()) {
                groupConfig.save();
            }
        }
        Minecraft.getMinecraft()
            .displayGuiScreen(this.parent);
    }

    private void showDetails() {
        showDetailsPressed = !showDetailsPressed;
    }

    private void no() {
        Minecraft.getMinecraft()
            .displayGuiScreen(this.parent);
    }

    private int getTextY() {
        int k = this.height / 2 - 50;
        k += 3 * (this.fontRendererObj.FONT_HEIGHT);
        return k;
    }
}
