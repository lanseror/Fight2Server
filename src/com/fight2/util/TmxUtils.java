package com.fight2.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.io.TMXMapReader;

import com.fight2.model.quest.QuestTile;

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

    public static Stack<QuestTile> findPath(final QuestTile startTile, final QuestTile desTile) {
        final tiled.core.Map map = TmxUtils.getMap();
        final TileLayer tmxLayer = (TileLayer) map.getLayers().get(0);
        TMXTilePoint currentPoint = new TMXTilePoint(startTile, null);
        final Queue<TMXTilePoint> queue = new LinkedList<TMXTilePoint>();
        queue.add(currentPoint);
        final Set<Tile> visitedTiles = new HashSet<Tile>();
        final Tile startTMXTile = tmxLayer.getTile(startTile.getRow(), startTile.getCol());
        visitedTiles.add(startTMXTile);
        while (!queue.isEmpty()) {
            currentPoint = queue.poll();
            final QuestTile pointTMXTile = currentPoint.getTmxTile();
            final int row = pointTMXTile.getRow();
            final int col = pointTMXTile.getCol();
            if (pointTMXTile == desTile) {
                break;
            }

            final Tile upTile = tmxLayer.getTile(row - 1, col);
            if (upTile == null && !visitedTiles.contains(upTile)) {/* up */
                visit(row - 1, col, currentPoint, queue);
                visitedTiles.add(upTile);
            }
            final Tile leftTile = tmxLayer.getTile(row, col - 1);
            if (leftTile == null && !visitedTiles.contains(leftTile)) {/* left */
                visit(row, col - 1, currentPoint, queue);
                visitedTiles.add(leftTile);
            }
            final Tile rightTile = tmxLayer.getTile(row, col + 1);
            if (rightTile == null && !visitedTiles.contains(rightTile)) { /* right */
                visit(row, col + 1, currentPoint, queue);
                visitedTiles.add(rightTile);
            }
            final Tile downTile = tmxLayer.getTile(row + 1, col);
            if (downTile != null && !visitedTiles.contains(downTile)) { /* down */
                visit(row + 1, col, currentPoint, queue);
                visitedTiles.add(downTile);
            }
            final Tile leftUpTile = tmxLayer.getTile(row - 1, col - 1);
            if (leftUpTile != null && !visitedTiles.contains(leftUpTile)) {/* left up */
                visit(row - 1, col - 1, currentPoint, queue);
                visitedTiles.add(leftUpTile);
            }
            final Tile rightUpTile = tmxLayer.getTile(row - 1, col + 1);
            if (rightUpTile != null && !visitedTiles.contains(rightUpTile)) {/* right up */
                visit(row - 1, col + 1, currentPoint, queue);
                visitedTiles.add(rightUpTile);
            }

            final Tile leftDownTile = tmxLayer.getTile(row + 1, col - 1);
            if (leftDownTile != null && !visitedTiles.contains(leftDownTile)) { /* left down */
                visit(row + 1, col - 1, currentPoint, queue);
                visitedTiles.add(leftDownTile);
            }

            final Tile rightDownTile = tmxLayer.getTile(row + 1, col + 1);
            if (rightDownTile != null && !visitedTiles.contains(rightDownTile)) { /* right down */
                visit(row + 1, col + 1, currentPoint, queue);
                visitedTiles.add(rightDownTile);
            }

        }

        final Stack<QuestTile> stack = new Stack<QuestTile>();
        stack.push(currentPoint.getTmxTile());
        while (currentPoint.getPredecessor() != null) {
            currentPoint = currentPoint.getPredecessor();
            stack.push(currentPoint.getTmxTile());
            if (currentPoint.getTmxTile() == startTile) {
                break;
            }
        }

        return stack;
    }

    private static void visit(final int row, final int col, final TMXTilePoint predecessor, final Queue<TMXTilePoint> queue) {
        final QuestTile pointTile = new QuestTile();
        pointTile.setRow(row);
        pointTile.setCol(col);
        final TMXTilePoint visitPoint = new TMXTilePoint(pointTile, predecessor);
        queue.add(visitPoint);
    }

    private static class TMXTilePoint {
        private final QuestTile tmxTile;
        private final TMXTilePoint predecessor;

        public TMXTilePoint(final QuestTile tmxTile, final TMXTilePoint predecessor) {
            super();
            this.tmxTile = tmxTile;
            this.predecessor = predecessor;
        }

        public QuestTile getTmxTile() {
            return tmxTile;
        }

        public TMXTilePoint getPredecessor() {
            return predecessor;
        }

    }
}
