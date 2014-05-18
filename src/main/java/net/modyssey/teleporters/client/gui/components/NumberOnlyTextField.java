package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;

public class NumberOnlyTextField extends GuiTextField {
    public NumberOnlyTextField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
        super(par1FontRenderer, par2, par3, par4, par5);
    }

    boolean isAllowedCharacter(int character) {
        return character >= '0' && character <= '9';
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    @Override
    public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_)
    {
        if (!isFocused())
        {
            return false;
        }
        else
        {
            switch (p_146201_1_)
            {
                case 1:
                    this.setCursorPositionEnd();
                    this.setSelectionPos(0);
                    return true;
                case 3:
                    GuiScreen.setClipboardString(this.getSelectedText());
                    return true;
                case 22:
                        this.writeText(GuiScreen.getClipboardString());

                    return true;
                case 24:
                    GuiScreen.setClipboardString(this.getSelectedText());

                        this.writeText("");

                    return true;
                default:
                    switch (p_146201_2_)
                    {
                        case 14:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                    this.deleteWords(-1);
                            }
                            else
                            {
                                this.deleteFromCursor(-1);
                            }

                            return true;
                        case 199:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(0);
                            }
                            else
                            {
                                this.setCursorPositionZero();
                            }

                            return true;
                        case 203:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                                }
                                else
                                {
                                    this.setSelectionPos(this.getSelectionEnd() - 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.setCursorPosition(this.getNthWordFromCursor(-1));
                            }
                            else
                            {
                                this.moveCursorBy(-1);
                            }

                            return true;
                        case 205:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                                }
                                else
                                {
                                    this.setSelectionPos(this.getSelectionEnd() + 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.setCursorPosition(this.getNthWordFromCursor(1));
                            }
                            else
                            {
                                this.moveCursorBy(1);
                            }

                            return true;
                        case 207:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(getText().length());
                            }
                            else
                            {
                                this.setCursorPositionEnd();
                            }

                            return true;
                        case 211:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                    this.deleteWords(1);
                            }
                            else
                            {
                                this.deleteFromCursor(1);
                            }

                            return true;
                        default:
                            if (isAllowedCharacter(p_146201_1_))
                            {
                                    this.writeText(Character.toString(p_146201_1_));

                                return true;
                            }
                            else
                            {
                                return false;
                            }
                    }
            }
        }
    }
}
