package com.fight2.model;

public enum SkillApplyParty {
    Self(false),
    Opponnent(false),
    Leader(false),
    OpponnentLeader(false),
    SelfAll(true),
    OpponnentAll(true);

    private final boolean isIndividual;

    private SkillApplyParty(final boolean isIndividual) {
        this.isIndividual = isIndividual;
    }

    public boolean isIndividual() {
        return isIndividual;
    }

}
