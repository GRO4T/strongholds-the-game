package com.strongholds.game.gameobject;

public interface IGameObject extends IReadOnlyGameObject{
    void setId(String id);
    void gotHit(int damage);
}
