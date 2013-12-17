package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

/**
 * test
 * @author normenhansen
 */
public class MainMenu extends SimpleApplication {
    
    private static MainMenu app;
    private static Main game;
    private EnvironmentFactory ENVIRONMENT;
    
    private AudioNode ping;
    private AudioNode sound;
    private AudioNode tgle;

    public static void main(String[] args) {
        app = new MainMenu();
        game = new Main();
        AppSettings gameSettings = null;
        gameSettings = new AppSettings(false);
        gameSettings.setResolution(1024, 768);
        gameSettings.setFullscreen(false);
        gameSettings.setVSync(false);
        gameSettings.setTitle("DrieDee Mario");
        gameSettings.setUseInput(true);
        gameSettings.setFrameRate(500);
        gameSettings.setSamples(0);
        gameSettings.setRenderer("LWJGL-OpenGL2");
        app.settings = gameSettings;
        app.setShowSettings(false);
        app.start();
        
        game.settings = gameSettings;
        game.setShowSettings(false);
    }
    
    private BitmapFont myFont;
    private BitmapText[] hudText = new BitmapText[5];
    private ArrayList<DisplayMode> modes;
    private int resIndex = -1;
    private int schIndex = 1;
    private int selindex = 0;
    private DisplayMode defmod;
    private ChaseCamera chaseCam;

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        
        flyCam.setEnabled(false);
        
        ping = new AudioNode(assetManager, "Sounds/menutick.wav");
        ping.setReverbEnabled(false);
        tgle = new AudioNode(assetManager, "Sounds/bump.wav");
        tgle.setReverbEnabled(false);
        sound = new AudioNode(assetManager, "Sounds/menu.ogg");
        sound.setReverbEnabled(false);
        sound.setLooping(true);
        sound.play();
        
        chaseCam = new ChaseCamera(cam, rootNode, inputManager);
        chaseCam.setSmoothMotion(true);
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setMaxDistance(1000);
        chaseCam.setMinVerticalRotation(0.32259342f);
        chaseCam.setMaxVerticalRotation(0.32259343f);
        chaseCam.setDragToRotate(false);
        chaseCam.setDefaultVerticalRotation(0.32259343f);
        
        sceneobjects();
        
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        DisplayMode[] modesBuffer = device.getDisplayModes();
        defmod = device.getDisplayMode();
        modes = new ArrayList<DisplayMode>();
        
        for (DisplayMode mode : modesBuffer) {
            if (!modes.contains(mode))
            {
                modes.add(mode);
            }
        }
        
        
        
        myFont = assetManager.loadFont("Interface/Fonts/verdana-48-regular.fnt");
        
        for (int i = 0; i < hudText.length; i++)
        {
            hudText[i] = new BitmapText(myFont, false);
            hudText[i].setSize(myFont.getCharSet().getRenderedSize());      // font size
            hudText[i].setColor(ColorRGBA.White);                             // font color
        }
        
        hudText[0].setText("Start");
        hudText[0].setColor(ColorRGBA.Cyan);
        hudText[1].setText("< " + defmod.getWidth() + "x" + defmod.getHeight() + " >");
        hudText[2].setText("Volledig scherm Aan");
        hudText[3].setText("Credits");
        hudText[4].setText("Exit");
        
        for (int i = 0; i < hudText.length; i++)
        {
            hudText[i].setLocalTranslation((settings.getWidth()/2)-(hudText[i].getLineWidth()/2), settings.getHeight()-((i*60)+100), 0); // position
            guiNode.attachChild(hudText[i]);
        }
        
        inputManager.clearMappings();
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Up",  new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Toggle",  new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Toggle",  new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(actionListener,"Down","Up","Left","Right","Toggle");
    }
    
    private void sceneobjects()
    {
        Texture west = assetManager.loadTexture("Textures/left55.jpg");
        Texture east = assetManager.loadTexture("Textures/right55.jpg");
        Texture north = assetManager.loadTexture("Textures/back55.jpg");
        Texture south = assetManager.loadTexture("Textures/front55.jpg");
        Texture up = assetManager.loadTexture("Textures/top55.jpg");
        Texture down = assetManager.loadTexture("Textures/bottom.jpg");

        rootNode.attachChild(SkyFactory.createSky(assetManager, west, east, north, south, up, down));
        
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient); 
        
            /** A white, directional light source */ 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        
        Material mat_tex = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
        Texture tex = assetManager.loadTexture("Models/terrein/desert.jpg"); 
        mat_tex.setTexture("ColorMap", tex);
        
        Spatial terr = assetManager.loadModel("Models/terrein/terrein.j3o");
        terr.setMaterial(mat_tex);

        terr.setLocalTranslation(0, -40f, 0);
        terr.scale(25f, 25f, 18f);
        rootNode.attachChild(terr);
        
        Spatial shroom = assetManager.loadModel("Models/shroom/shroom.j3o");
        shroom.setLocalTranslation(-60f, -18f, 6f);
        shroom.rotate(0, 1.7f, 0);
        shroom.scale(10f);
        rootNode.attachChild(shroom);
        
        Spatial home = assetManager.loadModel("Models/home/home.j3o");
        home.setLocalTranslation(-100f, -40f, -60f);
        home.rotate(0, 0.8f, 0);
        home.scale(3f);
        rootNode.attachChild(home);
        
        Spatial brick = assetManager.loadModel("Models/brickf/brick.j3o");
        brick.setLocalTranslation(60f, -15f, -60f);
        brick.rotate(0, 1.7f, 0);
        brick.scale(22f);
        rootNode.attachChild(brick);
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Down") && !keyPressed) {
                selindex++;
                ping.play();
            }
            if (name.equals("Up") && !keyPressed) {
                selindex--;
                ping.play();
            }
            if (name.equals("Right") && !keyPressed) {
                if (selindex == 1)
                {
                    resIndex++;
                    tgle.play();
                }
                if (selindex == 2)
                {
                    tgle.play();
                    if (schIndex == 1)
                    {
                        schIndex = 0;
                    }
                    else 
                    {
                        schIndex = 1;
                    }
                }
            }
            if (name.equals("Left") && !keyPressed) {
                if (selindex == 1)
                {
                    resIndex--;
                    tgle.play();
                }
                if (selindex == 2)
                {
                    tgle.play();
                    if (schIndex == 1)
                    {
                        schIndex = 0;
                    }
                    else 
                    {
                        schIndex = 1;
                    }
                }
            }
            if (name.equals("Toggle") && !keyPressed) {
                if (selindex == 2)
                {
                    tgle.play();
                    if (schIndex == 1)
                    {
                        schIndex = 0;
                    }
                    else 
                    {
                        schIndex = 1;
                    }
                }
                
                if (selindex == 4)
                {
                    app.stop();
                }
                
                if (selindex == 0)
                {
                    game.start();
                }
            }
            if (resIndex >= modes.size())
            {
                resIndex = 0;
            }
            if (resIndex < 0)
            {
                resIndex = (modes.size()-1);
            }
            if (selindex >= hudText.length)
            {
                selindex = 0;
            }
            if (selindex < 0)
            {
                selindex = (hudText.length-1);
            }
            
            hudText[0].setText("Start");
            hudText[0].setColor(ColorRGBA.White);
            hudText[1].setText("< " + modes.get(resIndex).getWidth() + "x" + modes.get(resIndex).getHeight() + " >");
            hudText[1].setLocalTranslation((settings.getWidth()/2)-(hudText[1].getLineWidth()/2), settings.getHeight()-160, 0);
            hudText[1].setColor(ColorRGBA.White);
            if (schIndex == 1)
            {
                hudText[2].setText("Volledig scherm Aan");
            }
            else
            {
                hudText[2].setText("Volledig scherm Uit");
            }
            hudText[2].setColor(ColorRGBA.White);
            hudText[3].setText("Credits");
            hudText[3].setColor(ColorRGBA.White);
            hudText[4].setText("Exit");
            hudText[4].setColor(ColorRGBA.White);
            
            hudText[selindex].setColor(ColorRGBA.Cyan);
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        chaseCam.setDefaultHorizontalRotation(chaseCam.getHorizontalRotation()+0.0002f); //normaal
        //chaseCam.setDefaultHorizontalRotation(chaseCam.getHorizontalRotation()+0.002f); //snel
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
