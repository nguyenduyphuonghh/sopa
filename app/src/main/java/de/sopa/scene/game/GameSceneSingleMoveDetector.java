package de.sopa.scene.game;

import de.sopa.model.game.GameService;

import org.andengine.input.touch.detector.HoldDetector;


/**
 * @author  Raphael Schilling
 * @author  David Schilling - davejs92@gmail.com
 */
class GameSceneSingleMoveDetector implements HoldDetector.IHoldDetectorListener {

    private final float SWIPE_SENSITIVITY;
    private final float fieldStartX;
    private final GameService gameService;
    private float firstX;
    private float firstY;
    private float widthPerTile;
    private GameFieldView gameFieldView;
    private float fieldStartY;
    private boolean isMoved;

    GameSceneSingleMoveDetector(float startX, float startY, float widthPerTile, GameFieldView gameFieldView,
        GameService gameService) {

        this.fieldStartX = startX;
        this.fieldStartY = startY;
        this.widthPerTile = widthPerTile;
        this.gameFieldView = gameFieldView;
        isMoved = false;
        SWIPE_SENSITIVITY = widthPerTile * 0.1f;
        this.gameService = gameService;
    }

    @Override
    public void onHoldStarted(HoldDetector pHoldDetector, int pPointerID, float pHoldX, float pHoldY) {

        if (!gameService.solvedPuzzle()) {
            firstX = pHoldX;
            firstY = pHoldY;
            isMoved = false;
        }
    }


    @Override
    public void onHold(HoldDetector pHoldDetector, long pHoldTimeMilliseconds, int pPointerID, float pHoldX,
        float pHoldY) {

        if (!gameService.solvedPuzzle()) {
            int row;

            if (!isMoved) {
                if (pHoldX - firstX > widthPerTile) {
                    row = (int) ((firstY - fieldStartY) / widthPerTile);
                    gameFieldView.oneStep(true, row, 1);

                    isMoved = true;
                } else if (firstX - pHoldX > widthPerTile) {
                    row = (int) ((firstY - fieldStartY) / widthPerTile);
                    gameFieldView.oneStep(true, row, -1);
                    isMoved = true;
                } else if (pHoldY - firstY > widthPerTile) {
                    row = (int) ((firstX - fieldStartX) / widthPerTile);
                    gameFieldView.oneStep(false, row, 1);
                    isMoved = true;
                } else if (firstY - pHoldY > widthPerTile) {
                    row = (int) ((firstX - fieldStartX) / widthPerTile);
                    gameFieldView.oneStep(false, row, -1);
                    isMoved = true;
                }
            }
        }
    }


    @Override
    public void onHoldFinished(HoldDetector pHoldDetector, long pHoldTimeMilliseconds, int pPointerID, float pHoldX,
        float pHoldY) {

        if (!gameService.solvedPuzzle()) {
            int row;

            if (!isMoved) {
                if (Math.abs(pHoldX - firstX) > Math.abs(pHoldY - firstY)) {
                    if (pHoldX - firstX > SWIPE_SENSITIVITY) {
                        row = (int) ((firstY - fieldStartY) / widthPerTile);
                        gameFieldView.oneStep(true, row, 1);
                        isMoved = true;
                    } else if (firstX - pHoldX > SWIPE_SENSITIVITY) {
                        row = (int) ((firstY - fieldStartY) / widthPerTile);
                        gameFieldView.oneStep(true, row, -1);
                        isMoved = true;
                    }
                } else if (pHoldY - firstY > SWIPE_SENSITIVITY) {
                    row = (int) ((firstX - fieldStartX) / widthPerTile);
                    gameFieldView.oneStep(false, row, 1);
                    isMoved = true;
                } else if (firstY - pHoldY > SWIPE_SENSITIVITY) {
                    row = (int) ((firstX - fieldStartX) / widthPerTile);
                    gameFieldView.oneStep(false, row, -1);
                    isMoved = true;
                }
            }
        }
    }
}
