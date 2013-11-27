package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

/**
 * test
 *
 * @author normenhansen
 */
public class PortSimulation extends SimpleApplication
{

    //private AVG avg = new AVG(assetManager, rootNode);
    //private Barge barge = new Barge(assetManager, rootNode);
    Container[] container = new Container[19];
    FreeCrane[] freeCranes = new FreeCrane[4];
    Port port;
    //private Freighter freighter = new Freighter(assetManager, rootNode);
    RailCrane railCrane;
    //private StorageCrane storageCrane = new StorageCrane(assetManager, rootNode);
    Train train;
    //private Truck truck = new Truck(assetManager, rootNode);

    public static void main(String[] args)
    {
        PortSimulation app = new PortSimulation();
        app.start();

        //NetworkHandler networkHandler = new NetworkHandler("141.252.222.172", 1337);
        //Thread t = new Thread(networkHandler);
        //t.start();
    }

    @Override
    public void simpleInitApp()
    {
        flyCam.setMoveSpeed(15f);
        cam.setLocation(new Vector3f(0f, 20f, 0));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));


        for (int i = 0; i < 4; i++)
        {
            freeCranes[i] = new FreeCrane(assetManager, rootNode);
            freeCranes[i].scale(0.5f);
            freeCranes[i].rotate(0, 180 * FastMath.DEG_TO_RAD, 0);
        }
        port = new Port(assetManager, rootNode);
        //port.scale(10f);
        //port.place();

        freeCranes[0].place(0, 5f, 81);
        freeCranes[1].place(10, 5f, 81);
        freeCranes[2].place(-40, 5f, 81);
        freeCranes[3].place(-30, 5f, 81);
        
        railCrane = new RailCrane(assetManager, rootNode);
        railCrane.place(-42f,5f,0);
        
        train = new Train(assetManager, rootNode, 19);
        train.place(-42,5f,0);
        
        for (int i = 0; i < 19; i++)
        {
            container[i] = new Container(assetManager, rootNode, ColorRGBA.randomColor());
            container[i].place(-42,5.1f,-1.5f*(i+1));
        }

        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir = new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        rootNode.addLight(sun);

        DirectionalLight sun2 = new DirectionalLight();
        Vector3f lightDir2 = new Vector3f(0.37352666f, 0.50444174f, 0.7784704f);
        sun2.setDirection(lightDir2);
        sun2.setColor(ColorRGBA.White.clone().multLocal(2));
        rootNode.addLight(sun2);

        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);
        waterProcessor.setDebug(false);
        waterProcessor.setLightPosition(lightDir);
        waterProcessor.setRefractionClippingOffset(1.0f);

        //setting the water plane
        Vector3f waterLocation = new Vector3f(0, 0, 0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        waterProcessor.setWaterColor(ColorRGBA.Yellow);

        Quad quad = new Quad(400, 400);

        //the texture coordinates define the general size of the waves
        quad.scaleTextureCoordinates(new Vector2f(12f, 12f));

        Geometry water = new Geometry("water", quad);
        //water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setMaterial(waterProcessor.getMaterial());
        water.setLocalTranslation(-200, 0, 200);

        rootNode.attachChild(water);

        viewPort.addProcessor(waterProcessor);
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        //freeCranes[0].move(tpf/2, 0, 0);
        //train.move(0,0,tpf*2);
        //train.model.rotate(tpf, tpf, tpf)
        
        //for (int i = 0; i < 19; i++)
        //{
         //   container[i].move(0, 0, tpf*2);
        //}
        
        //railCrane.moveTop(tpf);
        railCrane.update(tpf);
    }
}
