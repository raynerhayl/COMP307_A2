package decisionTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class GUI extends JFrame {

	Node root;
	Node selected_node = null;

	ActionListener main;
	JTextPane textPane = new JTextPane();

	JPanel canvas;

	public GUI(ActionListener main) {
		this.main = main;

		this.setSize(new Dimension(500, 400));
		this.setLocationRelativeTo(null);
		this.setContentPane(initializeGUI());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public JPanel initializeGUI() {
		JPanel cP = new JPanel();
		cP.setLayout(new BoxLayout(cP, BoxLayout.Y_AXIS));

		JPanel centerP = new JPanel();
		{
			centerP.setLayout(new BoxLayout(centerP, BoxLayout.X_AXIS));
			JPanel buttonP = new JPanel();
			{ // Button panel
				buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.Y_AXIS));
				buttonP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
				buttonP.setMaximumSize(new Dimension(80, Integer.MAX_VALUE));
				JButton loadT = new JButton("Load Test Set");
				loadT.addActionListener(main);
				JButton loadTr = new JButton("Load Training Set");
				loadTr.addActionListener(main);
				JButton classify = new JButton("Classify");
				classify.addActionListener(main);
				buttonP.add(classify);
				buttonP.add(loadT);
				buttonP.add(loadTr);
				centerP.add(buttonP);
			}
			canvas = new JPanel() {

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g2.setRenderingHints(rh);

					if (root != null) {
						root.draw(g2);
					}
				}

			}; // Graphical canvas panel
			MyMouseAdapter mA = new MyMouseAdapter(this);
			canvas.addMouseMotionListener(mA);
			canvas.addMouseListener(mA);
			{
				canvas.setBackground(Color.lightGray);
				centerP.add(canvas);
			}
		}

		JScrollPane consoleP = new JScrollPane(textPane);
		{// console area
			textPane.setEditable(false);
			consoleP.setBackground(Color.white);
			consoleP.setPreferredSize(new Dimension(200, 50));
		}

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerP, consoleP);
		cP.add(splitPane);
		return cP;
	}

	public void println(String text) {
		textPane.setText(textPane.getText().concat(text + "\n"));
	}

	public void print(String text) {
		textPane.setText(textPane.getText().concat(text));
	}

	public void clearText() {
		textPane.setText("");
	}

	class MyMouseAdapter extends MouseAdapter {

		private GUI parent;

		public MyMouseAdapter(GUI p) {
			parent = p;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			if (root != null) {
				Node n = root.contains(e.getX(), e.getY());
				if (n != null) {
					selected_node = n;
					println("Selected Node: " + n.name + " Probability: " + n.probibility);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			if (selected_node != null) {
				int[] posDif = new int[] { e.getX() - selected_node.pos[0], e.getY() - selected_node.pos[1] };
				selected_node.changePos(posDif);
				parent.repaint();
			}
		}
	}

}
