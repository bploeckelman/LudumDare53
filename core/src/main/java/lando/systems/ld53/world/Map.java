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
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
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
    private final Array<PolygonMapObject> polygonObjects;
    private final Array<PolylineMapObject> polylineObjects;
    private final Array<CircleMapObject> circleObjects;

    public final Array<Vector2> polylineEndpoints;

    private final Vector2 v1 = new Vector2();
    private final Vector2 v2 = new Vector2();

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
        MapObjects collisionObjects = layers.collision.getObjects();
        this.polygonObjects = collisionObjects.getByType(PolygonMapObject.class);
        this.polylineObjects = collisionObjects.getByType(PolylineMapObject.class);
        this.circleObjects = collisionObjects.getByType(CircleMapObject.class);

        // extract polyline endpoints for easy access by the collision system
        this.polylineEndpoints = new Array<>();
        for (int i = 0; i < polylineObjects.size; i++) {
            Polyline polyline = polylineObjects.get(i).getPolyline();
            float[] verts = polyline.getTransformedVertices();
            int numVerts = polyline.getVertices().length;
            for (int v = 0; v < numVerts; v += 2) {
                v1.set(verts[v], verts[v + 1]);
                v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
                polylineEndpoints.add(v1.cpy(), v2.cpy());
            }
        }
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
                for (int i = 0; i < polylineObjects.size; i++) {
                    Polyline polyline = polylineObjects.get(i).getPolyline();
                    float[] verts = polyline.getTransformedVertices();
                    int numVerts = polyline.getVertices().length;
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[v], verts[v + 1]);
                        v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
                        shapes.line(v1, v2, Color.MAGENTA, 6f);
                    }
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[v], verts[v + 1]);
                        v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
                        shapes.filledCircle(v1, 10f, Color.GOLD);
                        shapes.filledCircle(v2, 10f, Color.GOLD);
                    }
                }

                for (int i = 0; i < polygonObjects.size; i++) {
                    Polygon polygon = polygonObjects.get(i).getPolygon();
                    float[] verts = polygon.getTransformedVertices();
                    int numVerts = polygon.getVertexCount();
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[v], verts[v + 1]);
                        v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
                        shapes.line(v1, v2, Color.VIOLET, 6f);
                    }
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[v], verts[v + 1]);
                        v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
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
