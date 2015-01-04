package survive;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

/**
 * �����ӵ�����
 * @author ZYL
 *
 */
public class Ball implements Constants
{
	public static final int RADIUS=2;
	
	//������������Ե���
	private double velocity=0;  //��λpts/s
	private double arc=0;  //��javaֱ������ϵΪ׼�ķ����
	
	//��������Ϸ�����в��ϱ仯������
	private double x=0;  //�����x, y�����ĵ�����
	private double y=0;
	
	private boolean isValid=true;
	
	private Ball()
	{
		
	}
	
	/**
	 * �����ӵ�ʵ���Ĺ�������
	 * @return
	 */
	public static Ball createBall()
	{
		int rand=(int)(10*Math.random());
		switch(rand)
		{
		case 0:
		{
			return createBallOnLeftHalfEdge();
		}
		case 1:
		{
			return createBallOnRightHarfEdge();
		}
		default:
		{
			return createBallOnUpEdge();
		}
		}
		
	}
	
	/**
	 * 
	 * 
	 * @param x �ӵ���x����
	 * @param y �ӵ���y����
	 * @return
	 */
	private static double selectRandomArc(double x, double y)
	{
		double arc1=getStandardArc(x, y, GamePanel.DEFAULT_SIZE.width, GamePanel.DEFAULT_SIZE.height);
		double arc2=getStandardArc(x, y, 0, GamePanel.DEFAULT_SIZE.height);
		
		if(arc1>arc2)
		{
			double temp=arc1;
			arc1=arc2;
			arc2=temp;
		}
		
		return arc1+Math.random()*(arc2-arc1);
		
	}
	
	/**
	 * ���ݴ����������A:(x1,y1), B:(x2,y2)ȷ��A->B�ĵ��ǣ���Χ��0~2*PI
	 * 
	 */
	private static double getStandardArc(double x1, double y1, double x2, double y2)
	{
		double lx=x2-x1;
		double ly=y2-y1;
		
		double length=Math.pow(lx*lx+ly*ly, 0.5);
		
		if(length<=0)
		{
			return 0;
		}
		
		double sin=ly/length;
		
		if(lx>=0&&ly>=0)
		{
			return Math.asin(sin);
		}
		else
		{
			if(lx>=0&&ly<0)
			{
				return 2*Math.PI+Math.asin(sin);
			}
			else
			{
			    return Math.PI-Math.asin(sin);
			}
		}
		
	}
	
	private static Ball createBallOnLeftHalfEdge()
	{
		Ball ball=new Ball();
		
		//���ѡ�����
		ball.x=0;
		ball.y=GamePanel.DEFAULT_SIZE.height/2*Math.random();
		
		//���ѡ�����
		ball.arc=selectRandomArc(ball.x, ball.y);
		
        //������ʼ��
	    ball.velocity=ConfigValues.getBallAverageV()*0.5+Math.random()*ConfigValues.getBallAverageV();		
        ball.isValid=true;
        
        return ball;
		
	}
	
	private static Ball createBallOnRightHarfEdge()
	{
        Ball ball=new Ball();
		
		//���ѡ�����
		ball.x=GamePanel.DEFAULT_SIZE.width;
		ball.y=GamePanel.DEFAULT_SIZE.height/2*Math.random();
		
		//���ѡ�����
		ball.arc=selectRandomArc(ball.x, ball.y);
		
        //������ʼ��
	    ball.velocity=ConfigValues.getBallAverageV()*0.5+Math.random()*ConfigValues.getBallAverageV();		
        ball.isValid=true;
        
        return ball;
        
	}
	
	private static Ball createBallOnUpEdge()
	{
        Ball ball=new Ball();
		
		//���ѡ�����
		ball.x=GamePanel.DEFAULT_SIZE.width/2+((Math.random()>=0.5)?1:-1)*GamePanel.DEFAULT_SIZE.width/2*Math.random();
		ball.y=0;
		
		//���ѡ�����
		ball.arc=selectRandomArc(ball.x, ball.y);
		
        //������ʼ��
	    ball.velocity=ConfigValues.getBallAverageV()*0.5+Math.random()*ConfigValues.getBallAverageV();		
        ball.isValid=true;
        
        return ball;
	}
	
	public void move()
	{
		x+=velocity*TIMER_INTERVAL*1.0/1000*Math.cos(arc);
		y+=velocity*TIMER_INTERVAL*1.0/1000*Math.sin(arc);
	}
	
	/**
	 * ��������ж��ӵ��Ƿ��Ѿ��ɳ���Ϸ����
	 * @return
	 */
	public boolean isOutOfPanel()
	{
		Rectangle rect=new Rectangle(-2*RADIUS, -2*RADIUS, GamePanel.DEFAULT_SIZE.width+4*RADIUS, GamePanel.DEFAULT_SIZE.height+4*RADIUS);
		return !rect.contains(new Point((int)x, (int)y));
	}
	
	public void selfProcess()
	{
		if(!isValid)
		{
			return;
		}
		
		this.move();
		if(isOutOfPanel())
		{
			this.isValid=false;
		}
	}
	
	public void paint(Graphics g)
	{
		if(!isValid)
		{
			return;
		}
		
		g.setColor(Color.WHITE);
		g.fillOval((int)(x-RADIUS), (int)(y-RADIUS), 2*RADIUS, 2*RADIUS);
	}

	public double getVelocity()
	{
		return velocity;
	}

	public double getArc()
	{
		return arc;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public boolean isValid()
	{
		return isValid;
	}
	
	
	
	

}
