package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
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

    AGV avg;
    Barge barge;
    Container[] container = new Container[19];
    FreeCrane[] freeCranes = new FreeCrane[4];
    Port port;
    Freighter freighter;
    RailCrane railCrane;
    //private StorageCrane storageCrane = new StorageCrane(assetManager, rootNode);
    Train train;
    Truck[] truck = new Truck[20];

    public static void main(String[] args)
    {
        PortSimulation app = new PortSimulation();
        app.start();

        Runnable networkHandler = new NetworkHandler("141.252.222.124", 1337);
        Thread t = new Thread(networkHandler);
        t.start();
    }

    @Override
    public void simpleInitApp()
    {
        flyCam.setMoveSpeed(50f);
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
        
        for (int i = 0; i < 20; i++)
        {
            truck[i] = new Truck(assetManager, rootNode);
            truck[i].rotate(0, -90*FastMath.DEG_TO_RAD, 0);
            truck[i].scale(0.15f);
            truck[i].place(40.32f,5f,i);
        }
        

        freeCranes[0].place(0, 5f, 81);
        freeCranes[1].place(10, 5f, 81);
        freeCranes[2].place(-40, 5f, 81);
        freeCranes[3].place(-30, 5f, 81);
        
        railCrane = new RailCrane(assetManager, rootNode);
        railCrane.place(-42f,5f,-1.52f);
        
        train = new Train(assetManager, rootNode);
        train.place(-42,5f,0);
        
        //avg = new AVG(assetManager, rootNode);
        
        freighter = new Freighter(assetManager, rootNode);
        freighter.scale(3f);
        freighter.place(0, 4f, 85);
        
        barge = new Barge(assetManager, rootNode);
        barge.scale(3f);
        barge.place(44, 4.5f, 70);
        
        for (int i = 0; i < 19; i++)
        {
            container[i] = new Container(assetManager, rootNode, ColorRGBA.randomColor());
            container[i].place(-42,5.23f,-1.5f*(i+1));
        }
        
        railCrane.attachContainer(container);

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
        
        Vector3f waterLocation=new Vector3f(0,4.5f,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);
        
        waterProcessor.setWaterDepth(40);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves
        
        Quad quad = new Quad(400,400);
        quad.scaleTextureCoordinates(new Vector2f(24f,24f));
        
        Geometry water=new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-200, 4.5f, 250);
        water.setShadowMode(ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());

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
        //    container[i].move(0, 0, tpf*2);
        //}
        
        railCrane.update(tpf);
    }
}
