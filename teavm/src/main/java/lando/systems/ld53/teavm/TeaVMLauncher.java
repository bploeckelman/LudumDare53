package lando.systems.ld53.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.web.WebApplication;
import com.github.xpenatan.gdx.backends.web.WebApplicationConfiguration;
import lando.systems.ld53.Config;
import lando.systems.ld53.Main;

/**
 * Launches the TeaVM/HTML application.
 * <br>
 * It may be important to note that if the TeaVM page is loaded from a URL with parameters,
 * that is, with a '?' sign after ".html" or some other file extension, then loading any
 * assets might not work right now. This is especially true when loading via IntelliJ IDEA's
 * built-in web server, which will default to adding on URL parameters that can be removed.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        WebApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        // change these to both 0 to use all available space, or both -1 for the canvas size.
        config.width = Config.Screen.window_width;
        config.height = Config.Screen.window_height;
        new WebApplication(new Main(), config);
    }
}
