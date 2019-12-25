package com.strongholds.game.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.strongholds.game.GameSingleton.ObjectState;

import java.util.HashMap;
import java.util.Map;

public class Animator {
    Map<ObjectState, AnimationClip> clips;
    private ObjectState prevState;

    public Animator(AnimationClip idle, AnimationClip move, AnimationClip attack) {
        prevState = ObjectState.IDLING;
        clips = new HashMap<>();
        clips.put(ObjectState.IDLING, idle);
        clips.put(ObjectState.MOVING, move);
        clips.put(ObjectState.ATTACKING, attack);
    }

    public void update(ObjectState state, float deltaTime){
        AnimationClip clip = clips.get(state);
        clip.update(deltaTime);
        if (state != prevState){
            clip.reset();
        }
        prevState = state;
    }

    public TextureRegion getCurrentFrame(){
        return clips.get(prevState).getCurrentFrame();
    }
}
