package com.strongholds.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationClip {
    Animation<TextureRegion> animation;
    float stateTime;

    public AnimationClip(Texture spriteSheet, int spriteSheetCols, int spriteSheetRows, float interval) {
        TextureRegion[][] spriteSheetIn2DArray = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / spriteSheetCols,
                spriteSheet.getHeight() / spriteSheetRows);

        TextureRegion[] spriteSheetIn1DArray = new TextureRegion[spriteSheetCols * spriteSheetRows];
        int index = 0;
        for (int i = 0; i < spriteSheetRows; i++) {
            for (int j = 0; j < spriteSheetCols; j++) {
                spriteSheetIn1DArray[index++] = spriteSheetIn2DArray[i][j];
            }
        }

        animation = new Animation<TextureRegion>(interval, spriteSheetIn1DArray);
        stateTime = 0.0f;
    }

    public void update(float deltaTime){
        stateTime += deltaTime;
    }

    TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime, true);
    }
}
