package com.banking.ui;
import javax.swing.*;
import java.awt.*;

public class Theme {
    // Primary Colors
    public static final Color PRIMARY_COLOR = new Color(114, 76, 210); // Deep purple
    public static final Color BACKGROUND_COLOR = new Color(39, 36, 55); // Dark background
    public static final Color PANEL_COLOR = new Color(45, 39, 104, 210); // Panel background
    public static final Color CARD_COLOR = new Color(58, 54, 82); // Card background

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(255, 255, 255); // White
    public static final Color TEXT_SECONDARY = new Color(180, 180, 180); // Greyish text
    public static final Color HIGHLIGHT_TEXT = new Color(255, 199, 0); // Yellow highlight

    // Buttons
    public static final Color BUTTON_PRIMARY = new Color(114, 76, 210);
    public static final Color BUTTON_PRIMARY_HOVER = new Color(142, 94, 255);
    public static final Color BUTTON_TEXT = new Color(255, 255, 255);

    public static final Color BUTTON_SECONDARY = CARD_COLOR;
    public static final Color BUTTON_SECONDARY_HOVER = new Color(121, 121, 131);


    // Fonts
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font SUBHEADER_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BODY_FONT = new Font("Arial", Font.PLAIN, 12);

    // Borders and other UI
    public static final Color BORDER_COLOR = new Color(99, 92, 144);

    public static void addHoverEffect(JButton button, Color normal, Color hover) {
        button.setBackground(normal);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }

}
