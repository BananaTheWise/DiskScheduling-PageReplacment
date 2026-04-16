package gui;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static Color selectedColor = Color.WHITE; // default

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showColorSelection();
        });
    }

    private static void showColorSelection() {
        JFrame frame = new JFrame("Color Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 3));

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.PINK, Color.BLACK};
        String[] colorNames = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Black"};

        for (int i = 0; i < colors.length; i++) {
            final int index = i;
            JButton button = new JButton(colorNames[i]);
            button.setBackground(colors[i]);
            button.setForeground(colors[i] == Color.BLACK ? Color.WHITE : Color.BLACK);
            button.addActionListener(e -> {
                selectedColor = colors[index];
                frame.dispose();
                showSystemSelection();
            });
            frame.add(button);
        }

        frame.setVisible(true);
    }

    private static void showSystemSelection() {
        JFrame frame = new JFrame("System Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setBackground(selectedColor);

        JButton osButton = new JButton("OS");
        JButton diskButton = new JButton("Disk");

        osButton.setBackground(selectedColor);
        diskButton.setBackground(selectedColor);

        osButton.addActionListener(e -> {
            frame.dispose();
            new OS().showGUI(selectedColor);
        });

        diskButton.addActionListener(e -> {
            frame.dispose();
            new Disk().showGUI(selectedColor);
        });

        frame.add(osButton);
        frame.add(diskButton);
        frame.setVisible(true);
    }
}
