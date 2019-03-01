package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Screen.MenuScreen;
import inf112.skeleton.app.Screen.PlayScreen;

public class RoboRally extends Game {
    public SpriteBatch batch;
    public final static int width = 385;
    public final static int height = 385;

    //endre disse om du endrer cfg i main
    public final static float cfgWidth = 800;
    public final static float cfgHeight = 480;

    @Override
    public void create() {

        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render(){
        super.render();
    }
}
