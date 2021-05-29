/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Line 
{
    private double x0,y0,x1,y1;
    
    private double dx;
    private double height;
    private double dir;
    private double GRAVITY = 6.0;
    
    
    public Line(double x0, double y0, double x1, double y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        
        dx = 0;
        height = 0;
        dir = 1;
        
    }
    
    public double get_GRAVITY()
    {
        return this.GRAVITY;
    }
    
    public double get_dir()
    {
        return this.dir;
    }
    
    public double get_height()
    {
        return this.height;
    }
    
    public double dx()
    {
        return this.dx;
    }
    
    public void set_GRAVITY(double g)
    {
        this.GRAVITY = g;
    }
    
    public void set_dir(double d)
    {
        this.dir = d;
    }
    
    public void set_height(double h)
    {
        this.height = h;
    }
    
    public void set_dx(double dx)
    {
        this.dx = dx;
    }
    
    public void update()
    {
        height += 0.1 * dir;
        if(height <= 0.25 && dir == -1)
        {
            dir *= -1;
        }
        
        this.dx *= 0.997;
        
        this.x0 += dx;
        this.x1 += dx;
        
        double dy = dir * GRAVITY * Math.pow(height,2);
        this.y0 += dy;
        this.y1 += dy;
        
    }
    
    public double length()
    {
        double dx = x1-x0;
        double dy = y1-y0;
        return Math.sqrt((dx*dx)+(dy*dy));
    }
    
    public void set_y1(double y1)
    {
        this.y1 = y1;
    }
    
    public void set_x1(double x1)
    {
        this.x1 = x1;
    }
    
    public void set_y0(double y0)
    {
        this.y0 = y0;
    }
    
    public void set_x0(double x0)
    {
        this.x0 = x0;
    }
    
    public double get_y1()
    {
        return this.y1;
    }
    
    public double get_x1()
    {
        return this.x1;
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
