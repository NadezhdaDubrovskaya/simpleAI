package com.rmw.machinelearning;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import processing.core.PVector;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.MONSTER_RADIUS;
import static com.rmw.machinelearning.Configuration.WIDTH;
import static java.util.Collections.emptyList;
import static org.jgrapht.alg.shortestpath.DijkstraShortestPath.findPathBetween;

class GameScreenGraph {

    private final Graph<PVector, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    private final List<ScreenObject> obstacles;

    GameScreenGraph(final List<ScreenObject> obstacles) {
        this.obstacles = obstacles;
        setUpGraph();
    }

    List<PVector> calculatePath(final PVector from, final PVector to) {
        try {
            return findPathBetween(graph, findClosestVertex(from), findClosestVertex(to)).getVertexList();
        } catch (final RuntimeException e) {
            return emptyList();
        }
    }

    /**
     * Determines if there is a free space in the passed direction based on the game screen graph
     * that is build in accordance to the obstacles on the map
     *
     * @param position  - coordinates of the player.
     *                  Note that they are going to be rounded to the closest vertex in the graph
     * @param direction - direction in which the presence of a free space will be checked
     * @return true in case there is an obstacle in that direction
     * false in case the path is clear in the chosen direction and no obstacle is found
     */
    boolean isThereAnObstacleNearBy(final PVector position, final Direction direction) {
        final PVector closestVertex = findClosestVertex(position);
        if (closestVertex == null) {
            // throw new IllegalArgumentException("Couldn't find closest vertex to the player position");
            return true;
        }
        float xCoordinate = closestVertex.x;
        float yCoordinate = closestVertex.y;
        switch (direction) {
            case LEFT:
                xCoordinate -= MONSTER_RADIUS*2;
                break;
            case RIGHT:
                xCoordinate += MONSTER_RADIUS*2;
                break;
            case TOP:
                yCoordinate -= MONSTER_RADIUS*2;
                break;
            case BOTTOM:
                yCoordinate += MONSTER_RADIUS*2;
                break;
            default:
                throw new InvalidParameterException("Unsupported direction " + direction);
        }
        return findVectorWithCoordinatesInGraph(xCoordinate, yCoordinate) == null;
    }

    void setUpGraph() {
        createAllPossibleVertexes();
        removeVertexesInsideAndNearObstacles();
        connectVertexes();
    }

    Set<PVector> getAllVertexes() {
        return graph.vertexSet();
    }

    private void createAllPossibleVertexes() {
        for (int x = MONSTER_RADIUS; x <= WIDTH - MONSTER_RADIUS; x = x + MONSTER_RADIUS) {
            for (int y = MONSTER_RADIUS; y <= HEIGHT - MONSTER_RADIUS; y = y + MONSTER_RADIUS) {
                graph.addVertex(new PVector(x, y));
            }
        }
    }

    private void removeVertexesInsideAndNearObstacles() {
        final List<PVector> toRemove = new ArrayList<>();
        obstacles.forEach(obstacle ->
                graph.vertexSet().forEach(vertex -> {
                    final boolean xInsideObstaclesBorder =
                            vertex.x > obstacle.getLeftBorder() - MONSTER_RADIUS
                                    && vertex.x < obstacle.getRightBorder() + MONSTER_RADIUS;
                    final boolean yInsideObstaclesBorder =
                            vertex.y > obstacle.getTopBorder() - MONSTER_RADIUS
                                    && vertex.y < obstacle.getBottomBorder() + MONSTER_RADIUS;
                    if (xInsideObstaclesBorder && yInsideObstaclesBorder) {
                        toRemove.add(vertex);
                    }
                }));
        toRemove.forEach(graph::removeVertex);
    }

    private void connectVertexes() {
        final Set<PVector> vertexes = graph.vertexSet();
        vertexes.forEach(vertex -> {
            final PVector rightNeighbor = findVectorWithCoordinatesInGraph(vertex.x + MONSTER_RADIUS, vertex.y);
            if (rightNeighbor != null) {
                graph.addEdge(vertex, rightNeighbor);
            }
            final PVector bottomNeighbor = findVectorWithCoordinatesInGraph(vertex.x, vertex.y + MONSTER_RADIUS);
            if (bottomNeighbor != null) {
                graph.addEdge(vertex, bottomNeighbor);
            }
        });
    }

    private PVector findVectorWithCoordinatesInGraph(final float x, final float y) {
        return graph.vertexSet().stream().filter(v -> v.x == x && v.y == y).findAny().orElse(null);
    }

    private PVector findClosestVertex(final PVector position) {
        int x = Math.round(position.x);
        int y = Math.round(position.y);
        x = x - x % MONSTER_RADIUS;
        y = y - y % MONSTER_RADIUS;
        return findVectorWithCoordinatesInGraph(x, y);
    }
}
