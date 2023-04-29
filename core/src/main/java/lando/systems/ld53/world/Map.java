package lando.systems.ld53.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Map {

    private static final TmxMapLoader.Parameters LOADER_PARAMS = new TmxMapLoader.Parameters() {{
        generateMipMaps = true;
        textureMinFilter = Texture.TextureFilter.MipMap;
        textureMagFilter = Texture.TextureFilter.MipMap;
    }};

    private static class Layers {
        private MapLayer collision;
        private MapLayer objects;
    }

    private final TiledMap map;
    private final Layers layers;
    private final OrthoCachedTiledMapRenderer renderer;
    private final Array<PolylineMapObject> boundaries;
    private final Array<CircleMapObject> circleObjects;

    private final Vector2 v1 = new Vector2();
    private final Vector2 v2 = new Vector2();

    private PolylineMapObject exteriorBoundary;

    public Map(String fileName) {
        // load the map and create the renderer
        TmxMapLoader loader = new TmxMapLoader();
        this.map = loader.load(fileName, LOADER_PARAMS);
        this.renderer = new OrthoCachedTiledMapRenderer(map);
        this.renderer.setBlending(true);

        // load layers
        this.layers = new Layers();
        this.layers.collision = map.getLayers().get("collision");
        this.layers.objects   = map.getLayers().get("objects");
        if (layers.collision == null || layers.objects == null) {
            throw new GdxRuntimeException("Missing required map layer, required layers are: 'collision', 'objects'");
        }

        // load map objects
        MapObjects objects = layers.objects.getObjects();
        for (MapObject object : objects) {
            MapProperties props = object.getProperties();
            String type = (String) props.get("type");

            // TODO(brian) - spawn an entity for this object
            switch (type) {
                case "foo": {
                } break;
                case "bar": {
                } break;
                // ...
            }
        }

        // load collision objects
        // TODO(brian) - is it necessary to separate the 'exterior' polylines from any other ones?
        boolean missingExterior = true;
        MapObjects collisionObjects = layers.collision.getObjects();
        this.boundaries = collisionObjects.getByType(PolylineMapObject.class);
        for (PolylineMapObject boundary : boundaries) {
            MapProperties props = boundary.getProperties();
            String type = (String) props.get("type");
            if (type != null && type.equalsIgnoreCase("exterior")) {
                this.exteriorBoundary = boundary;
                missingExterior = false;
                break;
            }
        }
        if (missingExterior || boundaries.isEmpty()) {
            throw new GdxRuntimeException("Missing map boundary");
        }

        this.circleObjects = collisionObjects.getByType(CircleMapObject.class);
    }


    public void update(float delta) {
        // TODO - update game objects that require updating
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();

        // TODO - render polygon sprites for special ground regions
        // TODO - render game objects with the batch

        // render debug visualizations if debug flag is toggled
        if (Config.Debug.general) {
            ShapeDrawer shapes = Main.game.assets.shapes;
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            {
                for (int i = 0; i < boundaries.size; i++) {
                    Polyline boundary = boundaries.get(i).getPolyline();
                    float[] verts = boundary.getTransformedVertices();
                    int numVerts = boundary.getVertices().length;
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[i], verts[i + 1]);
                        v2.set(verts[(i + 2) % numVerts], verts[(i + 3) % numVerts]);
                        shapes.line(v1, v2, Color.MAGENTA, 6f);
                    }
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[i], verts[i + 1]);
                        v2.set(verts[(i + 2) % numVerts], verts[(i + 3) % numVerts]);
                        shapes.filledCircle(v1, 10f, Color.YELLOW);
                        shapes.filledCircle(v2, 10f, Color.YELLOW);
                    }
                }

                for (int i = 0; i < circleObjects.size; i++) {
                    Circle circle = circleObjects.get(i).getCircle();
                    v1.set(circle.x, circle.y);
                    shapes.filledCircle(v1, circle.radius, Color.BLUE);
                }
            }
            batch.end();
        }
    }

}
