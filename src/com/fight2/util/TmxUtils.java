package com.fight2.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import tiled.core.Map;
import tiled.io.TMXMapReader;

public class TmxUtils {
    private static final Map MAP = loadMap();

    private static Map loadMap() {
        try {
            final File mapFile = getFileFromResources("fight2.tmx");
            final Map map = new TMXMapReader().readMap(mapFile.getAbsolutePath());
            return map;
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static File getFileFromResources(final String filename) throws URISyntaxException {
        // Need to load files with their absolute paths, since they might refer to
        // tileset files that are expected to be in the same directory as the TMX file.
        final URL fileUrl = TmxUtils.class.getResource(filename);
        final File mapFile = new File(fileUrl.toURI());
        return mapFile;
    }

    public static Map getMap() {
        return MAP;
    }
}
