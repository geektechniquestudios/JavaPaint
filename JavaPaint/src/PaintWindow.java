import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class PaintWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton brushButton, 
		lineButton, 
		ellipseButton, 
		rectangleButton, 
		strokeButton, 
		fillButton;
	//Shape aShape;
	int currentAction = 1;
	
	Color strokeColor = Color.BLACK, 
		  fillColor = Color.BLACK;
	
	public static void main(String[] args) 
	{
		new PaintWindow();
	}
	
	public PaintWindow()
	{
		this.setSize(500, 500);
		this.setTitle("Geek Technique Paint");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel buttonPanel = new JPanel();
		
		Box someBox = Box.createHorizontalBox();
		
		brushButton = makeMeButtons("./src/Brush.png", 1); 
		lineButton = makeMeButtons("./src/Line.png", 2);
		ellipseButton = makeMeButtons("./src/Ellipse.png", 3);
		rectangleButton = makeMeButtons("./src/Rectangle.png", 4);
		
		strokeButton = makeMeColorButtons("./src/Stroke.png", 5, true);
		fillButton = makeMeColorButtons("./scr/Fill.png", 6, false);
		
		someBox.add(brushButton);
		someBox.add(lineButton);
		someBox.add(ellipseButton);
		someBox.add(rectangleButton);
		someBox.add(strokeButton);
		someBox.add(fillButton);
		
		buttonPanel.add(someBox);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(new DrawingBoard(), BorderLayout.CENTER);
		this.setVisible(true);
	}//should be good to here
	
	public JButton makeMeButtons(String iconFile, final int actionNumber)
	{
		JButton theButton = new JButton();
		Icon buttonIcon = new ImageIcon(iconFile);
		theButton.setIcon(buttonIcon); 
		
		theButton.addActionListener
		(
			new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					currentAction = actionNumber;
					System.out.println(actionNumber);
				}
			}
		);
		
		return theButton;
	}
	
	public JButton makeMeColorButtons(String iconFile, final int actionNumber, final boolean stroke)
	{	
		JButton theButton = new JButton();
		Icon buttonIcon = new ImageIcon(iconFile);
		theButton.setIcon(buttonIcon); 
		
		theButton.addActionListener
		(
			new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					if(stroke)
					{
						strokeColor = JColorChooser.showDialog(null, "Pick a stroke", Color.BLACK);
					}
					else
					{
						fillColor = JColorChooser.showDialog(null, "Pick a fill", Color.BLACK);

					}
				}
			}
		);
		
		return theButton;
	}
	
	private class DrawingBoard extends JComponent
	{
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		ArrayList<Color> shapeFill = new ArrayList<Color>();
		ArrayList<Color> shapeStroke = new ArrayList<Color>();
		Point drawStart, drawEnd;
		
		public DrawingBoard()
		{
			this.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					drawStart = new Point(e.getX(), e.getY());
					drawEnd = drawStart;
					repaint();
				}
				
				public void mouseReleased(MouseEvent e)
				{
					Shape aShape = drawRectangle(drawStart.x, drawStart.y, e.getX(), e.getY());
					
					shapes.add(aShape);
					shapeFill.add(fillColor);
					shapeStroke.add(strokeColor);
					
					drawStart = null;
					drawEnd = null;
					repaint();
				}
			});
			
			this.addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseDragged(MouseEvent e)
				{ 
					drawEnd = new Point(e.getX(), e.getY());
					repaint();
				}
			});	
		}
		
		public void paint(Graphics g)
		{
			Graphics2D graphSettings = (Graphics2D)g;
			graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphSettings.setStroke(new BasicStroke(2));
			
			Iterator<Color> strokeCounter = shapeStroke.iterator();
			Iterator<Color> fillCounter = shapeFill.iterator();
			
			graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
			for (Shape s : shapes)
			{
				graphSettings.setPaint(strokeCounter.next());
				graphSettings.draw(s);
				graphSettings.setPaint(fillCounter.next());
				graphSettings.fill(s);
			}
			
			if(drawStart != null && drawEnd != null)
			{
				graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
				graphSettings.setPaint(Color.LIGHT_GRAY);
				Shape aShape = drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				graphSettings.draw(aShape);
			}
		}
		
		private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2)
		{
			int x = Math.min(x1, x2);
			int y = Math.min(y1, y2);
			
			int width = Math.abs(x1 - x2);
			int height = Math.abs(y1 - y2);
			
			return new Rectangle2D.Float(x, y, width, height);
		}
	}
}