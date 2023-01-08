package com.backend.se_project_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RecommenderServiceImpl implements RecommenderService{

    private int size = 12;
    private int distance[][] = new int[][] {
            {-1, -1, -1, -1, -1, -1, 4, 4, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, 2, -1, 3, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, 3, 6, -1, 3},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, 3, 5, 3},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 3, -1},
            {4, 2, -1, -1, -1, -1, -1, 4, 5, -1, -1, -1},
            {4, -1, -1, -1, -1, -1, 4, -1, 2, -1, -1, 5},
            {-1, 3, 3, -1, -1, -1, 5, 2, -1, -1, -1, -1},
            {-1, -1, 6, 3, -1, -1, -1, -1, -1, -1, -1, 5},
            {-1, -1, -1, 5, 4, 3, -1, -1, -1, -1, -1, -1},
            {-1, -1, 3, 3, -1, -1, -1, 5, -1, 5, -1, -1}};

    private int traffic[][] = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 0, 3, 0, 0, 1},
            {0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0},
            {0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0}};

    private int timeMatrix[][] = new int[size][size];

    private final StationService stationService;

    @Autowired
    public RecommenderServiceImpl(StationService stationService) {

        this.stationService = stationService;
    }


    private void calculateTimeMatrix(int distance[][], int traffic[][]) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                timeMatrix[i][j]  = (distance[i][j] + traffic[i][j] * distance[i][j]) * 2;
            }
        }
    }

    public String dijkstra(int srcId, int endId) {

        String path = "";
        int shortest[] = new int[size];
        int parents[] = new int[size];
        boolean visited[] = new boolean[size];

        srcId--;
        endId--;
        calculateTimeMatrix(distance, traffic);

        for (int i = 0; i < size; i++) {
            shortest[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }
        shortest[srcId] = 0;
        parents[srcId] = -1;

        for (int i = 0; i < size; i++) {
            int nearest = -1;
            int shortestDist = Integer.MAX_VALUE;
            for (int v = 0; v < size; v++) {
                if (!visited[v] && shortest[v] < shortestDist) {
                    nearest = v;
                    shortestDist = shortest[v];
                }
            }
            visited[nearest] = true;

            for (int v = 0; v < size; v++) {
                int edge = timeMatrix[nearest][v];

                if (edge > 0 && ((shortestDist + edge) < shortest[v])) {
                    parents[v] = nearest;
                    shortest[v] = shortestDist + edge;
                }
            }
        }

        path = getPath(parents, endId, path);
        path = "Recommended route: " + path.substring(4) + "\nEstimated time: " + Integer.toString(shortest[endId]) + " min";
        return path;
    }
    private String getPath(int[] parents, int endId, String path) {

            if (endId == -1) {
                return path;
            }
            path = getPath(parents, parents[endId], path);

            path = path + " -> " + getStationName(endId);
            return path;
    }

    private String getStationName(int endId) {
        endId++;
        String stationName = stationService.getStationNameById(endId);
        if(stationName.equals("")) {
            switch (endId) {
                case 1:
                    return "1st Street";
                case 2:
                    return "2nd Street";
                case 3:
                    return "3rd Street";
                default:
                    return Integer.toString(endId) + "th Street";
            }
        }
        else {
            return stationName;
        }
    }




}
