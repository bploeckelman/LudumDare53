package lando.systems.ld53.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class TemplateAwareTmxMapLoader extends TmxMapLoader {

    FileHandle tmxFile;

    @Override
    protected TiledMap loadTiledMap(FileHandle tmxFile, TmxMapLoader.Parameters parameter, ImageResolver imageResolver) {
        this.tmxFile = tmxFile;
        return super.loadTiledMap(tmxFile,parameter,imageResolver);
    }

    @Override
    protected void loadObject(TiledMap map, MapObjects objects, XmlReader.Element element, float heightInPixels) {
        if (element.getName().equals("object")) {
            if (!element.hasAttribute("template")) {
                super.loadObject(map, objects, element, heightInPixels);
                return;
            }

            String attrib = element.getAttribute("template");
            FileHandle template = getRelativeFileHandle(tmxFile, attrib);
            XmlReader.Element el = xml.parse(template);
            Array<XmlReader.Element> objectChildren = el.getChildrenByName("object");
            for (XmlReader.Element obj : objectChildren) {
                obj.setAttribute("x", element.getAttribute("x"));
                obj.setAttribute("y", element.getAttribute("y"));
                obj.setAttribute("id", element.getAttribute("id"));
                super.loadObject(map, objects, obj, heightInPixels);
                return;
            }
        }
    }

}
