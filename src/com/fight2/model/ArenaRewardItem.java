package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ArenaRewardItem extends BaseEntity {
    private static final long serialVersionUID = -7485815241564723537L;
    private ArenaReward arenaReward;
    private ArenaRewardItemType type;
    private int amount;
    private CardTemplate cardTemplate;

    @ManyToOne
    @JoinColumn(name = "arena_reward_id")
    public ArenaReward getArenaReward() {
        return arenaReward;
    }

    public void setArenaReward(final ArenaReward arenaReward) {
        this.arenaReward = arenaReward;
    }

    public ArenaRewardItemType getType() {
        return type;
    }

    public void setType(final ArenaRewardItemType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn(name = "cardTemplate_id")
    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static enum ArenaRewardItemType {
        Card,
        ArenaTicket,
        Stamina
    }

}
