package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class User extends BaseEntity {
    public static final int USER_CARDPACK_SIZE = 80;
    private static final long serialVersionUID = -4914598284011248917L;
    private String installUUID;
    private String name;
    private String username;
    private String password;
    private String avatar;
    private int cardCount;
    private int level = 1;
    private PartyInfo partyInfo;
    private UserStoreroom storeroom;
    private UserQuestInfo questInfo;
    private List<Card> cards;
    private boolean isDisabled;
    private UserType type;
    private Guild guild;
    private int guildContribution;
    private int salary;
    private int glory;

    public String getInstallUUID() {
        return installUUID;
    }

    public void setInstallUUID(final String installUUID) {
        this.installUUID = installUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(final int cardCount) {
        this.cardCount = cardCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @OneToOne(mappedBy = "user")
    public PartyInfo getPartyInfo() {
        return partyInfo;
    }

    public void setPartyInfo(final PartyInfo partyInfo) {
        this.partyInfo = partyInfo;
    }

    @OneToOne(mappedBy = "user")
    public UserStoreroom getStoreroom() {
        return storeroom;
    }

    public void setStoreroom(final UserStoreroom storeroom) {
        this.storeroom = storeroom;
    }

    @OneToOne(mappedBy = "user")
    public UserQuestInfo getQuestInfo() {
        return questInfo;
    }

    public void setQuestInfo(final UserQuestInfo questInfo) {
        this.questInfo = questInfo;
    }

    @OneToMany(mappedBy = "user")
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    public int getGuildContribution() {
        return guildContribution;
    }

    public void setGuildContribution(final int guildContribution) {
        this.guildContribution = guildContribution;
    }

    public UserType getType() {
        return type;
    }

    public void setType(final UserType type) {
        this.type = type;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(final int salary) {
        this.salary = salary;
    }

    public int getGlory() {
        return glory;
    }

    public void setGlory(final int glory) {
        this.glory = glory;
    }

    public static int getUserCardpackSize() {
        return USER_CARDPACK_SIZE;
    }

    public enum UserType {
        User,
        ArenaGuardian,
        QuestNpc
    }

}
