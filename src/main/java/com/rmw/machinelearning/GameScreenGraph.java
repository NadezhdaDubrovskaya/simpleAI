package com.rmw.machinelearning;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import processing.core.PVector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.MONSTER_RADIUS;
import static com.rmw.machinelearning.Configuration.WIDTH;

class GameScreenGraph {

    private final Graph<PVector, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    private final List<ScreenObject> obstacles = Obstacles.getInstance(null).getObstacles();

    GameScreenGraph() {
        createAllPossibleVertexes();
        removeVertexesInsideAndNearObstacles();
        connectVertexes();
    }

    List<PVector> calculatePath(final PVector from, final PVector to) {
        throw new NotImplementedException();
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

    }
}
