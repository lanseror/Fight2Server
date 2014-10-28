package com.fight2.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserQuestInfoDao;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardTemplate;
import com.fight2.model.User;
import com.fight2.model.UserQuestInfo;
import com.fight2.model.quest.QuestTile;
import com.fight2.model.quest.QuestTile.TileItem;
import com.fight2.util.QuestUtils;
import com.fight2.util.SummonHelper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/quest")
public class QuestAction extends BaseAction {
    private static final long serialVersionUID = -9079446757823452597L;
    @Autowired
    private UserQuestInfoDao userQuestInfoDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SummonHelper summonHelper;
    private int id;
    private int row;
    private int col;

    @Action(value = "go", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String go() {
        final Map<String, Object> response = Maps.newHashMap();
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(currentUser.getId());
        UserQuestInfo userQuestInfo = user.getQuestInfo();
        if (userQuestInfo == null) {
            userQuestInfo = new UserQuestInfo();
            userQuestInfo.setUser(user);
            userQuestInfoDao.add(userQuestInfo);
        }
        userQuestInfo.setRow(row);
        userQuestInfo.setCol(col);
        userQuestInfoDao.update(userQuestInfo);

        response.put("status", 0);
        final List<QuestTile> treasures = QuestUtils.getUserData(currentUser.getId());
        final Iterator<QuestTile> it = treasures.iterator();
        int treasureIndex = 0;
        while (it.hasNext()) {
            final QuestTile treasure = it.next();
            if (treasure.getRow() == row && treasure.getCol() == col) {
                response.put("status", 1);
                final TileItem tileItem = treasure.getItem();
                response.put("treasureItem", tileItem);
                if (tileItem == TileItem.Card) {
                    summonTreasure(response, user);
                }
                response.put("treasureIndex", treasureIndex);
                it.remove();
                break;
            }
            treasureIndex++;
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(response));
        return SUCCESS;
    }

    public void summonTreasure(final Map<String, Object> response, final User user) {
        final CardTemplate cardTemplate = summonHelper.summon();
        final String avatar = cardTemplate.getAvatars().get(0).getUrl();
        final String image = cardTemplate.getThumbImages().get(0).getUrl();

        final Card card = new Card();
        card.setAtk(cardTemplate.getAtk());
        card.setAvatar(avatar);
        card.setHp(cardTemplate.getHp());
        card.setImage(image);
        card.setName(cardTemplate.getName());
        card.setStar(cardTemplate.getStar());
        card.setCardTemplate(cardTemplate);
        card.setStatus(CardStatus.InStoreroom);
        card.setUser(user);
        cardDao.add(card);

        final Card cardVo = new Card();
        cardVo.setId(card.getId());
        cardVo.setAtk(card.getAtk());
        cardVo.setAvatar(avatar);
        cardVo.setHp(card.getHp());
        cardVo.setImage(image);
        cardVo.setName(card.getName());
        cardVo.setSkill(card.getSkill());
        cardVo.setStar(card.getStar());
        final CardTemplate cardTemplateVo = new CardTemplate();
        cardTemplateVo.setId(cardTemplate.getId());
        cardVo.setCardTemplate(cardTemplateVo);
        response.put("card", cardVo);
        final List<Card> cards = cardDao.listByUser(user);
        user.setCardCount(cards.size());
        userDao.update(user);
    }

    @Action(value = "treasure", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getUserQuestTreasure() {
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final List<QuestTile> treasures = QuestUtils.getUserData(currentUser.getId());
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(treasures));
        return SUCCESS;
    }

    public UserQuestInfoDao getUserQuestInfoDao() {
        return userQuestInfoDao;
    }

    public void setUserQuestInfoDao(final UserQuestInfoDao userQuestInfoDao) {
        this.userQuestInfoDao = userQuestInfoDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public SummonHelper getSummonHelper() {
        return summonHelper;
    }

    public void setSummonHelper(final SummonHelper summonHelper) {
        this.summonHelper = summonHelper;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
