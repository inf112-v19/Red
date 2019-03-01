package inf112.skeleton.app.Screen.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.GameMap;
import inf112.skeleton.app.GameObjects.Directions.Direction;
import inf112.skeleton.app.RoboRally;
import inf112.skeleton.app.Scenes.Hud;

public class TestScreen implements Screen {

    private RoboRally game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private GameMap gameMap;
    private float time = 0;


    public TestScreen(RoboRally game) {
        this.game = game;
        this.gameCam = new OrthographicCamera();
        this.gamePort = new FitViewport(RoboRally.width, RoboRally.height, gameCam);
        this.hud = new Hud(game.batch);
        this.gameMap = new GameMap("assets/map3.tmx", 3);
        this.map = gameMap.getMap();
        this.renderer = new OrthogonalTiledMapRenderer(map);

        gameCam.position.set(gamePort.getWorldWidth() / 2, (gamePort.getWorldHeight() / 2), 0);


    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        updateMap();
    }


    public void handleInput(float deltaTime) {
        time += deltaTime;

        if (Gdx.input.isKeyPressed(Input.Keys.W) && time > 0.2) {
            time = 0;
            gameMap.movePlayer(Direction.NORTH,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && time > 0.2) {
            time = 0;
            gameMap.movePlayer(Direction.EAST,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) && time > 0.2) {
            time = 0;
            gameMap.movePlayer(Direction.SOUTH,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && time > 0.2) {
            time = 0;
            gameMap.movePlayer(Direction.WEST,0);
        }


    }

    private void updateMap() {
        map = gameMap.getMap();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(Gdx.graphics.getDeltaTime());

        renderer.setView(gameCam);
        renderer.render();


//        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
//        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
