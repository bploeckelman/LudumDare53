package lando.systems.ld53.physics.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TestGameArea {
    public Array<TestWallSegment> segments;

    public TestGameArea() {
        float x1 = Gdx.graphics.getWidth() * .1f;
        float x2 = Gdx.graphics.getWidth() * .9f;
        float y1 = Gdx.graphics.getHeight() * .1f;
        float y2 = Gdx.graphics.getHeight() * .9f;
        segments = new Array<>();
        segments.add(new TestWallSegment(x1, y1, x2, y1));
        segments.add(new TestWallSegment(x2, y1, x2, y2));
        segments.add(new TestWallSegment(x2, y2, x1, y2));
        segments.add(new TestWallSegment(x1, y2, x1, y1));
    }

    public void debugRender(SpriteBatch batch) {
        for (TestWallSegment segment : segments) {
            segment.collisionShape.debugRender(batch);
        }
    }
}
