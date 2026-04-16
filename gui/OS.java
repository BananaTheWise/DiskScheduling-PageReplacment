package gui;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class OS {
    private Color bgColor;

    public void showGUI(Color color) {
        this.bgColor = color;
        JFrame frame = new JFrame("OS Page Replacement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().setBackground(bgColor);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.setBackground(bgColor);

        JLabel refLabel = new JLabel("Page Reference String (comma/space separated):");
        JTextField refField = new JTextField();
        JLabel frameLabel = new JLabel("Number of Frames:");
        JTextField frameField = new JTextField();
        JButton runButton = new JButton("Run");
        JTextArea outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.add(refLabel);
        panel.add(refField);
        panel.add(frameLabel);
        panel.add(frameField);
        panel.add(new JLabel()); // empty
        panel.add(runButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        runButton.addActionListener(e -> {
            String refStr = refField.getText();
            String frameStr = frameField.getText();
            try {
                String[] parts = refStr.split("[,\\s]+");
                int[] pages = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    pages[i] = Integer.parseInt(parts[i]);
                }
                int frameNo = Integer.parseInt(frameStr);

                // Capture output
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                System.out.println("FIFO page Replacement");
                invokeSimulate("os.project.FIFO", pages, frameNo);
                System.out.println("LRU page replacement");
                invokeSimulate("os.project.LRU", pages, frameNo);
                System.out.println("Optimal page replacement");
                invokeSimulate("os.project.Optimal", pages, frameNo);
                System.out.println("Second Chance page replacement ");
                invokeSimulate("os.project.secondChance", pages, frameNo);

                System.out.flush();
                System.setOut(old);

                outputArea.setText(baos.toString());
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }

    private void invokeSimulate(String className, int[] pages, int frameNo) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod("simulate", int[].class, int.class);
        method.invoke(null, pages, frameNo);
    }
}
