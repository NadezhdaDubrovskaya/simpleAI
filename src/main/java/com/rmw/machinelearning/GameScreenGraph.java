package com.rmw.machinelearning;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import processing.core.PApplet;
import processing.core.PVector;

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
    }

    void setUpGraph() {
        final long startTime = System.currentTimeMillis();
        createAllPossibleVertexes();
        removeVertexesInsideAndNearObstacles();
        connectVertexes();
        final long stopTime = System.currentTimeMillis();
        final long elapsedTime = stopTime - startTime;
        PApplet.println("GameScreenGraph took " + elapsedTime + " ms to be created");
    }

    List<PVector> calculatePath(final PVector from, final PVector to) {
        try {
            return findPathBetween(graph, findClosestVertex(from), findClosestVertex(to)).getVertexList();
        } catch (final RuntimeException e) {
            return emptyList();
        }
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
