package com.checkers.web.views;

import com.checkers.web.domain.Score;
import com.checkers.web.service.ScoreService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class ScoresComponent extends VerticalLayout {

    private final ScoreService scoreService;

    private Grid<Score> scoreGrid = new Grid<>(Score.class);

    public ScoresComponent(ScoreService scoreService) {
        this.scoreService = scoreService;

        H3 header = new H3("Scores table with all winners");
        addClassName("list-view");
        setSizeFull();
        configureScoreGrid();
        updateScoreList();

        add(header, scoreGrid);
    }

    private void configureScoreGrid() {
        scoreGrid.getColumnByKey("winner").setVisible(false);
        scoreGrid.getColumnByKey("scoreId").setVisible(false);
        scoreGrid.setColumns("user", "advantage", "createdAt");
        scoreGrid.getColumnByKey("createdAt").setAutoWidth(true);
    }

    private void updateScoreList() {
        scoreGrid.setItems(scoreService.findAllOrderByAdvantage());
    }

}
