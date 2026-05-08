package gui;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class Disk {
    private Color bgColor;

    public void showGUI(Color color) {
        this.bgColor = color;
        JFrame frame = new JFrame("Disk Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().setBackground(bgColor);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.setBackground(bgColor);

        JLabel cylLabel = new JLabel("Cylinders visited (comma/space separated):");
        JTextField cylField = new JTextField("98, 183, 37, 122, 14, 124, 65, 67");
        JLabel dirLabel = new JLabel("Direction:");
        JComboBox<String> dirBox = new JComboBox<>(new String[]{"large", "small"});
        JLabel startLabel = new JLabel("Starting head:");
        JTextField startField = new JTextField("53");
        JLabel sizeLabel = new JLabel("Number of cylinders:");
        JTextField sizeField = new JTextField("200");
        JButton runButton = new JButton("Run");

        panel.add(cylLabel);
        panel.add(cylField);
        panel.add(dirLabel);
        panel.add(dirBox);
        panel.add(startLabel);
        panel.add(startField);
        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(new JLabel()); // empty
        panel.add(runButton);

        frame.add(panel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane, BorderLayout.CENTER);

        runButton.addActionListener(e -> {
            String cylStr = cylField.getText();
            String dir = (String) dirBox.getSelectedItem();
            String startStr = startField.getText();
            String sizeStr = sizeField.getText();
            try {
                String[] parts = cylStr.split("[,\\s]+");
                int[] cylinders = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    cylinders[i] = Integer.parseInt(parts[i]);
                }
                int start = Integer.parseInt(startStr);
                int size = Integer.parseInt(sizeStr);

                tabbedPane.removeAll();

                String[] algorithms = {"FCFS", "SSTF", "SCAN", "CSCAN", "LOOK", "CLOOK"};
                java.util.List<String> algoNames = new java.util.ArrayList<>();
                java.util.List<Integer> totalSeeks = new java.util.ArrayList<>();

                for (String selectedAlgo : algorithms) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    PrintStream old = System.out;
                    System.setOut(ps);

                    // Clone array to prevent sorting in one algorithm from affecting the next
                    int[] currentCylinders = cylinders.clone();

                    try {
                        if ("FCFS".equals(selectedAlgo)) {
                            invokeCalculateSeek("algorithms.DiskScheduling.FCFS", currentCylinders, start);
                        } else if ("SSTF".equals(selectedAlgo)) {
                            invokeCalculateSeekWithDir("algorithms.DiskScheduling.SSTF", currentCylinders, start, dir);
                        } else if ("SCAN".equals(selectedAlgo)) {
                            invokeCalculateSeekWithSize("algorithms.DiskScheduling.SCAN", currentCylinders, start, dir, size);
                        } else if ("LOOK".equals(selectedAlgo)) {
                            invokeCalculateSeekWithDir("algorithms.DiskScheduling.Look", currentCylinders, start, dir);
                        } else if ("CSCAN".equals(selectedAlgo)) {
                            invokeCalculateSeekWithSize("algorithms.DiskScheduling.CSCAN", currentCylinders, start, dir, size);
                        } else if ("CLOOK".equals(selectedAlgo)) {
                            invokeCalculateSeekWithDir("algorithms.DiskScheduling.CLOOK", currentCylinders, start, dir);
                        }
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }

                    System.out.flush();
                    System.setOut(old);

                    String output = baos.toString();
                    java.util.List<Integer> sequence = extractSequence(output, start);

                    int totalSeek = 0;
                    for (String line : output.split("\n")) {
                        if (line.contains("Total Seek Distance:")) {
                            try {
                                totalSeek = Integer.parseInt(line.split(":")[1].trim());
                            } catch (Exception ex) {}
                        }
                    }
                    algoNames.add(selectedAlgo);
                    totalSeeks.add(totalSeek);

                    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                    
                    JTextArea outputArea = new JTextArea(output);
                    outputArea.setEditable(false);
                    JScrollPane scrollText = new JScrollPane(outputArea);
                    
                    JPanel graphPanel = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            if (!sequence.isEmpty()) {
                                drawDiskSchedulingGraph(g, sequence.stream().mapToInt(Integer::intValue).toArray(), getWidth(), getHeight(), selectedAlgo);
                            }
                        }
                    };
                    graphPanel.setBackground(bgColor);
                    
                    splitPane.setTopComponent(scrollText);
                    splitPane.setBottomComponent(graphPanel);
                    splitPane.setDividerLocation(150);
                    
                    tabbedPane.addTab(selectedAlgo, splitPane);
                }

                // Add Performance Metrics Tab
                StringBuilder summaryText = new StringBuilder();
                summaryText.append(String.format("%-12s | %-20s | %-20s\n", "Algorithm", "Total Head Movement", "Avg Seek Distance"));
                summaryText.append("-----------------------------------------------------------------\n");
                for (int i = 0; i < algoNames.size(); i++) {
                    double avg = (double) totalSeeks.get(i) / cylinders.length;
                    summaryText.append(String.format("%-12s | %-20d | %-20.2f\n", algoNames.get(i), totalSeeks.get(i), avg));
                }

                JTextArea summaryArea = new JTextArea(summaryText.toString());
                summaryArea.setEditable(false);
                summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
                JScrollPane summaryScroll = new JScrollPane(summaryArea);

                JPanel chartPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (totalSeeks.isEmpty()) return;
                        int maxSeek = java.util.Collections.max(totalSeeks);
                        if (maxSeek == 0) maxSeek = 1; // prevent divide-by-zero
                        int barWidth = getWidth() / (totalSeeks.size() * 2 + 1);
                        for (int i = 0; i < totalSeeks.size(); i++) {
                            int barHeight = (int) (((double) totalSeeks.get(i) / maxSeek) * (getHeight() - 80));
                            int x = barWidth + i * 2 * barWidth;
                            int y = getHeight() - 50 - barHeight;
                            g.setColor(new Color(100, 150, 200));
                            g.fillRect(x, y, barWidth, barHeight);
                            g.setColor(Color.BLACK);
                            g.drawRect(x, y, barWidth, barHeight);
                            g.drawString(algoNames.get(i), x, getHeight() - 30);
                            g.drawString(String.valueOf(totalSeeks.get(i)), x, y - 5);
                        }
                    }
                };
                chartPanel.setBackground(bgColor);

                JSplitPane summarySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                summarySplit.setTopComponent(summaryScroll);
                summarySplit.setBottomComponent(chartPanel);
                summarySplit.setDividerLocation(150);

                tabbedPane.addTab("Performance Metrics", summarySplit);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        frame.setVisible(true);
    }

    private void invokeCalculateSeek(String className, int[] cylinders, int start) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod("calculateSeek", int[].class, int.class);
        method.invoke(null, cylinders, start);
    }

    private void invokeCalculateSeekWithDir(String className, int[] cylinders, int start, String dir) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod("calculateSeek", int[].class, int.class, String.class);
        method.invoke(null, cylinders, start, dir);
    }

    private void invokeCalculateSeekWithSize(String className, int[] cylinders, int start, String dir, int size) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod("calculateSeek", int[].class, int.class, String.class, int.class);
        method.invoke(null, cylinders, start, dir, size);
    }

    private void drawDiskSchedulingGraph(Graphics g, int[] sequence, int width, int height, String algorithmName) {
        Graphics2D g2d = (Graphics2D) g;

        double axisY = 80;
        double startX = 80;
        double scale = (width - 160) / 200.0;
        double yStep = 40;

        // Draw horizontal axis
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine((int) startX, (int) axisY, (int) (startX + 200 * scale), (int) axisY);

        // Draw axis label
        g2d.drawString("Cylinders", (int) (startX + 200 * scale - 80), (int) (axisY + 30));

        // Draw ticks and labels
        for (int i = 0; i <= 200; i += 20) {
            double x = startX + i * scale;
            g2d.drawLine((int) x, (int) (axisY - 5), (int) x, (int) (axisY + 5));
            g2d.drawString(String.valueOf(i), (int) (x - 15), (int) (axisY + 20));
        }

        double currentY = axisY + 50;

        // Draw zig-zag path with arrows
        g2d.setColor(new Color(0, 100, 200));
        for (int i = 0; i < sequence.length - 1; i++) {
            double x1 = startX + sequence[i] * scale;
            double x2 = startX + sequence[i + 1] * scale;
            double y1 = currentY;
            double y2 = currentY + yStep;

            // Draw line
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

            // Draw circle at start point
            g2d.fillOval((int) (x1 - 4), (int) (y1 - 4), 8, 8);

            // Draw arrow
            drawArrow(g2d, x1, y1, x2, y2);

            // Draw distance
            int distance = Math.abs(sequence[i + 1] - sequence[i]);
            g2d.setColor(Color.RED);
            g2d.drawString("Δ=" + distance, (int) ((x1 + x2) / 2), (int) ((y1 + y2) / 2 - 5));
            g2d.setColor(new Color(0, 100, 200));

            currentY += yStep;
        }

        // Draw final point
        if (sequence.length > 0) {
            double lastX = startX + sequence[sequence.length - 1] * scale;
            double lastY = currentY - yStep;
            g2d.setColor(new Color(0, 100, 200));
            g2d.fillOval((int) (lastX - 4), (int) (lastY - 4), 8, 8);
        }

        // Legend with algorithm name
        g2d.setColor(Color.BLACK);
        g2d.drawString("Algorithm: " + algorithmName + " | Start: " + sequence[0], 20, 30);
    }

    private void drawArrow(Graphics2D g, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 10;

        double x = x2;
        double y = y2;

        double xA = x - arrowLength * Math.cos(angle - Math.PI / 6);
        double yA = y - arrowLength * Math.sin(angle - Math.PI / 6);

        double xB = x - arrowLength * Math.cos(angle + Math.PI / 6);
        double yB = y - arrowLength * Math.sin(angle + Math.PI / 6);

        int[] xPoints = {(int) x, (int) xA, (int) xB};
        int[] yPoints = {(int) y, (int) yA, (int) yB};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    private java.util.List<Integer> extractSequence(String output, int start) {
        java.util.List<Integer> sequence = new java.util.ArrayList<>();
        sequence.add(start);

        String[] lines = output.split("\n");
        for (String line : lines) {
            if (line.contains("Move from")) {
                try {
                    // Parse "Move from X to Y = distance"
                    String[] parts = line.split("Move from|to|=");
                    if (parts.length >= 3) {
                        int to = Integer.parseInt(parts[2].trim().split("\\s")[0]);
                        if (sequence.isEmpty() || sequence.get(sequence.size() - 1) != to) {
                            sequence.add(to);
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip lines that don't parse correctly
                }
            }
        }

        return sequence;
    }}