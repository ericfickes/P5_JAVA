package Test3;

import processing.core.PApplet;
import processing.core.PShape;

import java.awt.*;

/**
 * sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */


/**
 * This is the sketch
 */
public class Main extends PApplet {


    int cX;
    int cY;

    float x,y, t;

    //color[] p1 = { #FEFEFC,#89776E,#ECE9E8,#AA9991,#322D2B,#DBD7D8,#D2C7BA,#B2A699,#CAC7C7,#D8CDC9,#BAB6B7,#C9BDB9,#918678,#E9DED9,#EEE7DD,#AAA6A7,#B8ACA8,#999596,#DFDC6A,#888584,#787574,#6A6456,#665854,#4D4337,#686564,#575453,#978B87,#F7EFEA,#474443,#DCD6CC,#423833,#766A65,#C8B5AE,#F8F7F3,#554946,#BBB6AA,#C6B192,#E8D6CF,#787368,#9A9489,#A98A79,#CEA070,#565348,#F8F785,#DDDEE1,#D2CB92,#BBBDC1,#B4AE73,#CDCED2,#100B0B,#E7D0B5,#ABADB1,#9B9CA0,#EEEFF1,#C7BDC6,#EFEEB6,#8F8754,#BDC6CB,#DEE7EA,#B3AE52,#8B8C8F,#C7ACA6,#9BA5AA,#A79C9E,#CED6DB,#D7CED6,#7A7B7E,#29211F,#7A8589,#D4BBAB,#B6ADB6,#D7BDB7,#595A5D,#ADB5BA,#958C95,#6A6B6E,#867B7D,#211715,#C49789,#EFF7F8,#494A4D,#8B9499,#645A63,#E7DEE7,#7F8474,#746B74,#E9B48E,#5A6467,#596357,#E8CDC7,#443942,#6A7476,#E4D897,#534A52,#6A7369,#8F5544,#37363A,#F8DBC9,#BF785E,#EABDA7,#C4C6B0,#4A5248,#394245,#A2A03F,#A2985A,#4A5355,#E0E7D3,#A4A59A,#8B948A,#F7EFF7,#ACB5A8,#414239,#FBE2DA,#211821,#CED6CA,#7E3827,#D9AB9D,#EFF7EE,#212223,#C26642,#C7C468,#CAABBA,#18181B,#D0CA4A,#E39E80,#182117,#E6E588,#A2A075 };
    float ct = 0.5f;
    float xx, yy, startX, startY;
    float w = 36;

    boolean stamped = false;

    PShape tmp;
//  720p 1280 x 720

    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");


    }

    @Override
    /**
     * The setup() function is run once, when the program starts.
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     If the sketch is a different dimension than the default, the size() function or fullScreen() function must be the first line in setup().
     Note: Variables declared within setup() are not accessible within other functions, including draw().
     */
    public  void  setup ()  {

        background(0x424242);

        smooth();
        stroke( 255 );
        noFill();
        strokeWeight(2);
        textSize(w/2);

        //  setup variables
        cX = width/2;
        cY = height/2;
        startX = cX;
        startY = cY;
        xx = yy = (w/2);
    }

    @Override
    public  void  draw ()  {

        // debug for reference
        if( !stamped ) {
            fill((int)frameCount%255, (int)xx*w, (int)yy*w);

            text( nf( ct, 2, 1), (float) (w*1.25), (float)(w*1.25) );
            stamped = true;
        }
            translate(xx, yy, 11);

            tmp = shapeJous(xx, yy, (float)(w*0.75), ct);
            shape( tmp );
            tmp = null;
//        if (Runtime.getRuntime().freeMemory() > 1000000) {
//            // add or do stuff
//        } else {
            System.gc(); // optionally add one of these
//        }

        
            if (xx >= width) {
                xx = (w /2);
                yy += w;

            } else if (yy >= height) {

                //  DEBUG and save for reference
                save(this+"_"+ ct +".png");
                stamped = false;
                yy = (w/2);

// only increment after drawing finishes entire screen
                background(0x424242);
                ct += 0.5;

            } else {
                xx += w;
            }



            //  stopper
            if(ct>36){
                exit();
            }

    }


    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                save(this+".png");
                noLoop();
                super.exit();
            }
            break;

            case 's':
            case 'S':
                {
                save(this+".png");
            }
            break;
        }

    }



    //////////////////////////////////////////////////////
//  Lissajous PShape maker
    private PShape shapeJous( float a, float b, float amp, float inc )
    {

//  DEBUG
//println("shapeJous( " + a + "," + b + "," + amp + "," + inc + " )");
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 0.5 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape();

        float x, y;


//  TODO: build a P5 GIF maker that runs this shapeJous and increments the T+ value by .5
//  record frames and animate, include spitting out of T value so you know
        for ( float t = 0; t <= 360; t+=inc)
        {
            x = a - amp * sin(a * t * PI/180);
            y = b - amp * sin(b * t * PI/180);

            shp.vertex(x, y);
        }
        shp.endShape();

        return shp;
    }



    /**
     * Start and run Test1.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "Test3.Main" );
    }

}
