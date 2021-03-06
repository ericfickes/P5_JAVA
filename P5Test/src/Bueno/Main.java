package Bueno;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;


/**
 *  BUENO - NOPE, fails, but new save() method and other tweaks
 *
 *  PLAN B : locate ANY alternate lissajous formula
 *  ** validate original shapeJouse
 */


/**
 * - shapeJuan() logic changes makes totally different shapes than shapeJous()
 *
 * - doExit() NO LONGER saves png.  This was causing a 37th blank png to be created
 *
 * - COLOR FILL : shape1, 2, 3 -> Red, Blue, Green -> where frameCount is the primary color, and xx/yy the other two
 *  EX: fill(frameCount%255, xx%255, yy%255, 100);
 *
 * // TODO: read this post about points in lissajous curve to confirm if 360 is the ideal max number of points
 //  https://mathoverflow.net/questions/38118/formula-for-number-of-intersection-points-for-lissajous-curve/38546#38546
 //  - then tweak shapeJuan()

 *  - TODO: finalize RnD and move on to pureJuice path -> harden Juan then start down the juice path

 * - HINTS are back on, but can't tell if they make any difference
 *
 * - solved relative base path situation
 *      String PROJECT_ROOT = System.getProperty("user.dir");
        String PNG_OUT = PROJECT_ROOT + "/out/";
 *
 *
 * - PNGs are now saving to the P5Test/out folder
 * save(  PNG_OUT + this + "_" + ct + ".png");
 *
 * - new util functions : doExit(), savePng().
 *
 *
 * - xyInc : PI CHANGES EVERYTHING!
 *  - changed to w*PI
 *      - grid looks great, nice spacing
 *      - shapes are totally different than before !
 *
 * - w = 42
 *
 * - grid adjusted
 *  - xx/yy starting point = w
 *  - xyInc : grid control in one place
 *
 * - strokeWeight(2) : 2 like xyInc which is w*2 -> PI too fat for this setup
 *  - if strokeWeight goes up, xyInc will need adjustment
 *
 * - using actual w param for shape size
 *
 * - Using a mixture of 2D & 3D shapes
 *
 * - 9 - 36 trials
 *  - NEW incrementer debug stamp in the middle-ish
 *
 * - Z VALUE IN SHAPEJOUS () CRRRRAAAAZZYYYYY MAAAANNNN
 *  - Z mods INC
 *

 * - setupStage() util for resetting stage.  Hoping to locate some P5 smoothing magic to draw better lines
 *  - still on TODO list : curveVertex() w/Lissajous point array
 *
 *  sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */
public class Main extends PApplet {

    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 42;
    PShape tmp = new PShape();

    //  TODO: is there a smarter way to "get relative" when saving PNGs from a running PApplet?
    String PROJECT_ROOT = System.getProperty("user.dir");
    String PNG_OUT = PROJECT_ROOT + "/out/";



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");
        smooth(8);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    /**
     * The setup() function is run once, when the program starts.
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     If the sketch is a different dimension than the default, the size() function or fullScreen() function must be the first line in setup().
     Note: Variables DECLARED within setup() are not accessible within other functions, including draw().
     */
    public  void  setup ()  {

        setupStage();
        strokeWeight(2);

        //  setup variables
        cX = width/2;
        cY = height/2;

        xx = yy = w;
        xyInc = (int)(w*2);    // x|y increment grid control

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  draw ()  {


        tmp = shapeJuan(xx, yy, w, ct);

        fill(frameCount%255, xx%255, yy%255, 100);
        stroke(frameCount%255);

        //  SHAPE1
        shape(tmp);

        //  SHAPE2  - G
        beginShape();

            stroke(frameCount%255);

            for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
            {
                PVector vect = tmp.getVertex(vv);

                fill(xx%255, frameCount%255, yy%255, 100);

                vertex( vect.x, vect.y, vect.z, vect.y, vect.x );
            }
        endShape();


        //  SHAPE3
        //  do the FredV
        pushMatrix();

    //TODO: how do you get 3D shapes different color than 2D shapes?
            fill(xx%255, yy%255, frameCount%255, 100);
            stroke(frameCount%255);

            translate(xx, yy, w);
            scale(random(.6f,4.2f));
            rotate(random(HALF_PI, TWO_PI));

            shape(tmp);
        popMatrix();



        if (xx >= width) {
            xx = w;
            yy += xyInc;

        //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {


            //  STAMP IT
            fill(255,0,0,69);
            rect(0, 0, w*2, w*HALF_PI);

            fill(255);
            textSize(42);

            text( ct, w/2, w);

            savePng();

            xx = yy = w;

            // only increment after drawing finishes entire screen
            ct++;

            //  reset stage
            setupStage();

            System.gc(); // optionally add one of these
        } else {
            xx += xyInc;
        }



        //  stopper
        if(ct>36){
            doExit();
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                doExit();
            }
            break;

            case 's':
            case 'S':
            {
                savePng();
            }
            break;
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * exit function
     */
    private void doExit(){
//        savePng();

        noLoop();
        exit();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * SAVE PNG helper
     */
    void savePng(){
        save(  PNG_OUT + this + "_" + ct + ".png");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Helper function to clear stage
     */
    private void setupStage() {

        //  reset stage
        background(-1);

        smooth(8);
        strokeCap(ROUND);
        strokeJoin(ROUND);

//  TODO: re-visit HINTS
hint(DISABLE_DEPTH_SORT);
hint(DISABLE_DEPTH_TEST);
hint(DISABLE_OPENGL_ERRORS);
hint(DISABLE_ASYNC_SAVEFRAME);
hint(DISABLE_BUFFER_READING);
hint(DISABLE_DEPTH_MASK);




    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  STILL NORMING
     * @param a X
     * @param b Y
     * @param amp "amplitude"
     * @param inc incrementor, "slicer of the max point count : typically 360/inc"
     * @return PShape
     */
    private PShape shapeJuan( float a, float b, float amp, int inc )
    {
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 1 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape();

        float x, y;

        for ( int t = 0; t <= 180; t+=inc)  //  420 instead of 360?
        {
            //  NEW HOTNESS!
            x = a - amp * cos((a * t * TWO_PI)/360);
            y = b - amp * sin((b * t * TWO_PI)/360);

            //  Z mods INC
            shp.vertex(x, y, t%inc);
        }
        shp.endShape();

        return shp;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Lissajous PShape maker
     * @param a     X coordinate
     * @param b     Y coordinate
     * @param amp   Amplitude or size
     * @param inc   Loop magic incrementer [ 1 - 36 supported ]. (360 / inc) = number of points in returned PShape
     * @return  PShape containing vertices in the shape of a lissajous pattern
     */
    private PShape shapeJous( float a, float b, float amp, int inc )
    {
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 1 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape();

        float x, y;

        for ( int t = 0; t <= 360; t+=inc)
        {
            x = a - amp * sin((a * t * PI)/180);
            y = b - amp * sin((b * t * PI)/180);

            //  Z mods INC
            shp.vertex(x, y, t%inc);
        }
        shp.endShape();

        return shp;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run Bueno.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "Bueno.Main" );
    }

}

/*


public void plot (Graphics g)
{
double i;
double xm,ym,x1,y1;
//max=10;
//part=120;
x1=50;
y1=50;
setDesf(desf2);
tt=0.0;
for(i=0;i<max;i+=max/part)
{
    xm=Math.sin(ome1*tt);
    ym=Math.sin(ome2*tt+desf);
    coordenadas(xm,ym);

    xp+=60;
    yp+=70;
    if(i>0)g.drawLine((int)x1,(int)y1,(int)xp,(int)yp);
    x1=xp;
    y1=yp;
    tt=tt+max/part;
}

}



int xi,yi,xc,yc,r;
xc=30;
yc=30;
r=10;




* */