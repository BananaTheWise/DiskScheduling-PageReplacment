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
        panel.setLayout(new GridLayout(3, 2));
        panel.setBackground(bgColor);

        JLabel refLabel = new JLabel("Page Reference String (comma/space separated):");
        JTextField refField = new JTextField("7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1");
        JLabel frameLabel = new JLabel("Number of Frames:");
        JTextField frameField = new JTextField("3");
        JButton runButton = new JButton("Run");

        panel.add(refLabel);
        panel.add(refField);
        panel.add(frameLabel);
        panel.add(frameField);
        panel.add(new JLabel()); // empty
        panel.add(runButton);

        frame.add(panel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane, BorderLayout.CENTER);

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

                tabbedPane.removeAll();

                String[] algorithms = {"FIFO", "LRU", "Optimal", "SecondChance"};
                String[] classNames = {
                    "algorithms.PageReplacement.FIFO",
                    "algorithms.PageReplacement.LRU", // Assuming LRU exists
                    "algorithms.PageReplacement.Optimal",
                    "algorithms.PageReplacement.secondChance" // Note: lowercase 's' for 'secondChance'
                };
                
                java.util.List<String> algoNames = new java.util.ArrayList<>();
                java.util.List<Integer> totalPageFaults = new java.util.ArrayList<>();

                for (int j = 0; j < algorithms.length; j++) {
                    String selectedAlgo = algorithms[j];
                    String className = classNames[j];

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    PrintStream old = System.out;
                    System.setOut(ps);

                    try {
                        System.out.println(selectedAlgo + " Page Replacement");
                        invokeSimulate(className, pages, frameNo);
                    } catch (Exception ex) {
                        System.out.println("Error running " + selectedAlgo + ": " + ex.getMessage());
                        ex.printStackTrace();
                    }

                    System.out.flush();
                    System.setOut(old);

                    String output = baos.toString();
                    int faults = 0;
                    for (String line : output.split("\n")) {
                        if (line.contains("Total Page Faults =")) {
                            try {
                                faults = Integer.parseInt(line.split("=")[1].trim());
                            } catch (Exception ex) {}
                        }
                    }
                    algoNames.add(selectedAlgo);
                    totalPageFaults.add(faults);

                    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                    
                    JTextArea outputArea = new JTextArea(output);
                    outputArea.setEditable(false);
                    JScrollPane scrollText = new JScrollPane(outputArea);
                    
                    // For page replacement, a complex graph isn't standard in the same way as disk scheduling.
                    // We'll keep a placeholder panel for consistency, but it won't draw anything specific
                    // unless you implement a visual representation of frames changing.
                    JPanel graphPanel = new JPanel(); 
                    graphPanel.setBackground(bgColor);
                    
                    splitPane.setTopComponent(scrollText);
                    splitPane.setBottomComponent(graphPanel);
                    splitPane.setDividerLocation(200); // Adjust as needed
                    
                    tabbedPane.addTab(selectedAlgo, splitPane);
                }

                // Add Performance Metrics Tab
                StringBuilder summaryText = new StringBuilder();
                summaryText.append(String.format("%-15s | %-15s\n", "Algorithm", "Total Page Faults"));
                summaryText.append("----------------------------------\n");
                for (int i = 0; i < algoNames.size(); i++) {
                    summaryText.append(String.format("%-15s | %-15d\n", algoNames.get(i), totalPageFaults.get(i)));
                }

                JTextArea summaryArea = new JTextArea(summaryText.toString());
                summaryArea.setEditable(false);
                summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
                JScrollPane summaryScroll = new JScrollPane(summaryArea);

                JPanel chartPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (totalPageFaults.isEmpty()) return;

                        int maxFaults = 0;
                        if (!totalPageFaults.isEmpty()) {
                            maxFaults = java.util.Collections.max(totalPageFaults);
                        }
                        if (maxFaults == 0) maxFaults = 1; // prevent divide-by-zero

                        int barWidth = getWidth() / (totalPageFaults.size() * 2 + 1);
                        for (int i = 0; i < totalPageFaults.size(); i++) {
                            int barHeight = (int) (((double) totalPageFaults.get(i) / maxFaults) * (getHeight() - 80));
                            int x = barWidth + i * 2 * barWidth;
                            int y = getHeight() - 50 - barHeight;
                            g.setColor(new Color(200, 100, 100)); // Reddish color for page faults
                            g.fillRect(x, y, barWidth, barHeight);
                            g.setColor(Color.BLACK);
                            g.drawRect(x, y, barWidth, barHeight);
                            g.drawString(algoNames.get(i), x, getHeight() - 30);
                            g.drawString(String.valueOf(totalPageFaults.get(i)), x, y - 5);
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
