package at.tugraz.ist.swe;

class Point extends android.graphics.Point {
    private int x, y ,previous_x, previous_y;

    public Point(int set_x, int set_y, int set_previous_x, int set_previous_y) {
        x = set_x;
        y = set_y;
        previous_y = set_previous_y;
        previous_x = set_previous_x;
    }
    @Override
    public String toString() {
        return x + ", " + y;
    }

    public void setX(int new_x){
      x = new_x;
    }

    public void setY(int new_y){
        y = new_y;
    }

    public void setPreviosX(int new_previous_x){
        previous_x = new_previous_x;
    }

    public void setPreviosY(int new_previous_y){
        previous_y = new_previous_y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getPreviousX(){
        return previous_x;
    }

    public int getPreviousY(){
        return previous_y;
    }
}
