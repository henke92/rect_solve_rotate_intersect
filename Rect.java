/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Rect
{
    private double x0;
    private double y0;
    private double width;
    private double angle;
    private double [] x_corner;
    private double [] y_corner;
    private boolean collision;
    
    
    private double height;
    private double dx;
    private double dir;
    private double GRAVITY = 6.0;
    
    
    
    public Rect(double x, double y, double w, double h, double a)
    {
        x0 = x;
        y0 = y;
        width = w;
        angle = a;
        
        x_corner = new double[4];
        y_corner = new double[4];
        
        collision = false;
        
        dir = 1;
        height = 0;
    }
    
    public double get_dx()
    {
        return this.dx;
    }
    
    public void set_dx(double dx)
    {
        this.dx = dx;
    }
    
    public double get_height()
    {
        return this.height;
    }
    
    public double get_dir()
    {
        return this.dir;
    }
    
    
    public void set_height(double h)
    {
        this.height = h;
    }
    
    public void set_dir(double d)
    {
        this.dir = d;
    }
    
    public void update()
    {
        height += 0.1 * dir;
        if(dir == -1 && height <= 0.25)
        {
            dir *= -1;
        }
        
        this.dx *= 0.997;
        
        this.x0 += dx;
        
        this.y0 = this.y0 + this.dir * GRAVITY * Math.pow(height,2);
    }
    
    public boolean is_collision()
    {
        return this.collision;
    }
    
    public void set_collision(boolean status)
    {
        collision = status;
    }
    
    public void set_angle(double a)
    {
        this.angle = a;
    }
    
    public void set_width(double w)
    {
        this.width = w;
    }
    
    public void set_y0(double val)
    {
        this.y0 = val;
    }
    
    public void set_x0(double val)
    {
        this.x0 = val;
    }
    
    public void set_y_corners(double [] vals)
    {
        for(int ix = 0; ix < 4; ix++)
        {
            this.y_corner[ix] = vals[ix];
        }
        
    }
    
    public void set_x_corners(double [] vals)
    {
        for(int ix = 0; ix < 4; ix++)
        {
            this.x_corner[ix] = vals[ix];
        }
    }
    
    public double get_y_corner(int ix)
    {
        return this.y_corner[ix];
    }
    
    public double get_x_corner(int ix)
    {
        return this.x_corner[ix];
    }
    
    public double get_angle()
    {
        return this.angle;
    }
    
    
    public double get_width()
    {
        return this.width;
    }
    
    public double center_y()
    {
        return this.y0 + this.width/2;
    }
    
    public double center_x()
    {
        return this.x0 + this.width/2;
    }
    
    public double get_y1()
    {
        return this.y0 + this.width;
    }
    
    public double get_x1()
    {
        return this.x0 + this.width;
    }
    
    public double get_y0()
    {
        return this.y0;
    }
    
    public double get_x0()
    {
        return this.x0;
    }
    
}
