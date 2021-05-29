/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Triangle 
{
    private double center_x;
    private double center_y;
    private double radius;
    private double angle;
    private boolean collision;
    
    private double dx;
    private double height;
    private double dir;
    private double GRAVITY = 6.0;
    
    
    public Triangle(double x, double y, double rad, double a)
    {
        center_x = x;
        center_y = y;
        radius = rad;
        angle = a;
        collision = false;
        
        this.height = 0;
        this.dir = 1;
        this.dx = 0;
        
    }
    
    public double get_GRAVITY()
    {
        return this.GRAVITY;
    }
    
    public void set_GRAVITY(double g)
    {
        this.GRAVITY = g;
    }
    
    public double get_dx()
    {
        return this.dx;
    }
    
    public double get_dir()
    {
        return this.dir;
    }
    
    public double get_height()
    {
        return this.height;
    }
    
    public void set_height(double h)
    {
        this.height = h;
    }
    
    public void set_dir(double d)
    {
        this.dir = d;
    }
    
    public void set_dx(double dx)
    {
        this.dx = dx;
    }
    
    public void update()
    {
        this.height += 0.1 * this.dir;
        if(this.height <= 0.25 && this.dir == -1)
        {
            this.dir *= -1;
        }
        
        this.dx *= 0.997;
        
        this.center_x += this.dx;
        
        this.center_y = this.center_y + this.dir * GRAVITY * Math.pow(height,2);
    }
    
    public boolean is_collision()
    {
        return collision;
    }
    
    public void set_collision(boolean status)
    {
        this.collision = status;
    }
    
    public int y_point(int ix)
    {
        double a = angle;
        for(int x = 0; x < ix; x++)
        {
            a += 2*Math.PI / 3;
        }
        int y_point = (int)(center_y + (radius * Math.sin(a)));
        return y_point;
    }
    
    public int x_point(int ix)
    {
        double a = angle;
        for(int x = 0; x < ix; x++)
        {
            a += 2*Math.PI / 3;
        }
        int x_point = (int)(center_x + (radius * Math.cos(a)));
        return x_point;
    }
    
    public int [] y_points()
    {
        int [] points = new int[3];
        double a = angle;
        for(int ix = 0; ix < 3; ix++)
        {
            points[ix] = (int)(center_y + (radius * Math.sin(a)));
            a += 2*Math.PI / 3;
        }
        return points;
    }
    
    public int [] x_points()
    {
        int [] points = new int[3];
        double a = angle;
        for(int ix = 0; ix < 3; ix++)
        {
            points[ix] = (int)(center_x + (radius * Math.cos(a)));
            a += 2*Math.PI / 3;
        }
        return points;
    }
    
    public double get_angle()
    {
        return angle;
    }
    
    public double get_radius()
    {
        return radius;
    }
    
    public double get_cent_y()
    {
        return center_y;
    }
    
    public double get_cent_x()
    {
        return center_x;
    }
    
    public void set_angle(double a)
    {
        angle = a;
    }
    
    public void set_radius(double r)
    {
        radius = r;
    }
    
    public void set_y(double y)
    {
        center_y = y;
    }
    
    public void set_x(double x)
    {
        center_x = x;
    }
    
}
