package lando.systems.ld53;

public class Config {

    public static final String window_title = "Crunch Time: Ship It!";

    public static class Debug {
        public static boolean general = false;
        public static boolean shaders = false;
        public static boolean ui = false;
        public static boolean show_launch_screen = false;
    }

    public static class Screen {
        public static final int window_width = 1280;
        public static final int window_height = 720;
        public static final int framebuffer_width = window_width;
        public static final int framebuffer_height = window_height;
    }

}
