package com.strongholds.game.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationClip {
    Animation<TextureRegion> animation;
    float stateTime;

    /**
     *  Constructor
      * @param spriteSheet      texture consisting of multiple smaller frames that make up the animation
     * @param spriteSheetCols   number of columns in the sprite sheet
     * @param spriteSheetRows   number of rows in the sprite sheet
     * @param frames            number of relevant frames in the sprite sheet (the ones that are not empty)
     * @param interval          time at which frames should be changed
     */
    public AnimationClip(Texture spriteSheet, int spriteSheetCols, int spriteSheetRows, int frames, float interval) {
        TextureRegion[][] spriteSheetIn2DArray = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / spriteSheetCols,
                spriteSheet.getHeight() / spriteSheetRows);

        TextureRegion[] spriteSheetIn1DArray = new TextureRegion[frames];
        int index = 0;
        for (int i = 0; i < spriteSheetRows; i++) {
            for (int j = 0; j < spriteSheetCols; j++) {
                if (i * spriteSheetCols + j + 1 > frames) break;
                spriteSheetIn1DArray[index++] = spriteSheetIn2DArray[i][j];
            }
        }

        animation = new Animation<TextureRegion>(interval, spriteSheetIn1DArray);
        stateTime = 0.0f;
    }

    /**
     *
     * @param deltaTime
     */
    public void update(float deltaTime){
        stateTime += deltaTime;
    }

    TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime, true);
    }
}
