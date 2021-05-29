/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Circle 
{
    private double cent_x, cent_y, radius;
    
    private double height;
    private double dx;
    private double dir;
    private double GRAVITY = 6.0;
    
    
    public Circle(double x, double y, double rad)
    {
        cent_x = x;
        cent_y = y;
        radius = rad;
        this.dx = 0;
        this.dir = 1;
        height = 0;
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
    
    public double get_dir()
    {
        return this.dir;
    }
    
    public double get_dx()
    {
        return this.dx;
    }
    
    public double get_height()
    {
        return this.height;
    }
    
    public void update()
    {
        this.height += 0.1 * this.dir;
        if(this.height <= 0.25 && this.dir == -1)
        {
            this.dir *= -1;
        }
        
        this.dx *= 0.997;
        
        this.cent_x += this.dx;
        
        this.cent_y = this.cent_y + this.dir * GRAVITY * Math.pow(this.height,2);
        
    }
    
    public double get_radius()
    {
        return this.radius;
    }
    
    public double get_y()
    {
        return this.cent_y;
    }
    
    public double get_x()
    {
        return this.cent_x;
    }
    
    public void set_radius(double r)
    {
        this.radius = r;
    }
    
    public void set_cent_y(double y)
    {
        this.cent_y = y;
    }
    
    public void set_cent_x(double x)
    {
        this.cent_x = x;
    }
}
