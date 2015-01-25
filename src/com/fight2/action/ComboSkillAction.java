package com.fight2.action;

import java.io.File;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.common.ImageCommon;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.CardTemplateDao;
import com.fight2.dao.ComboSkillCardDao;
import com.fight2.dao.ComboSkillDao;
import com.fight2.dao.SkillOperationDao;
import com.fight2.dao.UserDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.ComboSkill;
import com.fight2.model.ComboSkillCard;
import com.fight2.model.SkillApplyParty;
import com.fight2.model.SkillOperation;
import com.fight2.model.SkillPointAttribute;
import com.fight2.model.SkillType;
import com.google.common.collect.Lists;

@Namespace("/combo-skill")
public class ComboSkillAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ComboSkillDao comboSkillDao;
    @Autowired
    private ComboSkillCardDao comboSkillCardDao;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private SkillOperationDao skillOperationDao;
    private ComboSkill comboSkill;
    private List<ComboSkill> datas;
    private int id;
    private int cardTemplateId;
    private int[] signs;
    private int[] points;
    private SkillType[] skillTypes;
    private SkillPointAttribute[] skillPointAttributes;
    private SkillApplyParty[] skillApplyPartys;
    private int[] cardIds;
    private List<Card> cards;
    private List<Card> savedCards;
    private File icon;
    private String iconFileName;

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "combo_skill_form.ftl") })
    public String add() {
        loadCardData();
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "combo_skill_form.ftl") })
    public String edit() {
        comboSkill = comboSkillDao.get(id);
        loadCardData();
        final List<ComboSkillCard> comboSkillCards = comboSkill.getComboSkillCards();
        savedCards = Lists.newArrayList();
        for (final ComboSkillCard comboSkillCard : comboSkillCards) {
            final CardTemplate cardTemplate = comboSkillCard.getCardTemplate();
            final Card card = new Card();
            card.setId(cardTemplate.getId());
            card.setName(cardTemplate.getName());
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, 1, cardTemplate);
            card.setAvatar(avatarObj.getUrl());
            savedCards.add(card);
        }
        while (savedCards.size() < 4) {
            final Card card = new Card();
            savedCards.add(card);
        }
        return SUCCESS;
    }

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "combo_skill_list.ftl") })
    public String list() {
        datas = comboSkillDao.list();
        return SUCCESS;
    }

    private void loadCardData() {
        final List<CardTemplate> cardTemplates = cardTemplateDao.list();
        cards = Lists.newArrayList();
        for (final CardTemplate cardTemplate : cardTemplates) {
            final Card card = new Card();
            card.setId(cardTemplate.getId());
            card.setName(cardTemplate.getName());
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, 1, cardTemplate);
            card.setAvatar(avatarObj.getUrl());
            cards.add(card);
        }
    }

    @Action(value = "save",
            interceptorRefs = {
                    @InterceptorRef(value = "fileUpload", params = { "allowedExtensions ", ".jpg,.png", "allowedTypes",
                            "image/png,image/jpeg,image/pjpeg" }), @InterceptorRef("tokenSession"), @InterceptorRef("basicStack") }, results = {
                    @Result(name = SUCCESS, location = "list", type = "redirect"), @Result(name = INPUT, location = "combo_skill_list.ftl") })
    public String save() {
        if (comboSkill.getId() == BaseEntity.EMPTY_ID) {
            createSave();
        } else {
            editSave();
        }
        for (int i = 0; i < points.length; i++) {
            final SkillOperation operation = new SkillOperation();
            final SkillType skillType = skillTypes[i];
            final int sign = (skillType == SkillType.Defence ? 1 : signs[i]);
            final int point = points[i];
            final SkillPointAttribute skillPointAttribute = skillPointAttributes[i];
            final SkillApplyParty skillApplyParty = skillApplyPartys[i];
            operation.setSign(sign);
            operation.setSkillType(skillType);
            operation.setPoint(point);
            operation.setSkillPointAttribute(skillPointAttribute);
            operation.setSkillApplyParty(skillApplyParty);
            operation.setComboSkill(comboSkill);
            skillOperationDao.add(operation);
        }
        for (int i = 0; i < cardIds.length; i++) {
            final int cardId = cardIds[i];
            final CardTemplate cardTemplate = cardTemplateDao.get(cardId);
            final ComboSkillCard comboSkillCard = new ComboSkillCard();
            comboSkillCard.setCardTemplate(cardTemplate);
            comboSkillCard.setComboSkill(comboSkill);
            comboSkillCardDao.add(comboSkillCard);
        }
        if (icon != null) {
            final ImageCommon imageCommon = new ImageCommon(icon, iconFileName);
            final CardImage imageVo = imageCommon.doUpload();
            comboSkill.setIcon(imageVo.getUrl());
            comboSkillDao.update(comboSkill);
        }
        return SUCCESS;
    }

    private void createSave() {
        comboSkillDao.add(comboSkill);
    }

    private void editSave() {
        final ComboSkill skillPo = comboSkillDao.get(comboSkill.getId());
        skillPo.setName(comboSkill.getName());
        skillPo.setProbability(comboSkill.getProbability());
        comboSkillDao.update(skillPo);

        // Clear operations first.
        final List<SkillOperation> skillOperations = skillPo.getOperations();
        for (final SkillOperation skillOperation : skillOperations) {
            skillOperationDao.delete(skillOperation);
        }
        final List<ComboSkillCard> comboSkillCards = skillPo.getComboSkillCards();
        for (final ComboSkillCard comboSkillCard : comboSkillCards) {
            comboSkillCardDao.delete(comboSkillCard);
        }

        comboSkill = skillPo;

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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getCardTemplateId() {
        return cardTemplateId;
    }

    public void setCardTemplateId(final int cardTemplateId) {
        this.cardTemplateId = cardTemplateId;
    }

    public int[] getSigns() {
        return signs;
    }

    public void setSigns(final int[] signs) {
        this.signs = signs;
    }

    public int[] getPoints() {
        return points;
    }

    public void setPoints(final int[] points) {
        this.points = points;
    }

    public SkillPointAttribute[] getSkillPointAttributes() {
        return skillPointAttributes;
    }

    public void setSkillPointAttributes(final SkillPointAttribute[] skillPointAttributes) {
        this.skillPointAttributes = skillPointAttributes;
    }

    public SkillApplyParty[] getSkillApplyPartys() {
        return skillApplyPartys;
    }

    public void setSkillApplyPartys(final SkillApplyParty[] skillApplyPartys) {
        this.skillApplyPartys = skillApplyPartys;
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public SkillOperationDao getSkillOperationDao() {
        return skillOperationDao;
    }

    public void setSkillOperationDao(final SkillOperationDao skillOperationDao) {
        this.skillOperationDao = skillOperationDao;
    }

    public SkillType[] getSkillTypes() {
        return skillTypes;
    }

    public void setSkillTypes(final SkillType[] skillTypes) {
        this.skillTypes = skillTypes;
    }

    public ComboSkillDao getComboSkillDao() {
        return comboSkillDao;
    }

    public void setComboSkillDao(final ComboSkillDao comboSkillDao) {
        this.comboSkillDao = comboSkillDao;
    }

    public ComboSkill getComboSkill() {
        return comboSkill;
    }

    public void setComboSkill(final ComboSkill comboSkill) {
        this.comboSkill = comboSkill;
    }

    public List<ComboSkill> getDatas() {
        return datas;
    }

    public void setDatas(final List<ComboSkill> datas) {
        this.datas = datas;
    }

    public int[] getCardIds() {
        return cardIds;
    }

    public void setCardIds(final int[] cardIds) {
        this.cardIds = cardIds;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    public ComboSkillCardDao getComboSkillCardDao() {
        return comboSkillCardDao;
    }

    public void setComboSkillCardDao(final ComboSkillCardDao comboSkillCardDao) {
        this.comboSkillCardDao = comboSkillCardDao;
    }

    public File getIcon() {
        return icon;
    }

    public void setIcon(final File icon) {
        this.icon = icon;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public void setIconFileName(final String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public List<Card> getSavedCards() {
        return savedCards;
    }

    public void setSavedCards(final List<Card> savedCards) {
        this.savedCards = savedCards;
    }

}
