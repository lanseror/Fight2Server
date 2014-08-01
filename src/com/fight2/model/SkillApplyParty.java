package com.fight2.model;

public enum SkillApplyParty {
    Self(false),
    Opponent(false),
    Leader(false),
    OpponentLeader(false),
    SelfAll(true),
    OpponentAll(true);

    private final boolean isIndividual;

    private SkillApplyParty(final boolean isIndividual) {
        this.isIndividual = isIndividual;
    }

    public boolean isIndividual() {
        return isIndividual;
    }

}
