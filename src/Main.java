import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

class Main extends JFrame implements MouseListener {
    JFrame myJFrame;
    Field field;
    ControlPanel controlPanel = new ControlPanel();
    Timer timer = null;
    double newParticlesVelocity = 1;

    int controlWidth, fieldWidth, height, fieldHeight, width;

    public static void main(String[] args) {
        Main main = new Main();
    }

    public Main() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        width = gd.getDisplayMode().getWidth();
        height = gd.getDisplayMode().getHeight();
        fieldWidth = (int) Math.round(width * 0.795);
        fieldHeight = height - 100;
        controlWidth = width - fieldWidth;

        myJFrame = new JFrame("BagelVulture's Bad Particle Simulation");
        myJFrame.setSize(width, height);
        myJFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        field = new Field();
        field.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        field.setBorder(BorderFactory.createLineBorder(Color.black));

        controlPanel.setPreferredSize(new Dimension(controlWidth, fieldHeight));
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        Container content = myJFrame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(field, BorderLayout.WEST);
        content.add(controlPanel, BorderLayout.EAST);

        pack();

        myJFrame.setVisible(true);

        timer.start();
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    int N = 500;
    int terminalVelocity = 6;
    double gravity = 0.03;
    double entropy = 0.9;
    int size = 16;
    int speed = 10;
    boolean pskMode = true;
    boolean pmMode = false;
    boolean multicolor = false;

    class Field extends JPanel {
        ParticleSystem particles;

        public Field() {
            init(N, fieldWidth, fieldHeight);

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && pskMode) {
                        particles.addParticle(e.getX(), e.getY());
                        N++;
                        controlPanel.updateSlider("Number of Particles", N, 1);
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON3 && pskMode) {
                        boolean J = particles.removeParticle(e.getX(), e.getY(), size);
                        if (J) {
                            N--;
                            controlPanel.updateSlider("Number of Particles", N, 1);
                            repaint();
                        }
                    }
                }

            });

            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (pskMode && SwingUtilities.isLeftMouseButton(e)) {
                        particles.addParticle(e.getX(), e.getY());
                        N++;
                        controlPanel.updateSlider("Number of Particles", N, 1);
                        repaint();
                    } else if (pskMode && SwingUtilities.isRightMouseButton(e)) {
                        boolean J = particles.removeParticle(e.getX(), e.getY(), size);
                        if (J) {
                            N--;
                            controlPanel.updateSlider("Number of Particles", N, 1);
                            repaint();
                        }
                    }if (pmMode && SwingUtilities.isLeftMouseButton(e)) {
                        particles.moveTowards(e.getX(), e.getY(), size * 16, 2);
                        repaint();
                    } else if (pmMode && SwingUtilities.isRightMouseButton(e)) {
                        particles.moveTowards(e.getX(), e.getY(), size * 16, -2);
                        repaint();
                    }
                }
            });

            timer = new Timer(speed, e -> {
                particles.update(fieldWidth, fieldHeight, terminalVelocity, gravity, entropy, size);
                repaint();
            });
        }

        public void init(int N, int fieldWidth, int height) {
            particles = new ParticleSystem(N, fieldWidth, height, newParticlesVelocity);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            particles.draw(g2d, size, multicolor);
        }
    }


    class ControlPanel extends JPanel {

        private final Map<String, JSlider> sliders = new HashMap<>();
        private final Map<String, JTextField> sliderTextFields = new HashMap<>();

        public ControlPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel Panel = new JPanel();
            Panel.setPreferredSize(new Dimension(controlWidth - 10, height / 4));
            Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));

            Panel.add(createSliderWithTextBox(
                    "Entropy (In Unknown Arbitrary Units)", 0, 100, 90,
                    val -> entropy = val / 100, 100));

            Panel.add(createSliderWithTextBox(
                    "Terminal Velocity (In Pixels Per Tick)", 1, 25, 6,
                    val -> terminalVelocity = (int) val, 1));

            Panel.add(createSliderWithTextBox(
                    "Gravity (In Pixels Per Tick Per Tick)", 0, 1000, 30,
                    val -> gravity = val / 1000, 1000));

            Panel.add(createSliderWithTextBox(
                    "Number of Particles", 1, 1500, 500,
                    val -> N = (int) val, 1));

            Panel.add(createSliderWithTextBox(
                    "Size of Particles (In Pixels)", 4, 100, 16,
                    val -> size = (int) val, 1));

            Panel.add(createSliderWithTextBox(
                    "Velocity of New Particles", 0, 300, 100,
                    val -> newParticlesVelocity = val / 100, 100));

            Panel.add(createSliderWithTextBox(
                    "Speed of the Simulation (In Ms Between Ticks)", 1, 100, 10,
                    val -> {
                        speed = (int) val;
                        if (timer != null) {
                            timer.setDelay(speed);
                        }
                    }, 1));

            JButton resetSlidersButton = new JButton("Reset Sliders");
            resetSlidersButton.addActionListener(e -> {
                resetSliders();
            });

            JButton resetButton = new JButton("Reset Particles");
            resetButton.addActionListener(e -> {
                field.init(N, fieldWidth, fieldHeight);
                timer.restart();
                field.repaint();
            });

            JButton startstopButton = new JButton("Pause/Resume The Simulation");
            startstopButton.addActionListener(e -> {
                if (timer.isRunning()) {
                    timer.stop();
                } else {
                    timer.start();
                }
            });

            JButton pskButton = new JButton("Enable/Disable Spawning/Killing Particles");
            pskButton.addActionListener(e -> {
                if (pmMode) {
                    pmMode = false;
                }
                pskMode = !pskMode;
            });

            JButton pmButton = new JButton("Enable/Disable Moving Particles");
            pmButton.addActionListener(e -> {
                if (pskMode) {
                    pskMode = false;
                }
                pmMode = !pmMode;
            });

            JCheckBox Multicolor = new JCheckBox("Colorful Particles");
            Multicolor.addActionListener(e -> {
                multicolor = !multicolor;
            });

            resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            resetSlidersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startstopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            pskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            pmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            Multicolor.setAlignmentX(Component.CENTER_ALIGNMENT);

            Panel.add(resetButton);
            Panel.add(resetSlidersButton);
            Panel.add(startstopButton);
            Panel.add(pskButton);
            Panel.add(pmButton);
            Panel.add(Multicolor);

            add(Panel);

            setVisible(true);
        }

        private JPanel createSliderWithTextBox(String labelText, int min, int max, double initial,
                                               java.util.function.DoubleConsumer setter, int textboxValueDivider) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JLabel label = new JLabel(labelText);
            JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, (int) initial);
            slider.setPreferredSize(new Dimension(150, 20));

            JTextField textField = new JTextField(String.valueOf(initial / textboxValueDivider), 6);

            slider.addChangeListener(e -> {
                int value = ((JSlider) e.getSource()).getValue();
                textField.setText(String.valueOf((double) value / textboxValueDivider));
                setter.accept(value);
            });

            textField.addActionListener(e -> {
                try {
                    double val = Double.parseDouble(textField.getText()) * textboxValueDivider;
                    setter.accept(val);
                    if (val < slider.getMinimum()) slider.setMinimum((int) Math.floor(val));
                    if (val > slider.getMaximum()) slider.setMaximum((int) Math.ceil(val));
                    slider.setValue((int) val);
                } catch (NumberFormatException ignored) {
                }
            });

            panel.add(label);
            panel.add(slider);
            panel.add(textField);

            sliders.put(labelText, slider);
            sliderTextFields.put(labelText, textField);

            return panel;
        }

        private void resetSliders() {
            N = 500;
            terminalVelocity = 6;
            gravity = 0.03;
            size = 16;
            speed = 10;
            if (timer != null) {
                timer.setDelay(speed);
            }
            entropy = 0.9;
            newParticlesVelocity = 1;

            updateSlider("Terminal Velocity (In Pixels Per Tick)", terminalVelocity, 1);
            updateSlider("Gravity (In Pixels Per Tick Per Tick)", (int) (gravity * 1000), 1000);
            updateSlider("Number of Particles", N, 1);
            updateSlider("Size of Particles (In Pixels)", size, 1);
            updateSlider("Speed of the Simulation (In Ms Between Ticks)", speed, 1);
            updateSlider("Entropy (In Unknown Arbitrary Units)", (int) (entropy * 100), 100);
            updateSlider("Velocity of New Particles", (int) (newParticlesVelocity * 100), 100);
        }

        private void updateSlider(String key, int sliderValue, int divider) {
            JSlider slider = sliders.get(key);
            JTextField field = sliderTextFields.get(key);
            if (slider != null && field != null) {
                slider.setValue(sliderValue);
                field.setText(String.valueOf((double) sliderValue / divider));
            }
        }
    }
}