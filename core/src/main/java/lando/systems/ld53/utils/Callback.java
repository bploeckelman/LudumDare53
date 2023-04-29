package lando.systems.ld53.utils;

@FunctionalInterface
public interface Callback {
    void run(Object... params);
}
