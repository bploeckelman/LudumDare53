package lando.systems.ld53.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.utils.TemplateAwareTmxMapLoader;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Map {

    private static class Layers {
        private MapLayer collision;
        private MapLayer objects;
    }

    private final TiledMap map;
    private final Layers layers;
    private final OrthoCachedTiledMapRenderer renderer;
    private final Array<PolygonMapObject> polygonObjects;
    private final Array<PolylineMapObject> polylineObjects;
    private final Array<EllipseMapObject> ellipseObjects;

    public final Array<Vector2> polylineEndpoints;
    public final Array<Circle> circles;

    private final Vector2 v1 = new Vector2();
    private final Vector2 v2 = new Vector2();

    public Map(String fileName) {
        // load the map and create the renderer
        TmxMapLoader loader = new TemplateAwareTmxMapLoader();
        this.map = loader.load(fileName);
        this.renderer = new OrthoCachedTiledMapRenderer(map);
        this.renderer.setBlending(true);

        // load layers
        this.layers = new Layers();
        this.layers.collision = map.getLayers().get("collision");
        this.layers.objects   = map.getLayers().get("objects");
        if (layers.collision == null || layers.objects == null) {
            throw new GdxRuntimeException("Missing required map layer, required layers are: 'collision', 'objects'");
        }

        // load collision objects
        MapObjects collisionObjects = layers.collision.getObjects();
        this.polygonObjects = collisionObjects.getByType(PolygonMapObject.class);
        this.polylineObjects = collisionObjects.getByType(PolylineMapObject.class);

        // load map objects
        this.ellipseObjects = new Array<EllipseMapObject>();
        MapObjects objects = layers.objects.getObjects();
        for (MapObject object : objects) {
            MapProperties props = object.getProperties();

            // TODO(brian) - spawn an entity for this object based on 'type' property?
            if (object instanceof EllipseMapObject) {
                ellipseObjects.add((EllipseMapObject) object);
            }
        }

        // extract 'ellipse' (actually circle) data for easy access by the collision system
        this.circles = new Array<Circle>();
        for (int i = 0; i < ellipseObjects.size; i++) {
            Ellipse ellipse = ellipseObjects.get(i).getEllipse();

            // position in tiled is bottom left, adjust so it's center
            float radius = Math.max(ellipse.width, ellipse.height) / 2f;
            v1.set(ellipse.x + radius, ellipse.y + radius);

            Circle circle = new Circle(v1, radius);
            circles.add(circle);
        }

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
                // draw polylines
                for (int i = 0; i < polylineObjects.size; i++) {
                    Polyline polyline = polylineObjects.get(i).getPolyline();
                    float[] verts = polyline.getTransformedVertices();
                    int numVerts = polyline.getVertices().length;
                    for (int v = 0; v < numVerts; v += 2) {
                        v1.set(verts[v], verts[v + 1]);
                        v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
                        shapes.line(v1, v2, Color.MAGENTA, 6f);
                    }
                }

                // draw polyline endpoints
                for (int i = 0; i < polylineEndpoints.size; i += 2) {
                    Vector2 p1 = polylineEndpoints.get(i);
                    Vector2 p2 = polylineEndpoints.get(i+1);
                    shapes.filledCircle(p1, 10f, Color.GOLD);
                    shapes.filledCircle(p2, 10f, Color.GOLD);
                }

                // draw polygons (unused currently)
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

                // draw circle objects
                for (Circle circle : circles) {
                    shapes.filledCircle(circle.x, circle.y, circle.radius, Color.SKY);
                }
            }
            batch.end();
        }
    }

}
