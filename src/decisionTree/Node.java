package decisionTree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {
	String attribute = "";
	int[] pos = new int[2];
	Node lChild;
	Node rChild;

	double probibility = 0; // probability that this nodes attribute is correct

	private int padding = 10;
	private final int Y_MARGIN = 40;
	private final int X_MARGIN = 10;

	private int depth = 0;

	private int width = 0;
	private int height = 0;

	public int size() {
		int size = 1;
		if (lChild != null) {
			size += lChild.size();
		}
		if (rChild != null) {
			size += rChild.size();
		}
		return size;
	}

	public Node(String att) {
		attribute = att;
	}

	public Node(String att, double probibility, int depth) {
		attribute = att;
		this.probibility = probibility;
		this.depth = depth;
	}

	public Node(String att, Node l, Node r, int depth) {
		this.attribute = att;
		lChild = l;
		rChild = r;
		this.depth = depth;
	}

	public int width(Graphics2D g2) {
		width = g2.getFontMetrics().stringWidth(attribute) + padding;
		return width - padding;
	}

	public int height(Graphics2D g2) {
		height = g2.getFontMetrics().getHeight() + padding;
		return height - padding;
	}

	public Node contains(int x, int y) {

		if (lChild != null) {
			Node n = lChild.contains(x, y);
			if (n != null) {
				return n;
			}
		}

		if (rChild != null) {
			Node n = rChild.contains(x, y);
			if (n != null) {
				return n;
			}
		}

		return (Math.pow(x - pos[0], 2) / Math.pow(width / 2, 2)
				+ Math.pow(pos[1] - y, 2) / Math.pow(height / 2, 2) < 1) ? this : null;

	}

	/**
	 * Finds the height of the tree and also populates a map of
	 * depth to a list of nodes at that depth.
	 * @param depthMap The map of depth to lists of nodes at that depth.
	 * @return the maximum depth of the tree
	 */
	public int maxDepth(Map<Integer, List<Node>> depthMap) {
		if (depthMap.containsKey(depth)) {
			depthMap.get(depth).add(this);
		} else {
			List<Node> list = new ArrayList<Node>();
			list.add(this);
			depthMap.put(depth, list);
		}
		int maxDepth = depth;
		if (this.lChild != null) {
			int lDepth = this.lChild.maxDepth(depthMap);
			maxDepth = (lDepth > maxDepth) ? lDepth : maxDepth;
		}

		if (this.rChild != null) {
			int rDepth = this.rChild.maxDepth(depthMap);
			maxDepth = (rDepth > maxDepth) ? rDepth : maxDepth;
		}

		if (this.rChild == null && this.lChild == null) {
			return depth;
		}

		return maxDepth;
	}

	/**
	 * Updates this nodes position by adding the requested difference and then
	 * does the same to each of its children.
	 * 
	 * @param poDif
	 *            the array for the difference in position
	 */
	public void changePos(int[] poDif) {
		pos[0] = pos[0] + poDif[0];
		pos[1] = pos[1] + poDif[1];

		if (lChild != null) {
			lChild.changePos(poDif);
		}

		if (rChild != null) {
			rChild.changePos(poDif);
		}
	}

	/**
	 * Draws this node and then all of its children
	 * 
	 * @param g2
	 */
	public void draw(Graphics2D g2) {

		g2.setColor(Color.white);

		if (lChild != null) {
			g2.drawLine(pos[0], pos[1], lChild.pos[0], lChild.pos[1]);

			lChild.draw(g2);
		}

		if (rChild != null) {
			g2.drawLine(pos[0], pos[1], rChild.pos[0], rChild.pos[1]);

			rChild.draw(g2);
		}

		g2.setColor(Color.darkGray);
		g2.fillOval(pos[0] - width(g2) / 2 - padding, pos[1] - height(g2) / 2 - padding, width(g2) + padding * 2,
				height(g2) + padding * 2);
		g2.setColor(Color.white);
		g2.drawString(attribute, pos[0] - width(g2) / 2, pos[1] + height(g2) / 2);

	}
}
