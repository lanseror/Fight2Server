package com.fight2.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import tiled.core.Map;
import tiled.core.MapLayer;
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
        final Vector<MapLayer> layers = map.getLayers();
        final TileLayer collisionLayer = (TileLayer) layers.get(0);
        TMXTilePoint currentPoint = new TMXTilePoint(startTile, null);
        final Queue<TMXTilePoint> queue = new LinkedList<TMXTilePoint>();
        queue.add(currentPoint);
        final Set<QuestTile> visitedTiles = new HashSet<QuestTile>();
        final QuestTile startRoadTile = new QuestTile(startTile.getRow(), startTile.getCol());
        visitedTiles.add(startRoadTile);
        while (!queue.isEmpty()) {
            currentPoint = queue.poll();
            final QuestTile pointTMXTile = currentPoint.getTmxTile();
            final int row = pointTMXTile.getRow();
            final int col = pointTMXTile.getCol();
            if (row == desTile.getRow() && col == desTile.getCol()) {
                break;
            }

            final int upRow = row - 1;
            final int upCol = col;
            final Tile upTile = collisionLayer.getTile(upRow, upCol);
            final QuestTile upRoadTile = createRoadTile(upRow, upCol);
            if (upTile == null && !visitedTiles.contains(upRoadTile)) {/* up */
                visit(upRow, upCol, currentPoint, queue);
                visitedTiles.add(upRoadTile);
            }
            final int leftRow = row;
            final int leftCol = col - 1;
            final Tile leftTile = collisionLayer.getTile(leftRow, leftCol);
            final QuestTile leftRoadTile = createRoadTile(leftRow, leftCol);
            if (leftTile == null && !visitedTiles.contains(leftRoadTile)) {/* left */
                visit(leftRow, leftCol, currentPoint, queue);
                visitedTiles.add(leftRoadTile);
            }
            final int rightRow = row;
            final int rightCol = col + 1;
            final Tile rightTile = collisionLayer.getTile(rightRow, rightCol);
            final QuestTile rightRoadTile = createRoadTile(rightRow, rightCol);
            if (rightTile == null && !visitedTiles.contains(rightRoadTile)) { /* right */
                visit(rightRow, rightCol, currentPoint, queue);
                visitedTiles.add(rightRoadTile);
            }
            final int downRow = row + 1;
            final int downCol = col;
            final Tile downTile = collisionLayer.getTile(downRow, downCol);
            final QuestTile downRoadTile = createRoadTile(downRow, downCol);
            if (downTile == null && !visitedTiles.contains(downRoadTile)) { /* down */
                visit(downRow, downCol, currentPoint, queue);
                visitedTiles.add(downRoadTile);
            }
            final int leftUpRow = row - 1;
            final int leftUpCol = col - 1;
            final Tile leftUpTile = collisionLayer.getTile(leftUpRow, leftUpCol);
            final QuestTile leftUpRoadTile = createRoadTile(leftUpRow, leftUpCol);
            if (leftUpTile == null && !visitedTiles.contains(leftUpRoadTile)) {/* left up */
                visit(leftUpRow, leftUpCol, currentPoint, queue);
                visitedTiles.add(leftUpRoadTile);
            }
            final int rightUpRow = row - 1;
            final int rightUpCol = col + 1;
            final Tile rightUpTile = collisionLayer.getTile(rightUpRow, rightUpCol);
            final QuestTile righUpRoadTile = createRoadTile(rightUpRow, rightUpCol);
            if (rightUpTile == null && !visitedTiles.contains(righUpRoadTile)) {/* right up */
                visit(rightUpRow, rightUpCol, currentPoint, queue);
                visitedTiles.add(righUpRoadTile);
            }
            final int leftDownRow = row + 1;
            final int leftDownCol = col - 1;
            final Tile leftDownTile = collisionLayer.getTile(leftDownRow, leftDownCol);
            final QuestTile leftDownRoadTile = createRoadTile(leftDownRow, leftDownCol);
            if (leftDownTile == null && !visitedTiles.contains(leftDownRoadTile)) { /* left down */
                visit(leftDownRow, leftDownCol, currentPoint, queue);
                visitedTiles.add(leftDownRoadTile);
            }
            final int rightDownRow = row + 1;
            final int rightDownCol = col + 1;
            final Tile rightDownTile = collisionLayer.getTile(rightDownRow, rightDownCol);
            final QuestTile rightDownRoadTile = createRoadTile(rightDownRow, rightDownCol);
            if (rightDownTile == null && !visitedTiles.contains(rightDownRoadTile)) { /* right down */
                visit(rightDownRow, rightDownCol, currentPoint, queue);
                visitedTiles.add(rightDownRoadTile);
            }
        }

        final Stack<QuestTile> stack = new Stack<QuestTile>();
        stack.push(currentPoint.getTmxTile());
        while (currentPoint.getPredecessor() != null) {
            currentPoint = currentPoint.getPredecessor();
            stack.push(currentPoint.getTmxTile());
            if (currentPoint.getTmxTile().equals(startTile)) {
                break;
            }
        }
        // System.out.println("test:" + stack.size());
        return stack;
    }

    private static QuestTile createRoadTile(final int row, final int col) {
        return new QuestTile(row, col);
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
