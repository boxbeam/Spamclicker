package viperlordx.spamclicker;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Main implements NativeKeyListener {
	static File config = new File("config.txt");
	public static boolean active = false;
	public static boolean active2 = false;
	public Thread clicker;
	static int leftbutton = NativeKeyEvent.VC_F11;
	static int rightbutton = NativeKeyEvent.VC_F12;
	static JWindow indicator = new JWindow();
	static int indicatorpos = 0;
	static JButton leftind = new JButton();
	static JButton rightind = new JButton();
	volatile static int delay = 10;
	volatile static int wait = 10;
	volatile static boolean hold = false;
	public static void main(String args[]) throws AWTException, NativeHookException, IOException {
		if (config.exists()) {
			FileReader read = new FileReader(config);
			BufferedReader reader = new BufferedReader(read);
			String text = "";
			String next = "";
			while ((next = reader.readLine()) != null) {
				text = text + next;
			}
			Pattern findleft = Pattern.compile("\\[L\\:(.+?)\\]");
			Matcher leftmatcher = findleft.matcher(text);
			if (leftmatcher.find()) {
				try {
					leftbutton = Integer.parseInt(leftmatcher.group(1));
				} catch (NumberFormatException e) {
				}
			}
			Pattern findright = Pattern.compile("\\[R\\:(.+?)\\]");
			Matcher rightmatcher = findright.matcher(text);
			if (rightmatcher.find()) {
				try {
					rightbutton = Integer.parseInt(rightmatcher.group(1));
				} catch (NumberFormatException e) {
				}
			}
			{
				Pattern pattern = Pattern.compile("\\[I\\:(.+?)\\]");
				Matcher matcher = pattern.matcher(text);
				if (matcher.find()) {
					indicatorpos = Integer.parseInt(matcher.group(1));
				}
			}
			{
				Pattern pattern = Pattern.compile("\\[S\\:(.+?)\\]");
				Matcher matcher = pattern.matcher(text);
				if (matcher.find()) {
					delay = Integer.parseInt(matcher.group(1));
				}
			}
			{
				Pattern pattern = Pattern.compile("\\[H\\:(.+?)\\]");
				Matcher matcher = pattern.matcher(text);
				if (matcher.find()) {
					hold = Boolean.parseBoolean(matcher.group(1));
				}
			}
			{
				Pattern pattern = Pattern.compile("\\[W\\:(.+?)\\]");
				Matcher matcher = pattern.matcher(text);
				if (matcher.find()) {
					wait = Integer.parseInt(matcher.group(1));
				}
			}
			if (indicatorpos == 0) {
				indicator.setVisible(false);
			}
			read.close();
		}
		JPanel indpanel = new JPanel();
		indpanel.setVisible(true);
		indpanel.setBounds(0, 0, 80, 40);
		indicator.setLayout(null);
		indpanel.setLayout(null);
		indicator.add(indpanel);
		leftind.setBounds(0, 0, 40, 40);
		rightind.setBounds(40, 0, 40, 40);
		rightind.setBackground(Color.RED);
		leftind.setBackground(Color.RED);
		leftind.setText("L");
		leftind.setMargin(new Insets(0, 0, 0, 0));
		rightind.setMargin(new Insets(0, 0, 0, 0));
		rightind.setText("R");
		leftind.setVisible(true);
		rightind.setVisible(true);
		leftind.setFont(new Font("Arial", Font.PLAIN, 15));
		rightind.setFont(new Font("Arial", Font.PLAIN, 15));
		indpanel.add(leftind);
		indpanel.add(rightind);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		if (indicatorpos == 1) {
			indicator.setBounds(0, 0, 80, 40);
		}
		if (indicatorpos == 2) {
			indicator.setBounds(screensize.width - 80, 0, 80, 40);	
		}
		if (indicatorpos == 3) {
			indicator.setBounds(screensize.width - 80, screensize.height - 40, 80, 40);	
		}
		if (indicatorpos == 4) {
			indicator.setBounds(0, screensize.height - 40, 80, 40);
		}
		indicator.setAlwaysOnTop(true);
		indicator.setVisible(true);
		config.createNewFile();
		LogManager.getLogManager().reset();
		JFrame frame = new JFrame();
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
				try {
					FileWriter writer = new FileWriter(config);
					writer.write("[L:" + leftbutton + "][R:" + rightbutton + "][I:" + indicatorpos + "][S:" + delay + "][H:" + hold + "][W:" + wait + "]");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}
			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}
			@Override
			public void windowIconified(WindowEvent arg0) {
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});
		frame.setVisible(true);
		frame.setBounds(0, 0, 300, 100);
		frame.setTitle("Spamclicker");
		frame.setAlwaysOnTop(false);
		JPanel panel = new JPanel();
		frame.add(panel);
		frame.setLayout(null);
		panel.setLayout(null);
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JLabel label = new JLabel();
		label.setText("Press " + NativeKeyEvent.getKeyText(leftbutton) + " for left mouse, " + NativeKeyEvent.getKeyText(rightbutton) + " for right.");
		panel.add(label);
		JButton button = new JButton();
		button.setText("Configure");
		button.setBounds(0, 40, 100, 20);
		button.setVisible(true);
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					JFrame rebind = new JFrame();
					rebind.setResizable(false);
					rebind.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					rebind.setBounds(0, 0, 400, 160);
					rebind.setVisible(true);
					rebind.setTitle("Configure");
					rebind.setLayout(null);
					JTextField setwait = new JTextField();
					setwait.setBounds(0, 100, 400, 20);
					setwait.setText(String.valueOf(wait));
					setwait.setToolTipText("Delay after release");
					setwait.setEditable(hold);
					setwait.setVisible(true);
					setwait.addKeyListener(new KeyListener() {
						@Override
						public void keyPressed(KeyEvent arg0) {
							if (setwait.getText().matches("[^0-9]")) {
								setwait.setText(setwait.getText().replaceAll("[^0-9]", ""));
							}
						}
						@Override
						public void keyReleased(KeyEvent arg0) {
							if (setwait.getText().matches("[^0-9]")) {
								setwait.setText(setwait.getText().replaceAll("[^0-9]", ""));
							}
							if (!setwait.getText().equals("")) {
								wait = Integer.parseInt(setwait.getText());
							}
						}
						@Override
						public void keyTyped(KeyEvent arg0) {
							if (setwait.getText().matches("[^0-9]")) {
								setwait.setText(setwait.getText().replaceAll("[^0-9]", ""));
							}
							if (!setwait.getText().equals("")) {
								wait = Integer.parseInt(setwait.getText());
							}
						}
					});
					JButton sethold = new JButton();
					sethold.setBounds(0, 80, 400, 20);
					sethold.setText("Hold click: " + hold);
					sethold.addMouseListener(new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
							hold = !hold;
							sethold.setText("Hold click: " + hold);
							setwait.setEditable(hold);
						}
						@Override
						public void mouseEntered(MouseEvent e) {
						}
						@Override
						public void mouseExited(MouseEvent e) {
						}
						@Override
						public void mousePressed(MouseEvent e) {
						}
						@Override
						public void mouseReleased(MouseEvent e) {
						}
					});
					JTextField setspeed = new JTextField();
					setspeed.setVisible(true);
					setspeed.setBounds(0, 60, 400, 20);
					setspeed.setToolTipText("Delay (milliseconds)");
					setspeed.setText(String.valueOf(delay));
					setspeed.addKeyListener(new KeyListener() {
						@Override
						public void keyPressed(KeyEvent arg0) {
							if (setspeed.getText().matches("[^0-9]")) {
								setspeed.setText(setspeed.getText().replaceAll("[^0-9]", ""));
							}
						}
						@Override
						public void keyReleased(KeyEvent arg0) {
							if (setspeed.getText().matches("[^0-9]")) {
								setspeed.setText(setspeed.getText().replaceAll("[^0-9]", ""));
							}
							if (!setspeed.getText().equals("")) {
								delay = Integer.parseInt(setspeed.getText());
							}
						}
						@Override
						public void keyTyped(KeyEvent arg0) {
							if (setspeed.getText().matches("[^0-9]")) {
								setspeed.setText(setspeed.getText().replaceAll("[^0-9]", ""));
							}
							if (!setspeed.getText().equals("")) {
								delay = Integer.parseInt(setspeed.getText());
							}
						}
					});
					JPanel rpanel = new JPanel();
					{
						JButton indpos = new JButton();
						indpos.setVisible(true);
						indpos.setBounds(0,40,400,20);
						indpos.setText("Indicator position: " + indicatorpos);
						if (indicatorpos == 0) {
							indpos.setText("Indicator position: OFF");
						}
						indpos.setToolTipText("Indicator position");
						indpos.addMouseListener(new MouseListener() {
							@Override
							public void mouseClicked(MouseEvent e) {
								indicatorpos++;
								if (indicatorpos > 4) {
									indicatorpos = 0;
								}
								indpos.setText("Indicator position: " + indicatorpos);
								indicator.setAlwaysOnTop(true);
								indicator.setVisible(true);
								if (indicatorpos == 0) {
									indpos.setText("Indicator position: OFF");
									indicator.setAlwaysOnTop(false);
									indicator.setVisible(false);
								}
								Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
								if (indicatorpos == 1) {
									indicator.setBounds(0, 0, 80, 40);	
								}
								if (indicatorpos == 2) {
									indicator.setBounds(screensize.width - 80, 0, 80, 40);	
								}
								if (indicatorpos == 3) {
									indicator.setBounds(screensize.width - 80, screensize.height - 40, 80, 40);	
								}
								if (indicatorpos == 4) {
									indicator.setBounds(0, screensize.height - 40, 80, 40);	
								}
							}
							@Override
							public void mouseEntered(MouseEvent e) {
							}
							@Override
							public void mouseExited(MouseEvent e) {
							}
							@Override
							public void mousePressed(MouseEvent e) {
							}
							@Override
							public void mouseReleased(MouseEvent e) {
							}
						});
						rpanel.add(indpos);
					}
					rpanel.add(setspeed);
					rpanel.add(sethold);
					rpanel.add(setwait);
					rpanel.setLayout(null);
					rpanel.setBounds(0, 0, 400, 200);
					rpanel.setVisible(true);
					rebind.add(rpanel);
					JButton leftbind = new JButton();
					leftbind.setToolTipText("Left mouse binding");
					leftbind.setBounds(0, 0, 400, 20);
					leftbind.setVisible(true);
					String text = NativeKeyEvent.getKeyText(leftbutton);
					leftbind.setText("Left mouse - " + text);
					rpanel.add(leftbind);
					JButton rightbind = new JButton();
					rightbind.setToolTipText("Left mouse binding");
					rightbind.setBounds(0, 20, 400, 20);
					rightbind.setVisible(true);
					text = NativeKeyEvent.getKeyText(rightbutton);
					rightbind.setText("Right mouse - " + text);
					rpanel.add(rightbind);
					leftbind.addMouseListener(new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
							leftbind.setText("> Left mouse <");
							leftbind.addKeyListener(new KeyListener() {
								@Override
								public void keyPressed(KeyEvent e) {
									String text = "";
									try {
										text = NativeKeyEvent.getKeyText(KeyBridge.getNativeKeyCode(e.getKeyCode()));
									} catch (AWTException | NativeHookException e2) {
										e2.printStackTrace();
									}
									leftbind.setText("Left mouse - " + text);
									try {
										leftbutton = KeyBridge.getNativeKeyCode(e.getKeyCode());
									} catch (AWTException | NativeHookException e1) {
										e1.printStackTrace();
									}
									label.setText("Press " + NativeKeyEvent.getKeyText(leftbutton) + " for left mouse, " + NativeKeyEvent.getKeyText(rightbutton) + " for right.");
								}
								@Override
								public void keyReleased(KeyEvent arg0) {
								}
								@Override
								public void keyTyped(KeyEvent arg0) {
								}
							});
						}
						@Override
						public void mouseEntered(MouseEvent arg0) {
						}
						@Override
						public void mouseExited(MouseEvent arg0) {
						}
						@Override
						public void mousePressed(MouseEvent arg0) {
						}
						@Override
						public void mouseReleased(MouseEvent arg0) {
						}
					});
					rightbind.addMouseListener(new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
							rightbind.setText("> Right mouse <");
							rightbind.addKeyListener(new KeyListener() {
								@Override
								public void keyPressed(KeyEvent e) {
									String text = "";
									try {
										text = NativeKeyEvent.getKeyText(KeyBridge.getNativeKeyCode(e.getKeyCode()));
									} catch (AWTException | NativeHookException e2) {
										e2.printStackTrace();
									}
									rightbind.setText("Right mouse - " + text);
									try {
										rightbutton = KeyBridge.getNativeKeyCode(e.getKeyCode());
									} catch (AWTException | NativeHookException e1) {
										e1.printStackTrace();
									}
									label.setText("Press " + NativeKeyEvent.getKeyText(leftbutton) + " for left mouse, " + NativeKeyEvent.getKeyText(rightbutton) + " for right.");
								}
								@Override
								public void keyReleased(KeyEvent arg0) {
								}
								@Override
								public void keyTyped(KeyEvent arg0) {
								}
							});
						}
						@Override
						public void mouseEntered(MouseEvent arg0) {
						}
						@Override
						public void mouseExited(MouseEvent arg0) {
						}
						@Override
						public void mousePressed(MouseEvent arg0) {
						}
						@Override
						public void mouseReleased(MouseEvent arg0) {
						}
					});
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		label.setBounds(0, 0, 300, 20);
		panel.setVisible(true);
		panel.setBounds(0, 0, 300, 100);
		label.setVisible(true);
		frame.setResizable(false);
		panel.add(button);
		Robot robot = new Robot();
		Thread clicker = new Thread() {
			public void run() {
				while (true) {
					try {
						if (!hold) {
							Thread.sleep(delay);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (active) {
						robot.mousePress(InputEvent.BUTTON1_MASK);
						if (hold) {
							try {
								Thread.sleep(delay);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
						if (hold) {
							try {
								Thread.sleep(wait);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						leftind.setBackground(Color.GREEN);
					} else {
						leftind.setBackground(Color.RED);
					}
				}
			}
		};
		Thread clicker2 = new Thread() {
			public void run() {
				while (true) {
					try {
						if (!hold) {
							Thread.sleep(delay);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (active2) {
						robot.mousePress(InputEvent.BUTTON3_MASK);
						if (hold) {
							try {
								Thread.sleep(delay);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						robot.mouseRelease(InputEvent.BUTTON3_MASK);
						if (hold) {
							try {
								Thread.sleep(wait);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						rightind.setBackground(Color.GREEN);
					} else {
						rightind.setBackground(Color.RED);
					}
				}
			}
		};
		clicker.start();
		clicker2.start();
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(new Main());
	}
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == leftbutton) {
			active = !active;
			if (active) {
				leftind.setBackground(Color.GREEN);
			} else {
				leftind.setBackground(Color.RED);
			}
		}
		if (e.getKeyCode() == rightbutton) {
			active2 = !active2;
			if (active2) {
				rightind.setBackground(Color.GREEN);
			} else {
				rightind.setBackground(Color.RED);
			}
		}
	}
	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
	}
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}
}