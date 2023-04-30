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
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld53.Assets;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;
import lando.systems.ld53.entities.Goal;
import lando.systems.ld53.entities.Peg;
import lando.systems.ld53.entities.WallSegment;
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
    private final Array<RectangleMapObject> rectangleObjects;

    public final Array<Vector2> polylineEndpoints;
    public final Array<Circle> circles;
    public final Array<Peg> pegs;
    public final Array<Goal> goals;

    public Array<WallSegment> wallSegments;

    private final Vector2 v1 = new Vector2();
    private final Vector2 v2 = new Vector2();

    public Map(String fileName) {
        // load the map and create the renderer
        TmxMapLoader loader = new TemplateAwareTmxMapLoader();
        this.map = loader.load(fileName);
        this.renderer = new OrthoCachedTiledMapRenderer(map);
        this.renderer.setBlending(true);

        this.pegs = new Array<>();
        this.goals = new Array<>();

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
        this.ellipseObjects = new Array<>();
        this.rectangleObjects = new Array<>();
        MapObjects objects = layers.objects.getObjects();
        for (MapObject object : objects) {
            MapProperties props = object.getProperties();
            if (object instanceof EllipseMapObject) {
                ellipseObjects.add((EllipseMapObject) object);
            } else if (object instanceof RectangleMapObject) {
                rectangleObjects.add((RectangleMapObject) object);
            }
        }

        // extract 'ellipse' (actually circle) data for easy access by the collision system
        // and instantiate entities that are
        this.circles = new Array<Circle>();
        for (int i = 0; i < ellipseObjects.size; i++) {
            EllipseMapObject ellipseObject = ellipseObjects.get(i);
            Ellipse ellipse = ellipseObject.getEllipse();

            // position in tiled is bottom left, adjust so it's center
            float radius = Math.max(ellipse.width, ellipse.height) / 2f;
            v1.set(ellipse.x + radius, ellipse.y + radius);

            Circle circle = new Circle(v1, radius);
            circles.add(circle);

            Assets assets = Main.game.assets;
            MapProperties props = ellipseObject.getProperties();
            String type = props.get("type", "", String.class);
            if (type.equals("peg")) {
                Peg peg = new Peg(assets, circle.x, circle.y);
                pegs.add(peg);
            }
        }

        // instantiate entities from the rectangle objects
        for (RectangleMapObject rectObj : rectangleObjects) {
            Rectangle rect = rectObj.getRectangle();
            MapProperties props = rectObj.getProperties();
            String type = props.get("type", "", String.class);
            if (type.equals("goal")) {
                Goal goal = new Goal(rectObj);
                goals.add(goal);
            }
        }

        // extract polyline endpoints for easy access by the collision system
        this.polylineEndpoints = new Array<>();
        this.wallSegments = new Array<>();
        for (int i = 0; i < polylineObjects.size; i++) {
            Polyline polyline = polylineObjects.get(i).getPolyline();
            float[] verts = polyline.getTransformedVertices();

            int numComponents = polyline.getVertices().length;
            for (int c = 0; c < numComponents; c += 2) {
                v1.set(verts[c], verts[c + 1]);
                polylineEndpoints.add(v1.cpy());
            }
        }

        for (int i = 0; i < polylineEndpoints.size; i += 2) {
            v1.set(polylineEndpoints.get(i));
            v2.set(polylineEndpoints.get(i+1));
            wallSegments.add(new WallSegment(v1.x, v1.y, v2.x, v2.y));
        }
    }

    public void update(float delta) {
        for (Peg peg : pegs) {
            peg.update(delta);
        }

        for (Goal goal : goals) {
            goal.update(delta);
        }
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
//                for (int i = 0; i < polylineObjects.size; i++) {
//                    Polyline polyline = polylineObjects.get(i).getPolyline();
//                    float[] verts = polyline.getTransformedVertices();
//                    int numVerts = polyline.getVertices().length;
//                    for (int v = 0; v < numVerts; v += 2) {
//                        v1.set(verts[v], verts[v + 1]);
//                        v2.set(verts[(v + 2) % numVerts], verts[(v + 3) % numVerts]);
//                        shapes.line(v1, v2, Color.MAGENTA, 6f);
//                    }
//                }

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
                shapes.setColor(1f, 1f, 0f, 0.25f);
                for (Circle circle : circles) {
                    shapes.filledCircle(circle.x, circle.y, circle.radius);
                }
                shapes.setColor(Color.WHITE);
            }
            batch.end();
        }
    }

}
