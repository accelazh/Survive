package survive;

import java.awt.*;

import javax.swing.*;

import java.awt.image.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * ��Ϸ��������
 * @author ZYL
 *
 */
public class GamePanel extends JPanel
implements Constants, ActionListener, KeyListener
{
	public static final Dimension DEFAULT_SIZE=new Dimension(200,300);
	public static final File GRADES_FILE=new File("grades.txt");
	
	private boolean started=false;  //��ʶ��Ϸ�Ƿ��Ѿ���ʼ
	private int heighScore=-1;   //��ֵ���ߵ�ʱ��,��λ������
	private javax.swing.Timer timer=new javax.swing.Timer(TIMER_INTERVAL,this);
	
	//��Ϸ�����б仯����
	private int score=0;   //��ǰ��ֵ�ʱ��
	private Craft craft=new Craft();
	private java.util.List<Ball> balls=new LinkedList<Ball>();
	
	private boolean left=false;  //ָʾ�ƶ�������ͬ
	private boolean right=false;
	private boolean down=false;
	private boolean up=false;
	
	private int counter=0;  //���������ӵ���counter
	
	public GamePanel()
	{
		this.setPreferredSize(DEFAULT_SIZE);
		this.setBackground(Color.BLACK);
		
		this.addKeyListener(this);
		
		refresh();
	}
	
	/**
	 * ��Ϸ��ʼ��ʱ���ʼ��
	 */
	public void refresh()
	{
		craft.initialize();
		balls.clear();
		score=0;
		
		left=false;
		right=false;
		down=false;
		up=false;
	}
	
	public void start()
	{
		refresh();
		started=true;
		timer.start();
	}
	
	public void stop(boolean succ)
	{
		started=false;
		timer.stop();
		
		if(succ)
		{
			if(heighScore>0)
			{
				heighScore=Math.max(heighScore, score);
			}
			else
			{
				heighScore=score;
			}
		}
		else
		{
			if(heighScore>0)
			{
				heighScore=Math.max(heighScore, score);
			}
			else
			{
				heighScore=score;
			}
		}
		
		//��¼����
		//���ļ��м�¼�ɼ�
		try
		{
			PrintWriter out=new PrintWriter(new FileWriter(GRADES_FILE, true));
			
			if(GRADES_FILE.exists())
			{
				out.append(getGrades());
			}
			else
			{
				out.print(getGrades());
			}
			
			out.close();
			
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		repaint();
	}
	
	/**
	 * �õ��ɼ�
	 * @return ��һϵ�гɼ���ɵĳɼ�����
	 */
	public String getGrades()
	{
		String output="";
		output+="\n";
		
		output+="=======================================================\n";
		output+="Achieved When: "+(new java.util.Date().toString())+"\n\n";
		
		output+="Time: "+score+"\n";
		output+="\n";
		
		output+="====Current Settings "+"====\n";
		output+="Ball Average Velocity: "+ConfigValues.getBallAverageV()+" pts/s"+"\n";
		output+="Craft Velocity: "+ConfigValues.getCraftAverageV()+" pts/s^2"+"\n";
		output+="Ball Generating Cycle: "+ConfigValues.getBallGenerageCycle()+" 10ms"+"\n";
		output+="=======================================================";
		
		output+="\n";
		
		return output;
	}
	
	public void pause()
	{
		timer.stop();
	}
	
	public void resume()
	{
		if(started)
		{
			timer.start();
		}
	}
	
	/**
	 * ��ConfigWrap��װ�Ĳ���ֵ�������趨������
	 * ֻ�е�start==false,����wrap.checkValid()
	 * ==true��ʱ��Ż���Ч
	 * 
	 * @param wrap
	 */
	public void setValues(ConfigWrap wrap)
	{
		if(!started&&wrap!=null&&wrap.checkValid())
		{
			ConfigValues.setBallAverageV(wrap.getBallAV());
			ConfigValues.setCraftAverageV(wrap.getCraftV());
			ConfigValues.setBallGenerageCycle(wrap.getBallGT());
			
			repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(started)
		{
			if(e.getSource()==timer)
			{
				//����ʱ��
				score++;
				
				//ˢ���ӵ�
				for(int i=0;i<balls.size();i++)
				{
					Ball ball=balls.get(i);
					ball.selfProcess();
					if(!ball.isValid())
					{
						balls.remove(ball);
					}
				}
				
				//ˢ��craft
				int hDir=NONE;
				if(left&&!right)
				{
					hDir=LEFT;
				}
				else
				{
					if(!left&&right)
					{
						hDir=RIGHT;
					}
				}
				
				int vDir=NONE;
				if(up&&!down)
				{
					vDir=UP;
				}
				else
				{
					if(!up&&down)
					{
						vDir=DOWN;
					}
				}
				
				craft.selfProcess(hDir, vDir, balls);
				
				//�½��ӵ�
				if(counter>=ConfigValues.getBallGenerageCycle())
				{
					balls.add(Ball.createBall());
					counter=0;
				}
				else
				{
					counter++;
				}
				
				//����Ƿ������Ϸ
				if(craft.isDestroyed())
				{
					stop(false);
				}
				
			}
		}
		
		//��������
		repaint();
	}

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == 'W')
		{
			up = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == 'S')
		{
			down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == 'A')
		{
			left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == 'D')
		{
			right = true;
		}
		
	}

	 
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == 'W')
		{
			up = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == 'S')
		{
			down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == 'A')
		{
			left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == 'D')
		{
			right = false;
		}
		
		// ��ʼ��Ϸ�Ĳ���
		if(e.getKeyCode()==KeyEvent.VK_ENTER&&!started)
		{
			start();
		}
		
		//��ʾ�������Ĳ���
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE&&!started)
		{
			showConfigDialog();
		}
		
	}
	
	/**
	 * ��ʾ�������
	 */
	public void showConfigDialog()
	{
		new ConfigDialog(this);
	}

	public void keyTyped(KeyEvent e)
	{
		
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw balls
		Iterator<Ball> iterator=balls.iterator();
		while(iterator.hasNext())
		{
			Ball ball=iterator.next();
			ball.paint(g);
		}
		
		//draw craft
		craft.paint(g, this);
		
		//draw info
	    String info=null;
	    if(!started)
	    {
	    	info="ENTER to start, ESC to config";
	    }
	    else
	    {
	    	info="Hold on!!!";
	    }
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Times", Font.BOLD, 10));
	    FontMetrics fm=g.getFontMetrics();
	    g.drawString(info, (getWidth()-fm.stringWidth(info))/2, fm.getHeight());
	    
	    //draw score
	    String time="Time: "+score;
	    String lowestTime=null;
	    if(heighScore>0)
	    {
	    	lowestTime="Record: "+heighScore;
	    }
	    else
	    {
	    	lowestTime="Record: "+"empty";
	    }
	    g.drawString(time, 5, getHeight()-5-fm.getHeight());
	    g.drawString(lowestTime, 5, getHeight()-5);
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		GamePanel p=null;
		frame.getContentPane().add(p=new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Survive");
		frame.pack();
		frame.setLocationRelativeTo(null);
		p.requestFocusInWindow();
		
		frame.setVisible(true);
	}
}
