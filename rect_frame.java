
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**s
 *
 * @author Henke
 */
public class rect_frame extends javax.swing.JFrame {

   
    private boolean gravity = false;
    
   
    private Timer t1;
    private int delay = 0;
    private int FRAME_WIDTH;
    private int CENTER;
    
    private Rect [] r;
    private int n_rects = 5;
    private int rect_w = 50;
    private int part_size = 50; //nr of points on lines
    private int dragged_rect = -1;      //r = 0 , r2 = 1
    private double intersect_val = 4;
    
    private double rect_differ = 0.0;
    
    private boolean double_rotation = false;
    private boolean rect_rotation = true;
    private boolean is_line = true;
    private boolean is_circle = true;
    private boolean is_tri = true;
    
    private boolean dragged_line = false;
    
    private boolean is_dragged_circle = false;
    private boolean is_dragged_rect = false;
    private boolean is_dragged_tri = false;
    
    private boolean auto_move = false;
    private double [] auto_rect_angle;
    private double [] auto_rect_speed;
    private double [] auto_circle_angle;
    private double [] auto_circle_speed;
    private double [] auto_tri_angle;
    private double [] auto_tri_speed;
    
    private double [] auto_line_angle;
    private double [] auto_line_speed;
    
    
    
    private Line line;
    private Circle [] c;
    private int c_size = 5;
    private int dragged_circle_index;
    
    private Triangle [] t;
    private int t_size = 3;
    private int dragged_t = -1;
    
    //bounce values
    
    private double up_h = 92;   //90
    private double low_h = 70;  //70
    
    private double dx0_val = 1700;    //1700
    private double dx1_val = 0.000025;   //0.000025
    
    
    /**
     * Creates new form frame
     */
    public rect_frame() {
        initComponents();
        init();
        this.setLayout(null);
        init_line();
        init_rects();
        init_circles();
        init_tri();
        init_auto_move();
        act_perf(delay);
        
    }
    
    public void init_tri()
    {
        Random rand = new Random();
        t = new Triangle[t_size];
        for(int ix = 0; ix < t_size; ix++)
        {
            double x = 50 + rand.nextInt(FRAME_WIDTH-50);
            double y = 50 + rand.nextInt(FRAME_WIDTH-50);
            double rad = 25 + ix * 10;
            double angle = rand.nextInt(629)/100.0;
            t[ix] = new Triangle(x,y,rad,angle);
        }
    }
    
    public void check_circle_line_rect()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.red);
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int k = 0; k < 4; k++)
            {
                int k0 = k;
                int k1 = k0 + 1;
                k1 = k1 % 4;
            
                double x0 = r[ix].get_x_corner(k0);
                double x1 = r[ix].get_x_corner(k1);
                double y0 = r[ix].get_y_corner(k0);
                double y1 = r[ix].get_y_corner(k1);
            
                double x_length = (x1-x0)/part_size;
                double y_length = (y1-y0)/part_size;
            
                for(double part = 0; part < part_size; part++)
                {
                    double x_cord = x0 + x_length * part;
                    double y_cord = y0 + y_length * part;
                    
                    double x0_ = line.get_x0();
                    double x1_ = line.get_x1();
                    double y0_ = line.get_y0();
                    double y1_ = line.get_y1();
                    
                    double x_length_ = (x1_-x0_)/part_size;
                    double y_length_ = (y1_-y0_)/part_size;
                    
                    for(double part2 = 0; part2 < part_size; part2++)
                    {
                        double x_cord2 = x0_ + x_length_ * part2;
                        double y_cord2 = y0_ + y_length_ * part2;
                        
                        if(x_cord2 > x_cord - intersect_val && x_cord2 < x_cord + intersect_val)
                        {
                            if(y_cord2 > y_cord - intersect_val && y_cord2 < y_cord + intersect_val)
                            {
                                r[ix].set_collision(true);
                                double dx5 = line.get_x1() - line.get_x0();
                                double dy5 = line.get_y1() - line.get_y0();
                                double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                                double angle = 0;
                                if(d5 != 0)
                                {
                                    angle = Math.acos(dx5/d5);
                                }
                                if(dy5 < 0)
                                {
                                    angle *= -1;
                                }
                                
                                
                                if(part2 > part_size/2)
                                {
                                    double right_angle = angle + Math.PI / 2;
                                    int right_x = (int) (line.get_x1() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y1() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle - Math.PI / 2;
                                    int left_x = (int) (line.get_x1() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y1() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - r[ix].center_x();
                                    double right_dy = right_y - r[ix].center_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - r[ix].center_x();
                                    double left_dy = left_y - r[ix].center_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    //angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        
                                        r[ix].set_x0(r[ix].get_x0() + 0.25 * Math.cos(right_angle));
                                        r[ix].set_y0(r[ix].get_y0() + 0.25 * Math.sin(right_angle));
                                        
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        r[ix].set_x0(r[ix].get_x0() + 0.25 * Math.cos(left_angle));
                                        r[ix].set_y0(r[ix].get_y0() + 0.25 * Math.sin(left_angle));
                                        
                                        
                                    }
                                    
                                   
                                }
                                else if(part2 < part_size/2) 
                                {
                                    double right_angle = angle - Math.PI / 2;
                                    int right_x = (int) (line.get_x0() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y0() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle + Math.PI / 2;
                                    int left_x = (int) (line.get_x0() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y0() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - r[ix].center_x();
                                    double right_dy = right_y - r[ix].center_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - r[ix].center_x();
                                    double left_dy = left_y - r[ix].center_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        
                                        r[ix].set_x0(r[ix].get_x0() + 0.25 * Math.cos(right_angle));
                                        r[ix].set_y0(r[ix].get_y0() + 0.25 * Math.sin(right_angle));
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        
                                        r[ix].set_x0(r[ix].get_x0() + 0.25 * Math.cos(left_angle));
                                        r[ix].set_y0(r[ix].get_y0() + 0.25 * Math.sin(left_angle));
                                        
                                    }
                                    
                                }
                                
                                
                            }
                        }
                        
                    }
                    
                    
                }
            }
        }
    }
    
    public void circle_line_resolve()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.red);
        
        
        double dx = line.get_x1()-line.get_x0();
        double dy = line.get_y1()-line.get_y0();
        
        double x_length = dx/part_size;
        double y_length = dy/part_size;
        
        
        for(double part = 0; part < part_size; part++)
        {
            double x = line.get_x0() + x_length * part;
            double y = line.get_y0() + y_length * part;
            
            for(int c0 = 0; c0 < c_size; c0++)
            {
                double dx_ = x - c[c0].get_x();
                double dy_ = y - c[c0].get_y();
                double d_ = Math.sqrt((dx_*dx_)+(dy_*dy_));
                if(d_ < c[c0].get_radius()*1.15)
                {
                    if(gravity)
                    {
                        if(c[c0].get_y() <= y)
                        {
                            c[c0].set_height(c[c0].get_height()*up_h/100);
                            c[c0].set_dir(-1);
                            line.set_height(line.get_height()*low_h/100);
                            line.set_dir(1);
                        }
                        else
                        {
                            c[c0].set_height(c[c0].get_height()*low_h/100);
                            c[c0].set_dir(1);
                            line.set_height(line.get_height()*up_h/100);
                            line.set_dir(-1);
                        }
                        if(c[c0].get_x() <= x)
                        {
                            c[c0].set_dx(c[c0].get_dx()-dx1_val);
                            line.set_dx(line.dx()+dx1_val);
                        }
                        else
                        {
                            c[c0].set_dx(c[c0].get_dx()+dx1_val);
                            line.set_dx(line.dx()-dx1_val);
                        }
                        
                        
                        
                    }
                    
                    
                    double mid_x = line.get_x0() + x_length * part_size / 2;
                    double mid_y = line.get_y0() + y_length * part_size / 2;
                    double d = Math.sqrt((dx * dx) + (dy * dy));
                    double angle = 0;
                    if (d != 0) 
                    {
                        angle = Math.acos(dx / d);
                    }
                    if (dy < 0) 
                    {
                        angle *= -1;
                    }

                    double right_angle = angle - Math.PI / 2;
                    double right_x = mid_x + 30 * Math.cos(right_angle);
                    double right_y = mid_y + 30 * Math.sin(right_angle);
                    

                    double left_angle = angle + Math.PI / 2;
                    double left_x = mid_x + 30 * Math.cos(left_angle);
                    double left_y = mid_y + 30 * Math.sin(left_angle);
                    

                    double right_dx = c[c0].get_x() - right_x;
                    double right_dy = c[c0].get_y() - right_y;
                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                    
                    double left_dx = c[c0].get_x() - left_x;
                    double left_dy = c[c0].get_y() - left_y;
                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                   
                    if(left_d <= right_d)
                    {
                        if(part > part_size/2)
                        {
                            double x3 = line.get_x0();
                            double y3 = line.get_y0();
                            double length = line.length();
                            double new_angle = angle - Math.PI/128;
                            double x3_ = x3 + (length * Math.cos(new_angle));
                            double y3_ = y3 + (length * Math.sin(new_angle));
                            line.set_x1(x3_);
                            line.set_y1(y3_);
                            
                            check_circle_line_rect();
                            
                            c[c0].set_cent_x(c[c0].get_x() + 6.5 * Math.cos(left_angle));
                            c[c0].set_cent_y(c[c0].get_y() + 6.5 * Math.sin(left_angle));
                        }
                        else
                        {
                            double x3 = line.get_x1();
                            double y3 = line.get_y1();
                            double length = line.length();
                            double new_angle = angle + Math.PI + Math.PI/128;
                            double x3_ = x3 + (length * Math.cos(new_angle));
                            double y3_ = y3 + (length * Math.sin(new_angle));
                            line.set_x0(x3_);
                            line.set_y0(y3_);
                            
                            check_circle_line_rect();
                            
                            c[c0].set_cent_x(c[c0].get_x() + 6.5 * Math.cos(left_angle));
                            c[c0].set_cent_y(c[c0].get_y() + 6.5 * Math.sin(left_angle));
                        }
                    }
                    else
                    {
                        if(part > part_size/2)
                        {
                            double x3 = line.get_x0();
                            double y3 = line.get_y0();
                            double length = line.length();
                            double new_angle = angle + Math.PI/128;
                            double x3_ = x3 + (length * Math.cos(new_angle));
                            double y3_ = y3 + (length * Math.sin(new_angle));
                            line.set_x1(x3_);
                            line.set_y1(y3_);
                            
                            check_circle_line_rect();
                            
                            c[c0].set_cent_x(c[c0].get_x() + 6.5  * Math.cos(right_angle));
                            c[c0].set_cent_y(c[c0].get_y() + 6.5   * Math.sin(right_angle));
                        }
                        else
                        {
                            double x3 = line.get_x1();
                            double y3 = line.get_y1();
                            double length = line.length();
                            double new_angle = angle + Math.PI - Math.PI/128;
                            double x3_ = x3 + (length * Math.cos(new_angle));
                            double y3_ = y3 + (length * Math.sin(new_angle));
                            line.set_x0(x3_);
                            line.set_y0(y3_);
                            
                            check_circle_line_rect();
                            
                            c[c0].set_cent_x(c[c0].get_x() + 6.5 * Math.cos(right_angle));
                            c[c0].set_cent_y(c[c0].get_y() + 6.5  * Math.sin(right_angle));
                        }
                    }
                }
            }
        }
        
        
    }
    
    public void rect_circle_resolve()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.red);
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int k = 0; k < 4; k++)
            {
                int k0 = k;
                int k1 = k0 + 1;
                k1 = k1 % 4;
            
                double x0 = r[ix].get_x_corner(k0);
                double x1 = r[ix].get_x_corner(k1);
                double y0 = r[ix].get_y_corner(k0);
                double y1 = r[ix].get_y_corner(k1);
            
                double x_length = (x1-x0)/part_size;
                double y_length = (y1-y0)/part_size;
            
                for(double part = 0; part < part_size; part++)
                {
                    double x_cord = x0 + x_length * part;
                    double y_cord = y0 + y_length * part;
                    
                    for(int ix2 = 0; ix2 < c_size; ix2++)
                    {
                        double x_cord2 = c[ix2].get_x();
                        double y_cord2 = c[ix2].get_y();
                        double dx = x_cord - x_cord2;
                        double dy = y_cord - y_cord2;
                        double d = Math.sqrt((dx*dx)+(dy*dy));
                        if(d < c[ix2].get_radius())
                        {
                            if(gravity)
                            {
                                if(r[ix].center_y() <= c[ix2].get_y())
                                {
                                    r[ix].set_height(r[ix].get_height()*up_h/100);
                                    r[ix].set_dir(-1);
                                    c[ix2].set_height(c[ix2].get_height()*low_h/100);
                                    c[ix2].set_dir(1);
                                }
                                else
                                {
                                    r[ix].set_height(r[ix].get_height()*low_h/100);
                                    r[ix].set_dir(1);
                                    c[ix2].set_height(c[ix2].get_height()*up_h/100);
                                    c[ix2].set_dir(-1);
                                }
                                
                                if(r[ix].center_x() <= c[ix2].get_x())
                                {
                                    double deltax = Math.abs(r[ix].center_x()-c[ix2].get_x());
                                    deltax /= 75;
                                    double r_ix = (r[ix].get_width()/2) / (r[ix].get_width()/2 + c[ix2].get_radius());
                                    double c_ix = (c[ix2].get_radius()) / (r[ix].get_width()/2 + c[ix2].get_radius());
                                    r[ix].set_x0(r[ix].get_x0()-deltax*r_ix);
                                    c[ix2].set_cent_x(c[ix2].get_x()+deltax*c_ix);
                                    
                                }
                                else
                                {
                                    double deltax = Math.abs(r[ix].center_x()-c[ix2].get_x());
                                    deltax /= 75;
                                    double r_ix = (r[ix].get_width()/2) / (r[ix].get_width()/2 + c[ix2].get_radius());
                                    double c_ix = (c[ix2].get_radius()) / (r[ix].get_width()/2 + c[ix2].get_radius());
                                    r[ix].set_x0(r[ix].get_x0()+deltax*r_ix);
                                    c[ix2].set_cent_x(c[ix2].get_x()-deltax*c_ix);
                                }
                                
                            }
                            
                            
                            
                            
                            
                            double a = 0;
                            if(d != 0)
                            {
                                a = Math.acos(dx/d);
                            }
                            if(dy < 0)
                            {
                                a *= -1;
                            }
                            
                                r[ix].set_x0(r[ix].get_x0()+0.5 * Math.cos(a));
                                r[ix].set_y0(r[ix].get_y0()+0.5 * Math.sin(a));
                            
                                c[ix2].set_cent_x(c[ix2].get_x()-0.5*Math.cos(a));
                                c[ix2].set_cent_y(c[ix2].get_y()-0.5*Math.sin(a));
                            
                            
                            
                            if(rect_rotation)
                            {
                                if(part > part_size/2 && is_dragged_circle)
                                {
                                    r[ix].set_angle(r[ix].get_angle()+Math.PI/256);
                                }
                                else if(is_dragged_circle)
                                {
                                    r[ix].set_angle(r[ix].get_angle()-Math.PI/256);
                                }
                            }
                            
                            if(double_rotation && is_dragged_rect)
                            {
                                if(part > part_size/2)
                                {
                                    r[ix].set_angle(r[ix].get_angle()+Math.PI/256);
                                }
                                else
                                {
                                    r[ix].set_angle(r[ix].get_angle()-Math.PI/256);
                                }
                            }
                            
                            if(!is_dragged_rect && !is_dragged_circle && double_rotation)
                            {
                                if(part > part_size/2)
                                {
                                    r[ix].set_angle(r[ix].get_angle()+Math.PI/256);
                                }
                                else
                                {
                                    r[ix].set_angle(r[ix].get_angle()-Math.PI/256);
                                }
                            }
                            
                        }
                        
                    }
                    
                }
            }
        }
     }
    
    public void init_circles()
    {
        Random rand = new Random();
        c = new Circle[c_size];
        for(int ix = 0; ix < c_size; ix++)
        {
            double x = 50 + rand.nextInt(FRAME_WIDTH-100);
            double y = 50 + rand.nextInt(FRAME_WIDTH-100);
            double rad = 20 + (ix)*7;
            c[ix] = new Circle(x,y,rad);
        }
    }
    
    public void init_line()
    {
        Random rand = new Random();
        int x0 = 50;
        int y0 = 120;
        int x1 = 350;
        int y1 = 150;
        line = new Line(x0,y0,x1,y1);
    }
    
  
    
    
    public void init_rects()
    {
        r = new Rect[n_rects];
        Random rand = new Random();
        for(int ix = 0; ix < n_rects; ix++)
        {
            double x = rand.nextInt(FRAME_WIDTH);
            double y = rand.nextInt(FRAME_WIDTH);
            
            double diff = rect_differ * rect_w/2;
            double val = -diff + (rand.nextInt((int)(diff*2 + 1)));
            
            double w = rect_w + val;
            double h = rect_w + val;
            
            double angle = ((double)(rand.nextInt(629))/100.0);
            r[ix] = new Rect(x,y,w,h,angle);
        }
        
    }
    
    public void check_array_col()
    {
        for(int z = 0; z < n_rects; z++)
        {
            r[z].set_collision(false);
        }
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int ix2 = 0; ix2 < n_rects; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = r[ix].center_x() - r[ix2].center_x();
                    double dy = r[ix].center_y() - r[ix2].center_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    if(d < (r[ix].get_width() + r[ix2].get_width())*1.5)
                    {
                        if(ix < ix2)
                        {
                            check_collision(ix,ix2);
                        }
                        else
                        {
                            check_collision(ix2,ix);
                        }
                    }
                }
            }
        }
    }
    
    
    
    public void check_collision(int ix, int ix2)
    {
        for(int k = 0; k < 4; k++)
        {
            int k0 = k;
            int k1 = k0 + 1;
            k1 = k1 % 4;
            
            double x0 = r[ix].get_x_corner(k0);
            double x1 = r[ix].get_x_corner(k1);
            double y0 = r[ix].get_y_corner(k0);
            double y1 = r[ix].get_y_corner(k1);
            
            double x_length = (x1-x0)/part_size;
            double y_length = (y1-y0)/part_size;
            
            for(double part = 0; part < part_size; part++)
            {
                double x_cord = x0 + x_length * part;
                double y_cord = y0 + y_length * part;
                
                for(int b = 0; b < 4; b++)
                {
                    int b0 = b;
                    int b1 = b0 + 1;
                    b1 = b1 % 4;
                    
                    double x0_ = r[ix2].get_x_corner(b0);
                    double x1_ = r[ix2].get_x_corner(b1);
                    double y0_ = r[ix2].get_y_corner(b0);
                    double y1_ = r[ix2].get_y_corner(b1);
                    
                    double x_length_ = (x1_ - x0_)/part_size;
                    double y_length_ = (y1_ - y0_)/part_size;
                    
                    for(double part2 = 0; part2 < part_size; part2++)
                    {
                        double x2_cord = x0_ + x_length_ * part2;
                        double y2_cord = y0_ + y_length_ * part2;
                        
                        if(x2_cord > x_cord - intersect_val && x2_cord < x_cord + intersect_val)
                        {
                            if(y2_cord > y_cord - intersect_val && y2_cord < y_cord + intersect_val)
                            {
                                if(gravity)
                                {
                                    if(r[ix].center_y() <= r[ix2].center_y())
                                    {
                                        r[ix].set_height(r[ix].get_height()*up_h/100);
                                        r[ix].set_dir(-1);
                                        r[ix2].set_height(r[ix2].get_height()*low_h/100);
                                        r[ix2].set_dir(1);
                                    }
                                    else
                                    {
                                        r[ix2].set_height(r[ix2].get_height()*low_h/100);
                                        r[ix2].set_dir(-1);
                                        r[ix].set_height(r[ix].get_height()*up_h/100);
                                        r[ix].set_dir(1);
                                    }
                                    
                                    if(r[ix].center_x() <= r[ix2].center_x())
                                    {
                                        double dx = Math.abs(r[ix].center_x()-r[ix2].center_x());
                                        dx /= dx0_val;
                                        r[ix].set_dx(-dx);
                                        r[ix2].set_dx(dx);
                                        
                                    }
                                    else
                                    {
                                        double dx = Math.abs(r[ix].center_x()-r[ix2].center_x());
                                        dx /= dx0_val;
                                        r[ix].set_dx(dx);
                                        r[ix2].set_dx(-dx);
                                    }
                                    
                                    
                                }
                                
                                r[ix].set_collision(true);
                                r[ix2].set_collision(true);
                                double dx = r[ix].center_x() - r[ix2].center_x();
                                double dy = r[ix].center_y() - r[ix2].center_y();
                                double d = Math.sqrt((dx*dx)+(dy*dy));
                                double a = 0;
                                if(d != 0)
                                {
                                    a = Math.acos(dx/d);
                                }
                                if(dy < 0)
                                {
                                    a *= -1;
                                }
                                
                                int count = 0;
                                for(int s0 = 0; s0 < 4; s0++)
                                {
                                    for(int s1 = 0; s1 < 4; s1++)
                                    {
                                        double s_dx = r[ix].get_x_corner(s0) - r[ix2].get_x_corner(s1);
                                        double s_dy = r[ix].get_y_corner(s0) - r[ix2].get_y_corner(s1);
                                        double s_d = Math.sqrt((s_dx*s_dx)+(s_dy*s_dy));
                                        if(s_d < 10)
                                        {
                                            count++;
                                        }
                                    }
                                }
                                
                                
                                    if (dragged_rect == ix) 
                                    {
                                        //r2 is moved
                                        r[ix2].set_x0(r[ix2].get_x0() - 0.05 * Math.cos(a));
                                        r[ix2].set_y0(r[ix2].get_y0() - 0.05 * Math.sin(a));
                                        
                                        
                                        if (rect_rotation) 
                                        {
                                            if (count >= 2)
                                            {

                                            } 
                                            else if (part2 > part_size/2) 
                                            {
                                                r[ix2].set_angle(r[ix2].get_angle() + Math.PI / 1500);
                                                if(double_rotation)
                                                r[ix].set_angle(r[ix].get_angle() - Math.PI/1500);  //added
                                            } 
                                            else if (part2 < part_size/2)
                                            {
                                                r[ix2].set_angle(r[ix2].get_angle() - Math.PI /1500);
                                                if(double_rotation)
                                                r[ix].set_angle(r[ix].get_angle() + Math.PI/1500);  //added
                                            }
                                            
                                            
                                            
                                        }
                                        
                                    } 
                                    else if (dragged_rect == ix2) 
                                    {
                                        //r2 is moved
                                        r[ix].set_x0(r[ix].get_x0() + 0.05 * Math.cos(a));
                                        r[ix].set_y0(r[ix].get_y0() + 0.05 * Math.sin(a));
                                        
                                        
                                        
                                        if (rect_rotation) 
                                        {
                                            if (count >= 2) 
                                            {

                                            } 
                                            else if (part > part_size/2) 
                                            {
                                                r[ix].set_angle(r[ix].get_angle() + Math.PI /1500);
                                                if(double_rotation)
                                                r[ix2].set_angle(r[ix2].get_angle() - Math.PI/1500);
                                            } 
                                            else if (part < part_size/2) 
                                            {
                                                r[ix].set_angle(r[ix].get_angle() - Math.PI / 1500);
                                                if(double_rotation)
                                                r[ix2].set_angle(r[ix2].get_angle() + Math.PI/1500);
                                            }
                                        }
                                        
                                    } 
                                    else if (dragged_rect != ix && dragged_rect != ix2) 
                                    {
                                        r[ix].set_x0(r[ix].get_x0() + 0.025 * Math.cos(a));
                                        r[ix].set_y0(r[ix].get_y0() + 0.025 * Math.sin(a));
                                        r[ix2].set_x0(r[ix2].get_x0() - 0.025 * Math.cos(a));
                                        r[ix2].set_y0(r[ix2].get_y0() - 0.025 * Math.sin(a));
                                        
                                        if(rect_rotation) 
                                        {
                                            if (count >= 2) 
                                            {

                                            } 
                                            else if (part > part_size/2) 
                                            {
                                                r[ix].set_angle(r[ix].get_angle() + Math.PI / 2000);
                                            } 
                                            else if (part < part_size/2) 
                                            {
                                                r[ix].set_angle(r[ix].get_angle() - Math.PI / 2000);
                                            }
                                            if (part2 > part_size/2) 
                                            {
                                                r[ix2].set_angle(r[ix2].get_angle() + Math.PI / 2000);
                                            } 
                                            else if (part < part_size/2) 
                                            {
                                                r[ix2].set_angle(r[ix2].get_angle() - Math.PI / 2000);
                                            }
                                        }
                                        
                                        
                                    }
                            }
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
    public void init()
    {
        FRAME_WIDTH = this.jPanel1.getWidth();
        CENTER = FRAME_WIDTH/2;
    }
    
    public void act_perf(int delay)
    {
        ActionListener taskPerformer = new ActionListener()
                {
                    
                    public void actionPerformed(ActionEvent evt)
                    {
                        update();
                        draw();
                        
                        
                    }
                };
        
        
        
        t1 = new Timer(delay,taskPerformer);
        t1.start();
    }
    
    public void draw_corner_points()
    {
        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.green);
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int z = 0; z < 4; z++)
            {
                int x0 = (int)r[ix].get_x_corner(z);
                int y0 = (int)r[ix].get_y_corner(z);
                g.fillOval(x0-3,y0-3,7,7);
            }
        }
    }
    
    public void eval_corner_points(int ix)
    {
       
        
        
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.blue);

        double angle = r[ix].get_angle() + Math.PI / 4;
        double w = r[ix].get_width();
        double h = r[ix].get_width();
        double radius = Math.sqrt((Math.pow(w/2, 2)) + (Math.pow(h/2, 2)));

        double[] x_vals = new double[4];
        double[] y_vals = new double[4];

        
        int count = 0;

        x_vals[count] = r[ix].center_x() + radius * Math.cos(angle);
        y_vals[count] = r[ix].center_y() + radius * Math.sin(angle);
        angle += Math.PI / 2;
        count++;

        x_vals[count] = r[ix].center_x() + radius * Math.cos(angle);
        y_vals[count] = r[ix].center_y() + radius * Math.sin(angle);
        angle += Math.PI / 2;
        count++;
        
        x_vals[count] = r[ix].center_x() + radius * Math.cos(angle);
        y_vals[count] = r[ix].center_y() + radius * Math.sin(angle);
        angle += Math.PI / 2;
        count++;

        x_vals[count] = r[ix].center_x() + radius * Math.cos(angle);
        y_vals[count] = r[ix].center_y() + radius * Math.sin(angle);
        count++;

        r[ix].set_x_corners(x_vals);
        r[ix].set_y_corners(y_vals);
        
    }
    
    
    
    public void circle_circle_collision()
    {
        for(int ix = 0; ix < c_size; ix++)
        {
            for(int ix2 = 0; ix2 < c_size; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = c[ix].get_x() - c[ix2].get_x();
                    double dy = c[ix].get_y() - c[ix2].get_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    if(d < c[ix].get_radius() + c[ix2].get_radius())
                    {
                        if(gravity)
                        {
                            if(c[ix].get_y() <= c[ix2].get_y())
                            {
                                c[ix].set_height(c[ix].get_height()*up_h/100);
                                c[ix].set_dir(-1);
                                c[ix2].set_height(c[ix2].get_height()*low_h/100);
                                c[ix2].set_dir(1);
                            }
                            else
                            {
                                c[ix].set_height(c[ix].get_height()*low_h/100);
                                c[ix].set_dir(1);
                                c[ix2].set_height(c[ix2].get_height()*up_h/100);
                                c[ix2].set_dir(-1);
                            }
                            
                            double deltax = Math.abs(c[ix].get_x()-c[ix2].get_x());
                            deltax /= dx0_val;
                            double ix_ratio = c[ix].get_radius()/(c[ix].get_radius()+c[ix2].get_radius());
                            double ix2_ratio = c[ix2].get_radius()/(c[ix].get_radius()+c[ix2].get_radius());
                            
                            if(c[ix].get_x() <= c[ix2].get_x())
                            {
                                c[ix].set_dx(c[ix].get_dx()-deltax*ix_ratio);
                                c[ix2].set_dx(c[ix2].get_dx()+deltax*ix2_ratio);
                            }
                            else
                            {
                                c[ix].set_dx(c[ix].get_dx()+deltax*ix_ratio);
                                c[ix2].set_dx(c[ix2].get_dx()-deltax*ix2_ratio);
                            }
                            
                        }
                        
                        
                        
                        double offset = (c[ix].get_radius() + c[ix2].get_radius()) - d;
                        double a = 0;
                        if(d != 0)
                        {
                            a = Math.acos(dx/d);
                        }
                        if(dy < 0)
                        {
                            a *= -1;
                        }
                        double x_off = offset * Math.cos(a);
                        double y_off = offset * Math.sin(a);
                        double ix_ratio = c[ix].get_radius()/(c[ix].get_radius()+c[ix2].get_radius());
                        double ix2_ratio = c[ix2].get_radius()/(c[ix].get_radius()+c[ix2].get_radius());
                        
                        c[ix].set_cent_x(c[ix].get_x()+x_off*ix_ratio);
                        c[ix].set_cent_y(c[ix].get_y()+y_off*ix_ratio);
                        c[ix2].set_cent_x(c[ix2].get_x()-x_off*ix2_ratio);
                        c[ix2].set_cent_y(c[ix2].get_y()-y_off*ix2_ratio);
                        
                    }
                }
            }
        }
    }
    
    
    public void resolve_rect_line()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.red);
        
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int k = 0; k < 4; k++)
            {
                int k0 = k;
                int k1 = k0 + 1;
                k1 = k1 % 4;
            
                double x0 = r[ix].get_x_corner(k0);
                double x1 = r[ix].get_x_corner(k1);
                double y0 = r[ix].get_y_corner(k0);
                double y1 = r[ix].get_y_corner(k1);
            
                double x_length = (x1-x0)/part_size;
                double y_length = (y1-y0)/part_size;
            
                for(double part = 0; part < part_size; part++)
                {
                    double x_cord = x0 + x_length * part;
                    double y_cord = y0 + y_length * part;
                    
                    double x0_ = line.get_x0();
                    double x1_ = line.get_x1();
                    double y0_ = line.get_y0();
                    double y1_ = line.get_y1();
                    
                    double x_length_ = (x1_-x0_)/part_size;
                    double y_length_ = (y1_-y0_)/part_size;
                    
                    for(double part2 = 0; part2 < part_size; part2++)
                    {
                        double x_cord2 = x0_ + x_length_ * part2;
                        double y_cord2 = y0_ + y_length_ * part2;
                        
                        if(x_cord2 > x_cord - intersect_val && x_cord2 < x_cord + intersect_val)
                        {
                            if(y_cord2 > y_cord - intersect_val && y_cord2 < y_cord + intersect_val)
                            {
                                if(gravity)
                                {
                                    if(r[ix].center_y() <= y_cord2)
                                    {
                                        r[ix].set_height(r[ix].get_height()*up_h/100);
                                        r[ix].set_dir(-1);
                                        line.set_height(line.get_height()*low_h/100);
                                        line.set_dir(1);
                                    }
                                    else
                                    {
                                        r[ix].set_height(r[ix].get_height()*low_h/100);
                                        r[ix].set_dir(1);
                                        line.set_height(line.get_height()*up_h/100);
                                        line.set_dir(-1);
                                    }
                                    
                                    if(r[ix].center_x() <= x_cord2)
                                    {
                                        r[ix].set_dx(r[ix].get_dx()-dx1_val);
                                        line.set_dx(line.dx() + dx1_val);
                                    }
                                    else
                                    {
                                        r[ix].set_dx(r[ix].get_dx()+dx1_val);
                                        line.set_dx(line.dx() - dx1_val);
                                    }
                                    
                                }
                                
                                
                                r[ix].set_collision(true);
                                double dx5 = line.get_x1() - line.get_x0();
                                double dy5 = line.get_y1() - line.get_y0();
                                double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                                double angle = 0;
                                if(d5 != 0)
                                {
                                    angle = Math.acos(dx5/d5);
                                }
                                if(dy5 < 0)
                                {
                                    angle *= -1;
                                }
                                
                                
                                
                                if(dragged_line)
                                {
                                    if(part <= part_size/2 && rect_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/64);
                                    }
                                    else if(part > part_size/2 && rect_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/64);
                                    }
                                }
                                
                                if(!dragged_line)
                                {
                                    if(part <= part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/64);
                                    }
                                    else if(part > part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/64);
                                    }
                                }
                                
                                if(is_dragged_rect)
                                {
                                    if(part <= part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/256);
                                    }
                                    else if(part > part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/256);
                                    }
                                }
                                
                                if(is_dragged_tri)
                                {
                                    if(part <= part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/256);
                                    }
                                    else if(part > part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/256);
                                    }
                                }
                                
                                
                                
                                if(part2 > part_size/2)
                                {
                                    double right_angle = angle + Math.PI / 2;
                                    int right_x = (int) (line.get_x1() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y1() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle - Math.PI / 2;
                                    int left_x = (int) (line.get_x1() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y1() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - r[ix].center_x();
                                    double right_dy = right_y - r[ix].center_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - r[ix].center_x();
                                    double left_dy = left_y - r[ix].center_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    //angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        double x3 = line.get_x0();
                                        double y3 = line.get_y0();
                                        double length  = line.length();
                                        double new_angle = angle - Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x1(x3_);
                                        line.set_y1(y3_);
                                        
                                        if(dragged_line)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(right_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(right_angle));
                                        }
                                        else if(is_dragged_tri)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(right_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(right_angle));
                                        }
                                        else
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 0.05 * Math.cos(right_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 0.05 * Math.sin(right_angle));
                                        }
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        double x3 = line.get_x0();
                                        double y3 = line.get_y0();
                                        double length  = line.length();
                                        double new_angle = angle + Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x1(x3_);
                                        line.set_y1(y3_);
                                        
                                        if(dragged_line)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(left_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(left_angle));
                                        }
                                        else if(is_dragged_tri)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(left_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(left_angle));
                                        }
                                        else
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 0.05 * Math.cos(left_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 0.05 * Math.sin(left_angle));
                                        }
                                        
                                        
                                        
                                        
                                    }
                                    
                                   
                                }
                                else if(part2 < part_size/2) 
                                {
                                    double right_angle = angle - Math.PI / 2;
                                    int right_x = (int) (line.get_x0() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y0() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle + Math.PI / 2;
                                    int left_x = (int) (line.get_x0() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y0() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - r[ix].center_x();
                                    double right_dy = right_y - r[ix].center_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - r[ix].center_x();
                                    double left_dy = left_y - r[ix].center_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        double x3 = line.get_x1();
                                        double y3 = line.get_y1();
                                        double length  = line.length();
                                        double new_angle = angle - Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x0(x3_);
                                        line.set_y0(y3_);
                                        
                                        if(dragged_line)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(right_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(right_angle));
                                        }
                                        else if(is_dragged_tri)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(right_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(right_angle));
                                        }
                                        else
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 0.05 * Math.cos(right_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 0.05 * Math.sin(right_angle));
                                        }
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        double x3 = line.get_x1();
                                        double y3 = line.get_y1();
                                        double length  = line.length();
                                        double new_angle = angle + Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x0(x3_);
                                        line.set_y0(y3_);
                                        
                                        if(dragged_line)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(left_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(left_angle));
                                        }
                                        else if(is_dragged_tri)
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 5.0 * Math.cos(left_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 5.0 * Math.sin(left_angle));
                                        }
                                        else
                                        {
                                            r[ix].set_x0(r[ix].get_x0() + 0.05 * Math.cos(left_angle));
                                            r[ix].set_y0(r[ix].get_y0() + 0.05 * Math.sin(left_angle));
                                        }
                                        
                                        
                                        
                                    }
                                    
                                }
                                
                                
                            }
                        }
                        
                    }
                    
                    
                }
            }
        }
       
    }
    
    
    public void line_intersect()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.red);
        
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int k = 0; k < 4; k++)
            {
                int k0 = k;
                int k1 = k0 + 1;
                k1 = k1 % 4;
            
                double x0 = r[ix].get_x_corner(k0);
                double x1 = r[ix].get_x_corner(k1);
                double y0 = r[ix].get_y_corner(k0);
                double y1 = r[ix].get_y_corner(k1);
            
                double x_length = (x1-x0)/part_size;
                double y_length = (y1-y0)/part_size;
            
                for(double part = 0; part < part_size; part++)
                {
                    double x_cord = x0 + x_length * part;
                    double y_cord = y0 + y_length * part;
                    
                    double x0_ = line.get_x0();
                    double x1_ = line.get_x1();
                    double y0_ = line.get_y0();
                    double y1_ = line.get_y1();
                    
                    double x_length_ = (x1_-x0_)/part_size;
                    double y_length_ = (y1_-y0_)/part_size;
                    
                    for(double part2 = 0; part2 < part_size; part2++)
                    {
                        double x_cord2 = x0_ + x_length_ * part2;
                        double y_cord2 = y0_ + y_length_ * part2;
                        
                        if(x_cord2 > x_cord - intersect_val && x_cord2 < x_cord + intersect_val)
                        {
                            if(y_cord2 > y_cord - intersect_val && y_cord2 < y_cord + intersect_val)
                            {
                                
                                if(gravity)
                                {
                                    if(r[ix].center_y() <= y_cord2)
                                    {
                                        r[ix].set_height(r[ix].get_height()*up_h/100);
                                        r[ix].set_dir(-1);
                                        line.set_height(line.get_height()*low_h/100);
                                        line.set_dir(1);
                                    }
                                    else 
                                    {
                                        r[ix].set_height(r[ix].get_height()*low_h/100);
                                        r[ix].set_dir(1);
                                        line.set_height(line.get_height()*up_h/100);
                                        line.set_dir(-1);
                                    }
                                    
                                    if(r[ix].center_x() <= x_cord2)
                                    {
                                        r[ix].set_dx(r[ix].get_dx()-dx1_val);
                                        line.set_dx(line.dx() + dx1_val);
                                    }
                                    else
                                    {
                                        r[ix].set_dx(r[ix].get_dx()+dx1_val);
                                        line.set_dx(line.dx() - dx1_val);
                                    }
                                    
                                }
                                
                                
                                
                                r[ix].set_collision(true);
                                double dx5 = line.get_x1() - line.get_x0();
                                double dy5 = line.get_y1() - line.get_y0();
                                double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                                double angle = 0;
                                if(d5 != 0)
                                {
                                    angle = Math.acos(dx5/d5);
                                }
                                if(dy5 < 0)
                                {
                                    angle *= -1;
                                }
                                
                                
                                if(dragged_line)
                                {
                                    if(part <= part_size/2 && rect_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/64);
                                    }
                                    else if(part > part_size/2 && rect_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/64);
                                    }
                                }
                                
                                if(is_dragged_rect)     
                                {
                                    if(part <= part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/256);       // + 
                                    }
                                    else if(part > part_size/2 && double_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/256);       // -
                                    }
                                }
                                
                                
                                if(part2 > part_size/2)
                                {
                                    double right_angle = angle + Math.PI / 2;
                                    int right_x = (int) (line.get_x0() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y0() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle - Math.PI / 2;
                                    int left_x = (int) (line.get_x0() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y0() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - r[ix].center_x();
                                    double right_dy = right_y - r[ix].center_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - r[ix].center_x();
                                    double left_dy = left_y - r[ix].center_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    //angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        
                                        double x3 = line.get_x0();
                                        double y3 = line.get_y0();
                                        double length  = line.length();
                                        double new_angle = angle - Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x1(x3_);
                                        line.set_y1(y3_);
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        
                                        double x3 = line.get_x0();
                                        double y3 = line.get_y0();
                                        double length  = line.length();
                                        double new_angle = angle + Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x1(x3_);
                                        line.set_y1(y3_);
                                    }
                                    
                                   
                                }
                                else if(part2 < part_size/2) 
                                {
                                    double right_angle = angle - Math.PI / 2;
                                    int right_x = (int) (line.get_x0() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y0() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle + Math.PI / 2;
                                    int left_x = (int) (line.get_x0() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y0() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - r[ix].center_x();
                                    double right_dy = right_y - r[ix].center_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - r[ix].center_x();
                                    double left_dy = left_y - r[ix].center_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        double x3 = line.get_x1();
                                        double y3 = line.get_y1();
                                        double length  = line.length();
                                        double new_angle = angle - Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x0(x3_);
                                        line.set_y0(y3_);
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        
                                        double x3 = line.get_x1();
                                        double y3 = line.get_y1();
                                        double length  = line.length();
                                        double new_angle = angle + Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x0(x3_);
                                        line.set_y0(y3_);
                                    }
                                    
                                }
                                
                                
                            }
                        }
                        
                    }
                    
                    
                }
            }
        }
        
        
    }
    
    public void line_gravity()
    {
        line.update();
        if(line.get_y0() > FRAME_WIDTH)
        {
            line.set_height(line.get_height() * up_h/100);
            line.set_dir(-1);
            line.set_y0(FRAME_WIDTH);
        }
        if(line.get_y1() > FRAME_WIDTH)
        {
            line.set_height(line.get_height() * up_h/100);
            line.set_dir(-1);
            line.set_y1(FRAME_WIDTH);
        }
    }
    
    public void triangle_gravity()
    {
        for(int ix = 0; ix < t_size; ix++)
        {
            t[ix].update();
            
            for(int z = 0; z < 3; z++)
            {
                if(t[ix].y_point(z) > FRAME_WIDTH)
                {
                    double x_cord = t[ix].x_point(z);
                    double y_cord = t[ix].y_point(z);
                    
                    t[ix].set_height(t[ix].get_height() * 15/100);
                    t[ix].set_dir(-1);
                    
                    double dy = t[ix].y_point(z) - (FRAME_WIDTH);
                    
                    
                    t[ix].set_y(t[ix].get_cent_y()-dy);
                    
                }
            }
            
        }
    }
    
    public void rect_gravity()
    {
        for(int ix = 0; ix < n_rects; ix++)
        {
            r[ix].update();
            for(int z = 0; z < 4; z++)
            {
                if(r[ix].get_y_corner(z) > FRAME_WIDTH)
                {
                    r[ix].set_height(r[ix].get_height()*up_h/100);
                    r[ix].set_dir(-1);
                    double dy = Math.abs(r[ix].get_y_corner(z) - FRAME_WIDTH);
                    r[ix].set_y0(r[ix].get_y0()-dy);
                    
                }
            }
        }
    }
    
    public void circle_gravity()
    {
            for(int ix = 0; ix < c_size; ix++)
            {
                c[ix].update();
                if(c[ix].get_y() + c[ix].get_radius() > FRAME_WIDTH)
                {
                    c[ix].set_height(c[ix].get_height()*up_h/100);
                    c[ix].set_dir(-1);
                    double dy = Math.abs(FRAME_WIDTH - (c[ix].get_y()+c[ix].get_radius()));
                    c[ix].set_cent_y(c[ix].get_y()-dy);
                }
            }
    }
    
    public void init_auto_move()
    {
        Random rand = new Random();
        auto_rect_angle = new double[n_rects];
        auto_rect_speed = new double[n_rects];
        auto_circle_angle = new double[c_size];
        auto_circle_speed  = new double[c_size];
        auto_tri_angle = new double [t_size];
        auto_tri_speed = new double [t_size];
        auto_line_angle = new double[2];
        auto_line_speed = new double[2];
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            auto_rect_angle[ix] = (double)(rand.nextInt(629)/100.0);
            auto_rect_speed[ix] = 0.5 + rand.nextInt(30)/10.0;
        }
        
        for(int ix = 0; ix < c_size; ix++)
        {
            auto_circle_angle[ix] = (double)(rand.nextInt(629))/100.0;
            auto_circle_speed[ix] = 0.5 + rand.nextInt(30)/10.0;
        }
        
        for(int ix = 0; ix < t_size; ix++)
        {
            auto_tri_angle[ix] = (double)(rand.nextInt(629))/100.0;
            auto_tri_speed[ix] = 0.5 + rand.nextInt(30)/10.0;
        }
        
        for(int ix = 0; ix < 2; ix++)
        {
            auto_line_angle[ix] = (double)(rand.nextInt(629))/100.0;
            auto_line_speed[ix] = 0.5 + rand.nextInt(30)/10.0;
        }
                
        
    }
    
    /*
    public void circle_inside_rect()
    {
        for(int ix = 0; ix < n_rects; ix++)
        {
            for(int ix2 = 0; ix2 < c_size; ix2++)
            {
                double dx = c[ix2].get_x() - r[ix].center_x();
                double dy = c[ix2].get_y() - r[ix].center_y();
                double d = Math.sqrt((dx*dx)+(dy*dy));
                if(d < r[ix].get_width()*0.5)
                {
                    Random rand = new Random();
                    double a = (double)(rand.nextInt(629))/100.0;
                    c[ix2].set_cent_x(c[ix2].get_x() + r[ix].get_width() * Math.cos(a));
                    c[ix2].set_cent_y(c[ix2].get_y() + r[ix].get_width() * Math.sin(a));
                }
            }
        }
    }
    */
    
    public void y_line_reflect(int ix)
    {
        auto_line_angle[ix] = -auto_line_angle[ix];
    }
    
    public void x_line_reflect(int ix)
    {
        auto_line_angle[ix] = Math.PI - auto_line_angle[ix];
    }
    
    public void x_tri_reflect(int ix)
    {
        auto_tri_angle[ix] = Math.PI - auto_tri_angle[ix];
    }
    
    public void y_tri_reflect(int ix)
    {
        auto_tri_angle[ix] = -auto_tri_angle[ix];
    }
    
    public void y_circle_reflect(int ix)
    {
        auto_circle_angle[ix] = -auto_circle_angle[ix];
    }
    
    public void x_circle_reflect(int ix)
    {
        auto_circle_angle[ix] = Math.PI - auto_circle_angle[ix];
    }
   
    public void y_rect_reflect(int ix)
    {
        auto_rect_angle[ix] = -auto_rect_angle[ix];        
    }
    
    public void x_rect_reflect(int ix)
    {
        auto_rect_angle[ix] = Math.PI - auto_rect_angle[ix];
    }
    
    public void update_line_move()
    {
        if(is_line && auto_move)
        {
                Random rand = new Random();
                int x = rand.nextInt(20);
                if(x <= 2)
                {
                    double a = 0;
                    a = (double)(-40 + rand.nextInt(81))/100.0;
                    auto_line_angle[0] = auto_line_angle[0] + a;
                }
                x = rand.nextInt(20);
                if(x <= 2)
                {
                    double a = 0;
                    a = (double)(-40 + rand.nextInt(81))/100.0;
                    auto_line_angle[1] = auto_line_angle[1] + a;
                }
                
                if(line.get_x0() - 10 < 0)
                {
                    x_line_reflect(0);
                }
                if(line.get_x0() + 10 > FRAME_WIDTH)
                {
                    x_line_reflect(0);
                }
                if(line.get_y0() - 10 < 0)
                {
                    y_line_reflect(0);
                }
                if(line.get_y0() + 10 > FRAME_WIDTH)
                {
                    y_line_reflect(0);
                }
                
                if(line.get_x1() - 10 < 0)
                {
                    x_line_reflect(1);
                }
                if(line.get_x1() + 10 > FRAME_WIDTH)
                {
                    x_line_reflect(1);
                }
                if(line.get_y1() - 10 < 0)
                {
                    y_line_reflect(1);
                }
                if(line.get_y1() +10 > FRAME_WIDTH)
                {
                    y_line_reflect(1);
                }
                
                line.set_x0(line.get_x0() + auto_line_speed[0] * Math.cos(auto_line_angle[0]));
                line.set_y0(line.get_y0() + auto_line_speed[0] * Math.sin(auto_line_angle[0]));
                line.set_x1(line.get_x1() + auto_line_speed[1] * Math.cos(auto_line_angle[1]));
                line.set_y1(line.get_y1() + auto_line_speed[1] * Math.sin(auto_line_angle[1]));
                
        }
    }
    
    public void update_tri_move()
    {
        if(t_size > 0 && auto_move)
        {
            for(int ix = 0; ix < t_size; ix++)
            {
                Random rand = new Random();
                int x = rand.nextInt(20);
                if(x <= 2)
                {
                    double a = 0;
                    a = (double)(-40 + rand.nextInt(81))/100.0;
                    auto_tri_angle[ix] = auto_tri_angle[ix] + a;
                }
                
                if(t[ix].get_cent_x()+t[ix].get_radius() > FRAME_WIDTH)
                {
                    x_tri_reflect(ix);
                }
                if(t[ix].get_cent_x()-t[ix].get_radius() < 0)
                {
                    x_tri_reflect(ix);
                }
                if(t[ix].get_cent_y()+t[ix].get_radius() > FRAME_WIDTH)
                {
                    y_tri_reflect(ix);
                }
                if(t[ix].get_cent_y()-t[ix].get_radius() < 0)
                {
                    y_tri_reflect(ix);
                }
                
                t[ix].set_x(t[ix].get_cent_x()+auto_tri_speed[ix]*Math.cos(auto_tri_angle[ix]));
                t[ix].set_y(t[ix].get_cent_y()+auto_tri_speed[ix]*Math.sin(auto_tri_angle[ix]));            }
        }
    }
    
    public void update_circle_move()
    {
        if(c_size > 0 && auto_move)
        {
            for(int ix = 0; ix < c_size; ix++)
            {
                Random rand = new Random();
                int x = rand.nextInt(20);
                if(x <= 2)
                {
                    double a = 0;
                    a = (double)(-40 + rand.nextInt(81))/100.0;
                    auto_circle_angle[ix] = auto_circle_angle[ix] + a;
                }
            
                if(c[ix].get_x()+c[ix].get_radius() > FRAME_WIDTH)
                {
                    x_circle_reflect(ix);
                }
                if(c[ix].get_x()-c[ix].get_radius() < 0)
                {
                    x_circle_reflect(ix);
                }
                if(c[ix].get_y()+c[ix].get_radius() > FRAME_WIDTH)
                {
                    y_circle_reflect(ix);
                }
                if(c[ix].get_y()-c[ix].get_radius() < 0)
                {
                    y_circle_reflect(ix);
                }
                
                c[ix].set_cent_x(c[ix].get_x() + auto_circle_speed[ix] * Math.cos(auto_circle_angle[ix]));
                c[ix].set_cent_y(c[ix].get_y() + auto_circle_speed[ix] * Math.sin(auto_circle_angle[ix]));
                
            }
            
        }
    }
    
    public void update_rect_move()
    {
        if(n_rects > 0 && auto_move)
        {
            for(int ix = 0; ix < n_rects; ix++)
            {
                Random rand = new Random();
                int x = rand.nextInt(20);
                if(x <= 2)
                {
                    double a = 0;
                    a = (double)(-40 + rand.nextInt(81))/100.0;
                    auto_rect_angle[ix] = auto_rect_angle[ix] + a;
                }
            
                
                if (r[ix].get_x1() > FRAME_WIDTH - r[ix].get_width() / 2) {
                    x_rect_reflect(ix);
                    //r[0].set_x0(FRAME_WIDTH - r[0].get_width());
                } if (r[ix].get_x0() < r[ix].get_width() / 2) {
                    x_rect_reflect(ix);
                    //r[0].set_x0(0);
                } if (r[ix].get_y1() > FRAME_WIDTH - r[ix].get_width() / 2) {
                    y_rect_reflect(ix);
                    //r[0].set_y0(FRAME_WIDTH - r[0].get_height());
                } if (r[ix].get_y0() < r[ix].get_width() / 2) {
                    y_rect_reflect(ix);
                    //r[0].set_y0(0);
                }
                
                r[ix].set_x0(r[ix].get_x0() + auto_rect_speed[ix] * Math.cos(auto_rect_angle[ix]));
                r[ix].set_y0(r[ix].get_y0() + auto_rect_speed[ix] * Math.sin(auto_rect_angle[ix]));
            }
            
        }
    }
    
    public void update()
    {        
        if(auto_move)
        {
            update_rect_move();
            update_circle_move();
            update_tri_move();
            update_line_move();
        }
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            eval_corner_points(ix);  
        }
        check_array_col();
        out_frame_collision();
        
        if(is_line)
        {
            if(!dragged_line)
            {
                //line_intersect();
            }
            resolve_rect_line();
        }
        if(is_circle)
        {
            //circle_inside_rect(); //resolve circle stuck in rect
            rect_circle_resolve();     
            circle_circle_collision(); 
            if(is_line)
            circle_line_resolve();     
        }
        if(is_tri)
        {
            check_rect_tri();
            check_tri_tri();    
            if(is_circle)
            check_tri_circle(); //check
            if(is_line)
            check_tri_line();
        }
        
        
        
        if(gravity && !is_dragged_rect && !is_dragged_circle && !is_dragged_tri && !dragged_line)
        {
            rect_gravity();
            circle_gravity();
            triangle_gravity();
            line_gravity();
        }
        
        
    }
    
    
    public void tri_out_of_frame()
    {
        for(int ix = 0; ix < t_size; ix++)
        {
            if(t[ix].get_cent_x() - t[ix].get_radius() < 0)
            {
                t[ix].set_x(t[ix].get_radius());
            }
            if(t[ix].get_cent_x() + t[ix].get_radius() > FRAME_WIDTH)
            {
                t[ix].set_x(FRAME_WIDTH - t[ix].get_radius());
            }
            if(t[ix].get_cent_y() - t[ix].get_radius() < 0)
            {
                t[ix].set_y(t[ix].get_radius());
            }
            if(t[ix].get_cent_y() + t[ix].get_radius() > FRAME_WIDTH)
            {
                t[ix].set_y(FRAME_WIDTH - t[ix].get_radius());
            }
        }
    }
    
    public void check_tri_line()
    {
        for(int ix = 0; ix < t_size; ix++)
        {
            tri_line_resolve(ix);
        }
    }
    
    
    public void tri_line_resolve(int t_ix)
    {
        
        t[t_ix].set_collision(false);
        
        for(int k = 0; k < 3; k++)
        {
            int k0 = k;
            int k1 = (k0 + 1) % 3;
        
            double x0 = t[t_ix].x_point(k0);
            double x1 = t[t_ix].x_point(k1);
            double y0 = t[t_ix].y_point(k0);
            double y1 = t[t_ix].y_point(k1);
            
            double x_length = (x1-x0)/part_size;
            double y_length = (y1-y0)/part_size;
            
            for(double part = 0; part < part_size; part++)
            {
                double x_cord = x0 + x_length * part;
                double y_cord = y0 + y_length * part;
                
                double x0_ = line.get_x0();
                double y0_ = line.get_y0();
                double x1_ = line.get_x1();
                double y1_ = line.get_y1();
                
                double x_length_ = (x1_-x0_)/part_size;
                double y_length_ = (y1_-y0_)/part_size;
                
                for(double part2 = 0; part2 < part_size; part2++)
                {
                    double x2_cord = x0_ + x_length_ * part2;
                    double y2_cord = y0_ + y_length_ * part2;
                    
                    if(x2_cord > x_cord - intersect_val && x2_cord < x_cord + intersect_val)
                    {
                        if(y2_cord > y_cord - intersect_val && y2_cord < y_cord + intersect_val)
                        {
                            
                            if(gravity)
                                {
                                    if(t[t_ix].get_cent_y() <= y2_cord)
                                    {
                                        t[t_ix].set_height(t[t_ix].get_height()*up_h/100);
                                        t[t_ix].set_dir(-1);
                                        line.set_height(line.get_height()*low_h/100);
                                        line.set_dir(1);
                                    }
                                    else
                                    {
                                        t[t_ix].set_height(t[t_ix].get_height()*low_h/100);
                                        t[t_ix].set_dir(1);
                                        line.set_height(line.get_height()*up_h/100);
                                        line.set_dir(-1);
                                    }
                                    
                                    if(t[t_ix].get_cent_x() <= x2_cord)
                                    {
                                        t[t_ix].set_dx(t[t_ix].get_dx()-dx1_val);
                                        line.set_dx(line.dx() + dx1_val);
                                    }
                                    else
                                    {
                                        t[t_ix].set_dx(t[t_ix].get_dx()+dx1_val);
                                        line.set_dx(line.dx() - dx1_val);
                                    }
                                    
                                }
                            
                            
                                t[t_ix].set_collision(true);
                                double dx5 = line.get_x1() - line.get_x0();
                                double dy5 = line.get_y1() - line.get_y0();
                                double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                                double angle = 0;
                                if(d5 != 0)
                                {
                                    angle = Math.acos(dx5/d5);
                                }
                                if(dy5 < 0)
                                {
                                    angle *= -1;
                                }
                                
                                if(is_dragged_tri)
                                {
                                    if(part <= part_size/2 && double_rotation)
                                    {
                                        t[t_ix].set_angle(t[t_ix].get_angle() - Math.PI/256);
                                    }
                                    else if(part > part_size/2 && double_rotation)
                                    {
                                        t[t_ix].set_angle(t[t_ix].get_angle() + Math.PI/256);
                                    }
                                }
                                if(dragged_line)
                                {
                                    if(part <= part_size/2 && rect_rotation)
                                    {
                                        t[t_ix].set_angle(t[t_ix].get_angle() - Math.PI/128);
                                    }
                                    else if(part > part_size/2 && rect_rotation)
                                    {
                                        t[t_ix].set_angle(t[t_ix].get_angle() + Math.PI/128);
                                    }
                                }
                                
                                if(!dragged_line)
                                {
                                    if(part <= part_size/2 && double_rotation)
                                    {
                                        t[t_ix].set_angle(t[t_ix].get_angle() - Math.PI/128);
                                    }
                                    else if(part > part_size/2 && double_rotation)
                                    {
                                        t[t_ix].set_angle(t[t_ix].get_angle() + Math.PI/128);
                                    }
                                }
                                   
                                
                                
                                
                                if(part2 >= part_size/2)
                                {
                                    double right_angle = angle + Math.PI / 2;
                                    int right_x = (int) (line.get_x1() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y1() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle - Math.PI / 2;
                                    int left_x = (int) (line.get_x1() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y1() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - t[t_ix].get_cent_x();
                                    double right_dy = right_y - t[t_ix].get_cent_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - t[t_ix].get_cent_x();
                                    double left_dy = left_y - t[t_ix].get_cent_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    //angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        double x3 = line.get_x0();
                                        double y3 = line.get_y0();
                                        double length  = line.length();
                                        double new_angle = angle - Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x1(x3_);
                                        line.set_y1(y3_);
                                        
                                        if(!is_dragged_tri)
                                        {
                                            t[t_ix].set_x(t[t_ix].get_cent_x() + 2 * Math.cos(right_angle));
                                            t[t_ix].set_y(t[t_ix].get_cent_y() + 2 * Math.sin(right_angle));
                                        }
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        double x3 = line.get_x0();
                                        double y3 = line.get_y0();
                                        double length  = line.length();
                                        double new_angle = angle + Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x1(x3_);
                                        line.set_y1(y3_);
                                        
                                        if(!is_dragged_tri)
                                        {
                                            t[t_ix].set_x(t[t_ix].get_cent_x() + 2 * Math.cos(left_angle));
                                            t[t_ix].set_y(t[t_ix].get_cent_y() + 2 * Math.sin(left_angle));
                                        }
                                        
                                        
                                    }
                                    
                                   
                                }
                                else if(part2 < part_size/2) 
                                {
                                    double right_angle = angle - Math.PI / 2;
                                    int right_x = (int) (line.get_x0() + 30 * Math.cos(right_angle));
                                    int right_y = (int) (line.get_y0() + 30 * Math.sin(right_angle));
                                    
                                    double left_angle = angle + Math.PI / 2;
                                    int left_x = (int) (line.get_x0() + 30 * Math.cos(left_angle));
                                    int left_y = (int) (line.get_y0() + 30 * Math.sin(left_angle));
                                    
                                    
                                    double right_dx = right_x - t[t_ix].get_cent_x();
                                    double right_dy = right_y - t[t_ix].get_cent_y();
                                    double right_d = Math.sqrt((right_dx*right_dx)+(right_dy*right_dy));
                                    
                                    double left_dx = left_x - t[t_ix].get_cent_x();
                                    double left_dy = left_y - t[t_ix].get_cent_y();
                                    double left_d = Math.sqrt((left_dx*left_dx)+(left_dy*left_dy));
                                    
                                    angle += Math.PI;
                                    
                                    if(right_d <= left_d)
                                    {
                                        double x3 = line.get_x1();
                                        double y3 = line.get_y1();
                                        double length  = line.length();
                                        double new_angle = angle - Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x0(x3_);
                                        line.set_y0(y3_);
                                        
                                        if(!is_dragged_tri)
                                        {
                                            t[t_ix].set_x(t[t_ix].get_cent_x() + 2 * Math.cos(right_angle));
                                            t[t_ix].set_y(t[t_ix].get_cent_y() + 2 * Math.sin(right_angle));
                                        }
                                        
                                    }
                                    else if(left_d < right_d)
                                    {
                                        double x3 = line.get_x1();
                                        double y3 = line.get_y1();
                                        double length  = line.length();
                                        double new_angle = angle + Math.PI/1024;
                                        double x3_ = x3 + (length * Math.cos(new_angle));
                                        double y3_ = y3 + (length * Math.sin(new_angle));
                                        line.set_x0(x3_);
                                        line.set_y0(y3_);
                                        
                                        if(!is_dragged_tri)
                                        {
                                            t[t_ix].set_x(t[t_ix].get_cent_x() + 2 * Math.cos(left_angle));
                                            t[t_ix].set_y(t[t_ix].get_cent_y() + 2 * Math.sin(left_angle));
                                        }
                                        
                                        
                                        
                                    }
                                    
                                }
                            
                        }
                    }
                }
                
            }
        }
    }
    
    
    
    public void check_tri_circle()
    {
        for(int x = 0; x < t_size; x++)
        {
            for(int y = 0; y < c_size; y++)
            {
                tri_circle_resolve(x,y);
            }
        }
    }
    
    
    public void tri_circle_resolve(int t_ix, int c_ix)
    {
        for(int k = 0; k < 3; k++)
        {
            int k0 = k;
            int k1 = (k0 + 1) % 3;
            
            double x0 = t[t_ix].x_point(k0);
            double x1 = t[t_ix].x_point(k1);
            double y0 = t[t_ix].y_point(k0);
            double y1 = t[t_ix].y_point(k1);
            
            double x_length = (x1-x0)/part_size;
            double y_length = (y1-y0)/part_size;
            
            for(double part = 0; part < part_size; part++)
            {
                double x_cord = x0 + x_length * part;
                double y_cord = y0 + y_length * part;
                
                double dx = c[c_ix].get_x() - x_cord;
                double dy = c[c_ix].get_y() - y_cord;
                double d = Math.sqrt((dx*dx)+(dy*dy));
                
                if(d < c[c_ix].get_radius())
                {
                    if(gravity)
                    {
                        if(t[t_ix].get_cent_y() <= c[c_ix].get_y())
                        {
                            t[t_ix].set_height(t[t_ix].get_height()*up_h/100);
                            t[t_ix].set_dir(-1);
                            c[c_ix].set_height(c[c_ix].get_height()*low_h/100);
                            c[c_ix].set_dir(1);
                        }
                        else
                        {
                            t[t_ix].set_height(t[t_ix].get_height()*low_h/100);
                            t[t_ix].set_dir(1);
                            c[c_ix].set_height(c[c_ix].get_height()*up_h/100);
                            c[c_ix].set_dir(-1);
                        }
                        
                        double deltax = Math.abs(t[t_ix].get_cent_x()-c[c_ix].get_x());
                        deltax /= dx0_val;
                        double t_ratio = t[t_ix].get_radius() / (t[t_ix].get_radius()+c[c_ix].get_radius());
                        double c_ratio = c[c_ix].get_radius() / (t[t_ix].get_radius()+c[c_ix].get_radius());
                        
                        if(t[t_ix].get_cent_x() <= c[c_ix].get_x())
                        {
                            t[t_ix].set_dx(t[t_ix].get_dx()-deltax*t_ratio);
                            c[c_ix].set_dx(c[c_ix].get_dx()+deltax*c_ratio);
                        }
                        else
                        {
                            t[t_ix].set_dx(t[t_ix].get_dx()+deltax*t_ratio);
                            c[c_ix].set_dx(c[c_ix].get_dx()-deltax*c_ratio);
                        }
                        
                    }
                    
                    
                    t[t_ix].set_collision(true);
                    
                    double dx5 = c[c_ix].get_x() - t[t_ix].get_cent_x();
                    double dy5 = c[c_ix].get_y() - t[t_ix].get_cent_y();
                    double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                    double a = 0;
                    if(d5 != 0)
                    {
                        a = Math.acos(dx5/d5);
                    }
                    if(dy5 < 0)
                    {
                        a *= -1;
                    }
                    
                    if(dragged_circle_index == c_ix)
                    {
                        t[t_ix].set_x(t[t_ix].get_cent_x() - 0.3 * Math.cos(a));
                        t[t_ix].set_y(t[t_ix].get_cent_y() - 0.3 * Math.sin(a));
                    }
                    else if(dragged_t == t_ix)
                    {
                        c[c_ix].set_cent_x(c[c_ix].get_x() + 1.5 * Math.cos(a));
                        c[c_ix].set_cent_y(c[c_ix].get_y() + 1.5 * Math.sin(a));
                    }
                    else
                    {
                        t[t_ix].set_x(t[t_ix].get_cent_x() - 0.5 * Math.cos(a));
                        t[t_ix].set_y(t[t_ix].get_cent_y() - 0.5 * Math.sin(a));
                        c[c_ix].set_cent_x(c[c_ix].get_x() + 1.0 * Math.cos(a));
                        c[c_ix].set_cent_y(c[c_ix].get_y() + 1.0 * Math.sin(a));
                    }
                    
                    if(part <= part_size/2 && is_dragged_tri && double_rotation)
                    {
                        t[t_ix].set_angle(t[t_ix].get_angle()-Math.PI/256);
                    }
                    else if(part > part_size/2 && is_dragged_tri && double_rotation)
                    {
                        t[t_ix].set_angle(t[t_ix].get_angle()+Math.PI/256);
                    }
                    
                    if(part <= part_size/2 && is_dragged_circle && (rect_rotation || double_rotation))
                    {
                        t[t_ix].set_angle(t[t_ix].get_angle()-Math.PI/256);
                    }
                    else if(part > part_size/2 && is_dragged_circle && (rect_rotation || double_rotation))
                    {
                        t[t_ix].set_angle(t[t_ix].get_angle()+Math.PI/256);
                    }
                            
                    if(!is_dragged_circle && !is_dragged_tri && double_rotation)
                    {
                        if(part <= part_size/2)
                        {
                            t[t_ix].set_angle(t[t_ix].get_angle()-Math.PI/256);
                        }
                        else if(part > part_size/2)
                        {
                            t[t_ix].set_angle(t[t_ix].get_angle()+Math.PI/256);
                        }
                    }
                    
                    
                    /*
                    if(part <= part_size/2 && rect_rotation || double_rotation)
                    {
                        t[t_ix].set_angle(t[t_ix].get_angle()-Math.PI/256);
                    }
                    else if(part > part_size/2 && rect_rotation || double_rotation)
                    {
                        t[t_ix].set_angle(t[t_ix].get_angle()+Math.PI/256);
                    }
                    */
                    
                }
                
                
            }
            
            
            
        }
    }
    
    public void check_tri_tri()
    {
        for(int x = 0; x < t_size; x++)
        {
            for(int y = 0; y < t_size; y++)
            {
                if(x != y)
                {
                    tri_tri_resolve(x,y);
                    
                }
                
            }
        }
    }
    
     public void tri_tri_resolve(int ix, int ix2)
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.green);
        
        t[ix].set_collision(false);
        t[ix2].set_collision(false);
        
        for(int k = 0; k < 3; k++)
        {
            int k0 = k;
            int k1 = k0 + 1;
            k1 = k1 % 3;
            
            double x0 = t[ix2].x_point(k0);
            double x1 = t[ix2].x_point(k1);
            double y0 = t[ix2].y_point(k0);
            double y1 = t[ix2].y_point(k1);
            
            double x_length = (x1-x0)/part_size;
            double y_length = (y1-y0)/part_size;
            
            for(double part = 0; part < part_size; part++)
            {
                double x_cord = x0 + x_length * part;
                double y_cord = y0 + y_length * part;
                
                for(int b = 0; b < 3; b++)
                {
                    int b0 = b;
                    int b1 = b0 + 1;
                    b1 = b1 % 3;
                    
                    double x0_ = t[ix].x_point(b0);
                    double x1_ = t[ix].x_point(b1);
                    double y0_ = t[ix].y_point(b0);
                    double y1_ = t[ix].y_point(b1);
                    
                    double x_length_ = (x1_ - x0_)/part_size;
                    double y_length_ = (y1_ - y0_)/part_size;
                    
                    for(double part2 = 0; part2 < part_size; part2++)
                    {
                        double x2_cord = x0_ + x_length_ * part2;
                        double y2_cord = y0_ + y_length_ * part2;
                     
                        
                        if(x2_cord > x_cord - intersect_val && x2_cord < x_cord + intersect_val)
                        {
                            if(y2_cord > y_cord - intersect_val  && y2_cord < y_cord + intersect_val)
                            {
                                t[ix2].set_collision(true);
                                t[ix].set_collision(true);
                                
                                if(gravity)
                                {
                                    if(t[ix].get_cent_y() <= t[ix2].get_cent_y())
                                    {
                                        t[ix].set_height(t[ix].get_height()*up_h/100);
                                        t[ix].set_dir(-1);
                                        t[ix2].set_height(t[ix2].get_height()*low_h/100);
                                        t[ix2].set_dir(1);
                                    }
                                    else
                                    {
                                        t[ix].set_height(t[ix].get_height()*low_h/100);
                                        t[ix].set_dir(1);
                                        t[ix2].set_height(t[ix2].get_height()*up_h/100);
                                        t[ix2].set_dir(-1);
                                    }
                                    
                                    double deltax = Math.abs(t[ix].get_cent_x()-t[ix2].get_cent_x());
                                    deltax /= dx0_val;
                                    
                                    double ix_ratio = t[ix].get_radius() / (t[ix].get_radius()+t[ix2].get_radius());
                                    double ix2_ratio = t[ix2].get_radius() / (t[ix].get_radius()+t[ix2].get_radius());
                                    
                                    if(t[ix].get_cent_x() <= t[ix2].get_cent_x())
                                    {
                                        t[ix].set_dx(t[ix].get_dx()-deltax*ix_ratio);
                                        t[ix2].set_dx(t[ix2].get_dx()+deltax*ix2_ratio);
                                    }
                                    else
                                    {
                                        t[ix].set_dx(t[ix].get_dx()+deltax*ix_ratio);
                                        t[ix2].set_dx(t[ix2].get_dx()-deltax*ix2_ratio);
                                    }
                                    
                                    
                                    
                                }
                                
                                
                                if(dragged_t == ix)// ix)
                                {
                                    double dx = t[ix2].get_cent_x() - t[ix].get_cent_x();
                                    double dy = t[ix2].get_cent_y() - t[ix].get_cent_y();
                                    double d = Math.sqrt((dx*dx)+(dy*dy));
                                    double a = 0;
                                    if(d != 0)
                                    {
                                        a = Math.acos(dx/d);
                                    }
                                    if(dy < 0)
                                    {
                                        a *= -1;
                                    }
                                    
                                    double resolve_val = 0.25;
                                    if(t[ix2].get_radius() > t[ix].get_radius())
                                    {
                                       resolve_val /= 5;
                                    }
                                    
                                    t[ix2].set_x(t[ix2].get_cent_x() + resolve_val * Math.cos(a));
                                    t[ix2].set_y(t[ix2].get_cent_y() + resolve_val * Math.sin(a));
                                    
                                    int count = 0;
                                    for(int m = 0; m < 3; m++)
                                    {
                                        for(int m1 = 0; m1 < 3; m1++)
                                        {
                                            double m_dx = t[ix].x_point(m) - t[ix2].x_point(m1);
                                            double m_dy = t[ix].y_point(m) - t[ix2].y_point(m1);
                                            double m_d = Math.sqrt((m_dx*m_dx)+(m_dy*m_dy));
                                            if(m_d < 20)
                                            {
                                                count++;
                                            }
                                        }
                                    }
                                    
                                    if(count >= 2)
                                    {
                                        
                                    }
                                    else if(part >= part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/512;
                                        if(t[ix2].get_radius() > t[ix].get_radius())
                                        {
                                            angle /= 4;
                                        }
                                        t[ix2].set_angle(t[ix2].get_angle() + angle);
                                        if(double_rotation)
                                        {
                                            t[ix].set_angle(t[ix].get_angle() - Math.PI/512);
                                        }
                                    }
                                    else if(part < part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/512;
                                        if(t[ix2].get_radius() > t[ix].get_radius())
                                        {
                                            angle /= 4;
                                        }
                                        t[ix2].set_angle(t[ix2].get_angle() - angle);
                                        if(double_rotation)
                                        {
                                            t[ix].set_angle(t[ix].get_angle() + Math.PI/512);
                                        }
                                    }
                                    
                                }
                                else if(dragged_t == ix2)
                                {
                                    double dx = t[ix2].get_cent_x() - t[ix].get_cent_x();
                                    double dy = t[ix2].get_cent_y() - t[ix].get_cent_y();
                                    double d = Math.sqrt((dx*dx)+(dy*dy));
                                    double a = 0;
                                    if(d != 0)
                                    {
                                        a = Math.acos(dx/d);
                                    }
                                    if(dy < 0)
                                    {
                                        a *= -1;
                                    }
                                    
                                    double resolve_val = 0.25;
                                    if(t[ix].get_radius() > t[ix2].get_radius())
                                    {
                                       resolve_val /= 5;
                                    }
                                   
                                    t[ix].set_x(t[ix].get_cent_x() - resolve_val * Math.cos(a));
                                    t[ix].set_y(t[ix].get_cent_y() - resolve_val * Math.sin(a));
                                    
                                    int count = 0;
                                    for(int m = 0; m < 3; m++)
                                    {
                                        for(int m1 = 0; m1 < 3; m1++)
                                        {
                                            double m_dx = t[ix].x_point(m) - t[ix2].x_point(m1);
                                            double m_dy = t[ix].y_point(m) - t[ix2].y_point(m1);
                                            double m_d = Math.sqrt((m_dx*m_dx)+(m_dy*m_dy));
                                            if(m_d < 20)
                                            {
                                                count++;
                                            }
                                        }
                                    }
                                    
                                    if(count >= 2)
                                    {
                                        
                                    }
                                    else if(part2 >= part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/512;
                                        if(t[ix].get_radius() > t[ix2].get_radius())
                                        {
                                            angle /= 4;
                                        }
                                        t[ix].set_angle(t[ix].get_angle() + angle);
                                        if(double_rotation)
                                        {
                                            t[ix2].set_angle(t[ix2].get_angle() - Math.PI/512);
                                        }
                                    }
                                    else if(part2 < part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/512;
                                        if(t[ix].get_radius() > t[ix2].get_radius())
                                        {
                                            angle /= 4;
                                        }
                                        t[ix].set_angle(t[ix].get_angle() - angle);
                                        if(double_rotation)
                                        {
                                            t[ix2].set_angle(t[ix2].get_angle() + Math.PI/512);
                                        }
                                    }
                                }
                                else
                                {
                                    double dx = t[ix2].get_cent_x() - t[ix].get_cent_x();
                                    double dy = t[ix2].get_cent_y() - t[ix].get_cent_y();
                                    double d = Math.sqrt((dx*dx)+(dy*dy));
                                    double a = 0;
                                    if(d != 0)
                                    {
                                        a = Math.acos(dx/d);
                                    }
                                    if(dy < 0)
                                    {
                                        a *= -1;
                                    }
                                    
                                   
                                    t[ix].set_x(t[ix].get_cent_x() - 0.1 * Math.cos(a));
                                    t[ix].set_y(t[ix].get_cent_y() - 0.1 * Math.sin(a));
                                    t[ix2].set_x(t[ix2].get_cent_x() + 0.1 * Math.cos(a));
                                    t[ix2].set_y(t[ix2].get_cent_y() + 0.1 * Math.sin(a));
                                    
                                    int count = 0;
                                    for(int m = 0; m < 3; m++)
                                    {
                                        for(int m1 = 0; m1 < 3; m1++)
                                        {
                                            double m_dx = t[ix].x_point(m) - t[ix2].x_point(m1);
                                            double m_dy = t[ix].y_point(m) - t[ix2].y_point(m1);
                                            double m_d = Math.sqrt((m_dx*m_dx)+(m_dy*m_dy));
                                            if(m_d < 20)
                                            {
                                                count++;
                                            }
                                        }
                                    }
                                    
                                    if(count >= 2)
                                    {
                                        
                                    }
                                    else if(part2 >= part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/1024;
                                        if(t[ix].get_radius() > t[ix2].get_radius())
                                        {
                                            angle /= 2;
                                        }
                                        t[ix].set_angle(t[ix].get_angle() + angle);
                                    }
                                    else if(part2 < part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/1024;
                                        if(t[ix].get_radius() > t[ix2].get_radius())
                                        {
                                            angle /= 2;
                                        }
                                        t[ix].set_angle(t[ix].get_angle() - angle);
                                    }
                                    if(count >= 2)
                                    {
                                        
                                    }
                                    else if(part >= part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/1024;
                                        if(t[ix2].get_radius() > t[ix].get_radius())
                                        {
                                            angle /= 2;
                                        }
                                        t[ix2].set_angle(t[ix2].get_angle() - angle);
                                    }
                                    else if(part < part_size/2 && rect_rotation)
                                    {
                                        double angle = Math.PI/1024;
                                        if(t[ix2].get_radius() > t[ix].get_radius())
                                        {
                                            angle /= 2;
                                        }
                                        t[ix2].set_angle(t[ix2].get_angle() + angle);
                                    }
                                }
                                
                            }
                        }
                        
                    }
                }
            }
        }
    }
    
    
    
    public void check_rect_tri()
    {
        for(int x = 0; x < n_rects; x++)
        {
            for(int y = 0; y < t_size; y++)
            {
                rect_tri_resolve(x,y);
            }
        }
    }
    
    public void rect_tri_resolve(int ix, int ix2)
    {
        
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.green);
        
        r[ix].set_collision(false);
        t[ix2].set_collision(false);
        
        
        for(int k = 0; k < 4; k++)
        {
            int k0 = k;
            int k1 = k0 + 1;
            k1 = k1 % 4;
            
            double x0 = r[ix].get_x_corner(k0);
            double x1 = r[ix].get_x_corner(k1);
            double y0 = r[ix].get_y_corner(k0);
            double y1 = r[ix].get_y_corner(k1);
            
            double x_length = (x1-x0)/part_size;
            double y_length = (y1-y0)/part_size;
            
            for(double part = 0; part < part_size; part++)
            {
                double x_cord = x0 + x_length * part;
                double y_cord = y0 + y_length * part;
                
                for(int b = 0; b < 3; b++)
                {
                    int b0 = b;
                    int b1 = b0 + 1;
                    b1 = b1 % 3;
                    
                    double x0_ = t[ix2].x_point(b0);
                    double x1_ = t[ix2].x_point(b1);
                    double y0_ = t[ix2].y_point(b0);
                    double y1_ = t[ix2].y_point(b1);
                    
                    double x_length_ = (x1_ - x0_)/part_size;
                    double y_length_ = (y1_ - y0_)/part_size;
                    
                    for(double part2 = 0; part2 < part_size; part2++)
                    {
                        double x2_cord = x0_ + x_length_ * part2;
                        double y2_cord = y0_ + y_length_ * part2;
                     
                        
                        if(x2_cord > x_cord - intersect_val && x2_cord < x_cord + intersect_val)
                        {
                            if(y2_cord > y_cord - intersect_val  && y2_cord < y_cord + intersect_val)
                            {
                                r[ix].set_collision(true);
                                t[ix2].set_collision(true);
                                
                                if(gravity)
                                {
                                    if(r[ix].center_y() <= t[ix2].get_cent_y())
                                    {
                                        r[ix].set_height(r[ix].get_height()*up_h/100);
                                        r[ix].set_dir(-1);
                                        t[ix2].set_height(t[ix2].get_height()*low_h/100);
                                        t[ix2].set_dir(1);
                                    }
                                    else
                                    {
                                        r[ix].set_height(r[ix].get_height()*low_h/100);
                                        r[ix].set_dir(1);
                                        t[ix2].set_height(t[ix2].get_height()*up_h/100);
                                        t[ix2].set_dir(-1);
                                    }
                                    
                                    double deltax = Math.abs(r[ix].center_x()-t[ix2].get_cent_x());
                                    deltax /= dx0_val;
                                    
                                    if(r[ix].center_x() <= t[ix2].get_cent_x())
                                    {
                                        r[ix].set_dx(r[ix].get_dx()-deltax);
                                        t[ix2].set_dx(t[ix2].get_dx()+deltax);
                                    }
                                    else
                                    {
                                        r[ix].set_dx(r[ix].get_dx()+deltax);
                                        t[ix2].set_dx(t[ix2].get_dx()-deltax);
                                    }
                                    
                                }
                                
                                
                                if(dragged_t == ix2)
                                {
                                    double dx = r[ix].center_x() - t[ix2].get_cent_x();
                                    double dy = r[ix].center_y() - t[ix2].get_cent_y();
                                    double d = Math.sqrt((dx*dx)+(dy*dy));
                                    double a = 0;
                                    if(d != 0)
                                    {
                                        a = Math.acos(dx/d);
                                    }
                                    if(dy < 0)
                                    {
                                        a *= -1;
                                    }
                                    r[ix].set_x0(r[ix].get_x0() + 1.0 * Math.cos(a));
                                    r[ix].set_y0(r[ix].get_y0() + 1.0 * Math.sin(a));
                                    t[ix2].set_x(t[ix2].get_cent_x() - 1.0  * Math.cos(a));
                                    t[ix2].set_y(t[ix2].get_cent_y() - 1.0 * Math.sin(a));
                                    
                                    if(part >= part_size/2 && rect_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() + Math.PI/128);
                                        if(double_rotation)
                                        {
                                            t[ix2].set_angle(t[ix2].get_angle() - Math.PI/128);
                                        }
                                    }
                                    else if(part < part_size/2 && rect_rotation)
                                    {
                                        r[ix].set_angle(r[ix].get_angle() - Math.PI/128);
                                        if(double_rotation)
                                        {
                                            t[ix2].set_angle(t[ix2].get_angle() + Math.PI/128);
                                        }
                                    }
                                    
                                    
                                }
                                else if(dragged_rect == ix)
                                {
                                    double dx = r[ix].center_x() - t[ix2].get_cent_x();
                                    double dy = r[ix].center_y() - t[ix2].get_cent_y();
                                    double d = Math.sqrt((dx*dx)+(dy*dy));
                                    double a = 0;
                                    if(d != 0)
                                    {
                                        a = Math.acos(dx/d);
                                    }
                                    if(dy < 0)
                                    {
                                        a *= -1;
                                    }
                                    
                                    t[ix2].set_x(t[ix2].get_cent_x() - 1.0 * Math.cos(a));
                                    t[ix2].set_y(t[ix2].get_cent_y() - 1.0 * Math.sin(a));
                                    r[ix].set_x0(r[ix].get_x0() + 1.0 * Math.cos(a));
                                    r[ix].set_y0(r[ix].get_y0() + 1.0 * Math.sin(a));
                                    
                                    if(part2 >= part_size/2 && rect_rotation)
                                    {
                                        t[ix2].set_angle(t[ix2].get_angle() + Math.PI/128);
                                        if(double_rotation)
                                        {
                                            r[ix].set_angle(r[ix].get_angle() - Math.PI/128);
                                        }
                                    }
                                    else if(part2 < part_size/2 && part2 > 2 && rect_rotation)
                                    {
                                        t[ix2].set_angle(t[ix2].get_angle() - Math.PI/128);
                                        if(double_rotation)
                                        {
                                            r[ix].set_angle(r[ix].get_angle() + Math.PI/128);
                                        }
                                    }
                                    
                                }
                                else
                                {
                                    double dx = r[ix].center_x() - t[ix2].get_cent_x();
                                    double dy = r[ix].center_y() - t[ix2].get_cent_y();
                                    double d = Math.sqrt((dx*dx)+(dy*dy));
                                    double a = 0;
                                    if(d != 0)
                                    {
                                        a = Math.acos(dx/d);
                                    }
                                    if(dy < 0)
                                    {
                                        a *= -1;
                                    }
                                    
                                    t[ix2].set_x(t[ix2].get_cent_x() - 1.0 * Math.cos(a));
                                    t[ix2].set_y(t[ix2].get_cent_y() - 1.0 * Math.sin(a));
                                    r[ix].set_x0(r[ix].get_x0() + 1.0 * Math.cos(a));
                                    r[ix].set_y0(r[ix].get_y0() + 1.0 * Math.sin(a));
                                    
                                    if(rect_rotation)
                                    {
                                        if(part >= part_size/2)
                                        {
                                            r[ix].set_angle(r[ix].get_angle() + Math.PI/128);
                                        }
                                        if(part < part_size/2)
                                        {
                                            r[ix].set_angle(r[ix].get_angle() - Math.PI/128);
                                        }
                                        
                                        if(part2 >= part_size/2)
                                        {
                                            t[ix2].set_angle(t[ix2].get_angle() + Math.PI/128);
                                        }
                                        if(part2 < part_size/2)
                                        {
                                            t[ix2].set_angle(t[ix2].get_angle() - Math.PI/128);
                                        }
                                    
                                    }
                                    
                                    
                                    
                                    
                                    
                                }
                                
                            }
                        }
                        
                    }
                }
            }
        }
    }
    
    
    public void draw_corner_points(int ix)
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.red);
            for(int z = 0; z < 4; z++)
            {
                int x = (int)r[ix].get_x_corner(z);
                int y = (int)r[ix].get_y_corner(z);
                g.fillOval(x-2,y-2,5,5);
            }
        
    }
    
    public void out_frame_collision()
    {
        for(int ix = 0; ix < n_rects; ix++)
        {
            if(r[ix].get_x0() < 0)
            {
                r[ix].set_x0(0);
            }
            if(r[ix].get_y0() < 0)
            {
                r[ix].set_y0(0);
            }
            if(r[ix].get_x1() > FRAME_WIDTH)
            {
                r[ix].set_x0(FRAME_WIDTH-r[ix].get_width());
            }
            if(r[ix].get_y1() > FRAME_WIDTH)
            {
                r[ix].set_y0(FRAME_WIDTH-r[ix].get_width());
            }
        }
        if(line.get_x0() < 0 && !dragged_line)
        {
            double dx = 0 - line.get_x0();
            line.set_x0(0);
            line.set_x1(line.get_x1()+dx);
        }
        if(line.get_x0() > FRAME_WIDTH && !dragged_line)
        {
            double dx = line.get_x0() - FRAME_WIDTH;
            line.set_x0(FRAME_WIDTH);
            line.set_x1(line.get_x1()-dx);
        }
        if(line.get_x1() < 0 && !dragged_line)
        {
            double dx = 0 - line.get_x1();
            line.set_x1(0);
            line.set_x0(line.get_x0()+dx);
        }
        if(line.get_x1() > FRAME_WIDTH && !dragged_line)
        {
            double dx = line.get_x1() - FRAME_WIDTH;
            line.set_x1(FRAME_WIDTH);
            line.set_x0(line.get_x0()-dx);
        }
        
        
        if(line.get_y0() < 0 && !dragged_line)
        {
            double dy = 0 - line.get_y0();
            line.set_y0(0);
            line.set_y1(line.get_y1()+dy);
        }
        if(line.get_y0() > FRAME_WIDTH && !dragged_line)
        {
            double dy = line.get_y0() - FRAME_WIDTH;
            line.set_y0(FRAME_WIDTH);
            line.set_y1(line.get_y1()-dy);
        }
        if(line.get_y1() < 0 && !dragged_line)
        {
            double dy = 0 - line.get_y1();
            line.set_y1(0);
            line.set_y0(line.get_y0()+dy);
        }
        if(line.get_y1() > FRAME_WIDTH && !dragged_line)
        {
            double dy = line.get_y1() - FRAME_WIDTH;
            line.set_y1(FRAME_WIDTH);
            line.set_y0(line.get_y0()-dy);
        }
        
        for(int ix = 0; ix < c_size; ix++)
        {
            if(c[ix].get_x() - c[ix].get_radius() < 0)
            {
                c[ix].set_cent_x(c[ix].get_radius());
            }
            if(c[ix].get_x() + c[ix].get_radius() > FRAME_WIDTH)
            {
                c[ix].set_cent_x(FRAME_WIDTH - c[ix].get_radius());
            }
            if(c[ix].get_y() - c[ix].get_radius() < 0)
            {
                c[ix].set_cent_y(c[ix].get_radius());
            }
            if(c[ix].get_y() + c[ix].get_radius() > FRAME_WIDTH)
            {
                c[ix].set_cent_y(FRAME_WIDTH - c[ix].get_radius());
            }
        }
        
        tri_out_of_frame();
        
        
    }
    
    public void draw()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_WIDTH);
        for(int ix = 0; ix < n_rects; ix++)
        {
            Graphics2D g2d = (Graphics2D)(g.create());
            
            if(r[ix].is_collision())
            {
                g2d.setColor(Color.red);
            }
            else
            {
                g2d.setColor(Color.blue);
            }
            double angle = r[ix].get_angle();
            double cent_x = r[ix].center_x();
            double cent_y = r[ix].center_y();
            g2d.rotate(angle,(int)cent_x,(int)cent_y);
            int x = (int)r[ix].get_x0();
            int y = (int)r[ix].get_y0();
            int w = (int)r[ix].get_width();
            int h = (int)r[ix].get_width();
            g2d.draw(new Rectangle(x,y,w,h));
            g2d.dispose();
            //draw_corner_points(ix);
            
            
        }
        
            //draw line
            if(is_line)
            {
                Graphics2D g2d = (Graphics2D)(g.create());
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.setColor(Color.blue);
                int x0 = (int)line.get_x0();
                int y0 = (int)line.get_y0();
                int x1 = (int)line.get_x1();
                int y1 = (int)line.get_y1();
                g2d.drawLine(x0,y0,x1,y1);
                g2d.dispose();
            }
            
            //draw circles
            
            if(is_circle)
            {
                Graphics2D g2d = (Graphics2D)(g.create());
                g2d.setColor(Color.blue);
                for(int ix = 0; ix < c_size; ix++)
                {   
                    int x = (int)(c[ix].get_x());
                    int y = (int)(c[ix].get_y());
                    int r = (int)(c[ix].get_radius());
                    g2d.drawOval(x-r,y-r,r*2,r*2);
                }
                g2d.dispose();
            }
            
            if(is_tri)
            {
                
                for(int ix = 0; ix < t_size; ix++)
                {
                    Graphics2D g2d = (Graphics2D)(g.create());
                    g2d.setColor(Color.blue);
                   
                    int [] x_points = t[ix].x_points();
                    int [] y_points = t[ix].y_points();
                    
                    Polygon p = new Polygon(x_points,y_points,3);
                    g2d.drawPolygon(p);
                
                    g2d.dispose();
                }
                
                
            }
            
            
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jSlider3 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jSlider4 = new javax.swing.JSlider();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jSlider5 = new javax.swing.JSlider();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        jLabel1.setText("nr of rects");

        jSlider1.setMaximum(16);
        jSlider1.setMinimum(2);
        jSlider1.setFocusable(false);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel2.setText("rect width");

        jSlider2.setMinimum(10);
        jSlider2.setFocusable(false);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jLabel3.setText("resolve val");

        jSlider3.setMinimum(5);
        jSlider3.setFocusable(false);
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });

        jLabel4.setText("part size (nr of intersect points)");

        jSlider4.setMaximum(60);
        jSlider4.setMinimum(20);
        jSlider4.setFocusable(false);
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("rotation");
        jCheckBox3.setFocusable(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("line");
        jCheckBox4.setFocusable(false);
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("circles");
        jCheckBox5.setFocusable(false);
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jCheckBox6.setText("double rotation");
        jCheckBox6.setFocusable(false);
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("triangles");
        jCheckBox7.setFocusable(false);
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        jLabel5.setText("rect width differ");

        jSlider5.setMaximum(175);
        jSlider5.setFocusable(false);
        jSlider5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider5StateChanged(evt);
            }
        });

        jCheckBox2.setText("gravity");
        jCheckBox2.setFocusable(false);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox8.setText("move auto");
        jCheckBox8.setFocusable(false);
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        jButton1.setText("reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox8)
                            .addComponent(jButton1)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox3)
                            .addComponent(jCheckBox4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox6)
                            .addComponent(jCheckBox2)))
                    .addComponent(jCheckBox5))
                .addGap(181, 181, 181))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox3)
                            .addComponent(jCheckBox6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox4)
                            .addComponent(jCheckBox2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCheckBox7)
                                    .addComponent(jButton1)))
                            .addComponent(jCheckBox8)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged

        int state = -1; // 0 rect, 1 circle , 2 line 
        
        double rect_val = 1000.0;
        int rect_ix = -1;
        for(int ix = 0; ix < n_rects; ix++)
        {
            double dx = evt.getX() - r[ix].center_x();
            double dy = evt.getY() - r[ix].center_y();
            double d = Math.sqrt((dx*dx)+(dy*dy));
            if(d < rect_val && d < r[ix].get_width()*3)
            {
                rect_val = d;
                rect_ix = ix;
            }
        }
        
        
        
        double circle_val = 1000.0;
        int circle_ix = -1;
        for(int ix = 0; ix < c_size; ix++)
        {
            double dx = evt.getX() - c[ix].get_x();
            double dy = evt.getY() - c[ix].get_y();
            double d = Math.sqrt((dx*dx)+(dy*dy));
            if(d < circle_val && d < c[ix].get_radius()*3)
            {
                circle_val = d;
                circle_ix = ix;
            }
        }
        
        
        
        double line_dist = 0;
        double int_line = -1;
        
            double dx = evt.getX() - line.get_x0();
            double dy = evt.getY() - line.get_y0();
            double d = Math.sqrt((dx*dx)+(dy*dy));
            double dx0 = evt.getX() - line.get_x1();
            double dy0 = evt.getY() - line.get_y1();
            double d0 = Math.sqrt((dx0*dx0)+(dy0*dy0));
            double length = line.length();
            
            if(d <= d0)
            {
                
                double dx5 = evt.getX() - line.get_x1();
                double dy5 = evt.getY() - line.get_y1();
                double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                if(d5 > 50)
                {
                    line_dist = d;
                    int_line = 1;
                }
                
                
            }
            else
            {
                double dx5 = evt.getX() - line.get_x0();
                double dy5 = evt.getY() - line.get_y0();
                double d5 = Math.sqrt((dx5*dx5)+(dy5*dy5));
                if(d5 > 50)
                {
                    line_dist = d0;
                    int_line = 0;
                }
            }
            
            double tri_val = 1000.0;
            int int_tri = -1;
            for(int ix = 0; ix < t_size; ix++)
            {
                double dx7 = evt.getX() - t[ix].get_cent_x();
                double dy7 = evt.getY() - t[ix].get_cent_y();
                double d7 = Math.sqrt((dx7*dx7)+(dy7*dy7));
                if(d7 < tri_val)
                {
                    tri_val = d7;
                    int_tri = ix;
                }
            }
            
            
            if(is_dragged_rect)
            {
                rect_ix = dragged_rect;
            }
            if(is_dragged_circle)
            {
                circle_ix = dragged_circle_index;
            }
            if(is_dragged_tri)
            {
                int_tri = dragged_t;
            }
            
            
            if(rect_val <= circle_val && rect_val <= line_dist && rect_val <= tri_val && rect_ix != -1 && !dragged_line && !is_dragged_circle && !is_dragged_tri)
            {  
                r[rect_ix].set_x0(evt.getX() - r[rect_ix].get_width()/2);
                r[rect_ix].set_y0(evt.getY() - r[rect_ix].get_width()/2);
                
                dragged_rect = rect_ix;
                is_dragged_rect = true;
                
            }
            if(circle_val <= rect_val && circle_val <= line_dist && circle_val <= tri_val && circle_ix != -1 && !dragged_line && !is_dragged_rect && !is_dragged_tri)
            {
                c[circle_ix].set_cent_x(evt.getX());
                c[circle_ix].set_cent_y(evt.getY());
                is_dragged_circle = true;
                dragged_circle_index = circle_ix;
            }
            if(line_dist <= rect_val && line_dist <= circle_val && line_dist <= tri_val && int_line != -1 && !is_dragged_rect && !is_dragged_circle && !is_dragged_tri)
            {
                if(int_line == 1)
                {
                    line.set_x0(evt.getX());
                    line.set_y0(evt.getY());
                    dragged_line = true;
                }
                else if(int_line == 0)
                {
                    line.set_x1(evt.getX());
                    line.set_y1(evt.getY());
                    dragged_line = true;
                }
            }
            if(tri_val <= rect_val && tri_val <= circle_val && tri_val <= line_dist && int_tri  != -1 && !dragged_line && !is_dragged_rect && !is_dragged_circle)
            {
                t[int_tri].set_x(evt.getX());
                t[int_tri].set_y(evt.getY());
                is_dragged_tri = true;
                dragged_t = int_tri;
            }
        
        
        
        
        
        

        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed

        
        double sm_val = 1000.0;
        int s_ix = -1;
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            double dx = evt.getX() - r[ix].center_x();
            double dy = evt.getY() - r[ix].center_y();
            double d = Math.sqrt((dx*dx)+(dy*dy));
            if(d < sm_val)
            {
                sm_val = d;
                s_ix = ix;
            }
        }
        dragged_rect = s_ix;
        
        if(evt.getButton() == 3)
        {
            dragged_rect = -2;
        }
        
        
        
        
        
        
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased

        dragged_rect = -1;
        dragged_circle_index = -1;
        dragged_t = -1;
        dragged_line = false;
        is_dragged_rect = false;
        is_dragged_circle = false;
        is_dragged_tri = false;
        
        
        
        // TODO add your handling code here:
           
    }//GEN-LAST:event_jPanel1MouseReleased

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

        // TODO add your handling code here:
        
        
        
        
        
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed

       
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1KeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed


        if(dragged_rect != -1)
        {
            r[dragged_rect].set_angle(r[dragged_rect].get_angle()+Math.PI/16);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged


        n_rects = jSlider1.getValue();
        init_rects();
        if(auto_move)
        {
            init_auto_move();
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged


        rect_w = jSlider2.getValue();
        init_rects();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged

        intersect_val = (double)(jSlider3.getValue())/10.0;
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider3StateChanged

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged

        part_size = jSlider4.getValue();
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider4StateChanged

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed


        if(jCheckBox3.isSelected())
        {
            rect_rotation = true;
        }
        else 
        {
            rect_rotation = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed


        if(jCheckBox4.isSelected())
        {
            is_line = true;
        }
        else
        {
            is_line = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed


        if(jCheckBox5.isSelected())
        {
            is_circle = true;
        }
        else
        {
            is_circle = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed


        if(jCheckBox6.isSelected())
        {
            double_rotation = true;
        }
        else
        {
            double_rotation = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed

        if(jCheckBox7.isSelected())
        {
            is_tri = true;
        }
        else
        {
            is_tri = false;
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jSlider5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider5StateChanged


        Random rand = new Random();
        rect_differ = (double)jSlider5.getValue()/100.0;
        double diff = rect_differ * rect_w/2;
        
        for(int ix = 0; ix < n_rects; ix++)
        {
            double val = -diff + (rand.nextInt((int)(diff*2 + 1)));
            r[ix].set_width(rect_w+val);
            r[ix].set_width(rect_w+val);
        }
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider5StateChanged

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed

        

        if(jCheckBox2.isSelected())
        {
            gravity = true;
            jCheckBox3.setSelected(false);
            jCheckBox6.setSelected(false);
            rect_rotation = false;
            double_rotation = false;
        }
        else
        {
            gravity = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed

        if(jCheckBox8.isSelected())
        {
            auto_move = true;
            init_auto_move();
        }
        else
        {
            auto_move = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        n_rects = 5;
        rect_w = 50;
        init();
        init_line();
        init_rects();
        init_circles();
        init_tri();
        init_auto_move();

        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(rect_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(rect_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(rect_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(rect_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rect_frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    // End of variables declaration//GEN-END:variables
}
