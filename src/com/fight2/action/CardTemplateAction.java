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
import com.fight2.model.BaseEntity;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.util.SummonHelper;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/card-template")
public class CardTemplateAction extends ActionSupport {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private SummonHelper summonHelper;
    private CardTemplate cardTemplate;
    private List<CardTemplate> datas;
    private int id;

    private File avatar1;
    private File avatar2;
    private File avatar3;
    private File avatar4;
    private String avatar1FileName;
    private String avatar2FileName;
    private String avatar3FileName;
    private String avatar4FileName;

    private File thumbImage1;
    private File thumbImage2;
    private File thumbImage3;
    private File thumbImage4;
    private String thumbImage1FileName;
    private String thumbImage2FileName;
    private String thumbImage3FileName;
    private String thumbImage4FileName;

    private File mainImage1;
    private File mainImage2;
    private File mainImage3;
    private File mainImage4;
    private String mainImage1FileName;
    private String mainImage2FileName;
    private String mainImage3FileName;
    private String mainImage4FileName;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "card_template_list.ftl") })
    public String list() {
        datas = cardTemplateDao.list();
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        final ActionContext context = ActionContext.getContext();
        final List<CardTemplate> list = cardTemplateDao.list();
        context.put("jsonMsg", new Gson().toJson(list));
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "card_template_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "card_template_form.ftl") })
    public String edit() {
        cardTemplate = cardTemplateDao.get(id);

        final List<CardImage> avatars = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_AVATAR, cardTemplate);
        final List<CardImage> mainImages = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_MAIN, cardTemplate);
        final List<CardImage> thumbs = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_THUMB, cardTemplate);
        cardTemplate.setAvatars(avatars);
        cardTemplate.setMainImages(mainImages);
        cardTemplate.setThumbImages(thumbs);

        return SUCCESS;
    }

    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String delete() {
        cardTemplate = cardTemplateDao.get(id);
        final List<CardImage> images = cardImageDao.listByCardTemplate(cardTemplate);
        for (final CardImage image : images) {
            cardImageDao.delete(image);
        }
        cardTemplateDao.delete(cardTemplate);
        return SUCCESS;
    }

    @Action(value = "save",
            interceptorRefs = {
                    @InterceptorRef(value = "fileUpload", params = { "allowedExtensions ", ".jpg,.png", "allowedTypes",
                            "image/png,image/jpeg,image/pjpeg" }), @InterceptorRef("tokenSession"), @InterceptorRef("basicStack") }, results = {
                    @Result(name = SUCCESS, location = "list", type = "redirect"), @Result(name = INPUT, location = "card_template_form.ftl") })
    public String save() {
        if (cardTemplate.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        cardTemplateDao.add(cardTemplate);
        saveImages();
        summonHelper.reLoadData();
        return SUCCESS;
    }

    private String editSave() {
        cardTemplateDao.update(cardTemplate);
        saveImages();
        summonHelper.reLoadData();
        return SUCCESS;
    }

    private void saveImages() {
        saveImage(1, avatar1, avatar1FileName, CardImage.TYPE_AVATAR);
        saveImage(2, avatar2, avatar2FileName, CardImage.TYPE_AVATAR);
        saveImage(3, avatar3, avatar3FileName, CardImage.TYPE_AVATAR);
        saveImage(4, avatar4, avatar4FileName, CardImage.TYPE_AVATAR);

        saveImage(1, mainImage1, mainImage1FileName, CardImage.TYPE_MAIN);
        saveImage(2, mainImage2, mainImage2FileName, CardImage.TYPE_MAIN);
        saveImage(3, mainImage3, mainImage3FileName, CardImage.TYPE_MAIN);
        saveImage(4, mainImage4, mainImage4FileName, CardImage.TYPE_MAIN);

        saveImage(1, thumbImage1, thumbImage1FileName, CardImage.TYPE_THUMB);
        saveImage(2, thumbImage2, thumbImage2FileName, CardImage.TYPE_THUMB);
        saveImage(3, thumbImage3, thumbImage3FileName, CardImage.TYPE_THUMB);
        saveImage(4, thumbImage4, thumbImage4FileName, CardImage.TYPE_THUMB);
    }

    private void saveImage(final int tier, final File image, final String fileName, final String type) {
        if (image != null) {
            final CardImage checkImage = cardImageDao.getByTypeTierAndCardTemplate(type, tier, cardTemplate);
            final ImageCommon imageCommon = new ImageCommon(image, fileName);
            final CardImage imageVo = imageCommon.doUpload();
            imageVo.setType(type);
            imageVo.setTier(tier);
            imageVo.setCardTemplate(cardTemplate);
            if (checkImage == null) {
                cardImageDao.add(imageVo);
            } else {
                checkImage.setUrl(imageVo.getUrl());
                checkImage.setWidth(imageVo.getWidth());
                checkImage.setHeight(imageVo.getHeight());
                cardImageDao.update(checkImage);
            }

        }
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

    public List<CardTemplate> getDatas() {
        return datas;
    }

    public void setDatas(final List<CardTemplate> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public File getAvatar1() {
        return avatar1;
    }

    public void setAvatar1(final File avatar1) {
        this.avatar1 = avatar1;
    }

    public File getAvatar2() {
        return avatar2;
    }

    public void setAvatar2(final File avatar2) {
        this.avatar2 = avatar2;
    }

    public File getAvatar3() {
        return avatar3;
    }

    public void setAvatar3(final File avatar3) {
        this.avatar3 = avatar3;
    }

    public File getAvatar4() {
        return avatar4;
    }

    public void setAvatar4(final File avatar4) {
        this.avatar4 = avatar4;
    }

    public String getAvatar1FileName() {
        return avatar1FileName;
    }

    public void setAvatar1FileName(final String avatar1FileName) {
        this.avatar1FileName = avatar1FileName;
    }

    public String getAvatar2FileName() {
        return avatar2FileName;
    }

    public void setAvatar2FileName(final String avatar2FileName) {
        this.avatar2FileName = avatar2FileName;
    }

    public String getAvatar3FileName() {
        return avatar3FileName;
    }

    public void setAvatar3FileName(final String avatar3FileName) {
        this.avatar3FileName = avatar3FileName;
    }

    public String getAvatar4FileName() {
        return avatar4FileName;
    }

    public void setAvatar4FileName(final String avatar4FileName) {
        this.avatar4FileName = avatar4FileName;
    }

    public File getThumbImage1() {
        return thumbImage1;
    }

    public void setThumbImage1(final File thumbImage1) {
        this.thumbImage1 = thumbImage1;
    }

    public File getThumbImage2() {
        return thumbImage2;
    }

    public void setThumbImage2(final File thumbImage2) {
        this.thumbImage2 = thumbImage2;
    }

    public File getThumbImage3() {
        return thumbImage3;
    }

    public void setThumbImage3(final File thumbImage3) {
        this.thumbImage3 = thumbImage3;
    }

    public File getThumbImage4() {
        return thumbImage4;
    }

    public void setThumbImage4(final File thumbImage4) {
        this.thumbImage4 = thumbImage4;
    }

    public String getThumbImage1FileName() {
        return thumbImage1FileName;
    }

    public void setThumbImage1FileName(final String thumbImage1FileName) {
        this.thumbImage1FileName = thumbImage1FileName;
    }

    public String getThumbImage2FileName() {
        return thumbImage2FileName;
    }

    public void setThumbImage2FileName(final String thumbImage2FileName) {
        this.thumbImage2FileName = thumbImage2FileName;
    }

    public String getThumbImage3FileName() {
        return thumbImage3FileName;
    }

    public void setThumbImage3FileName(final String thumbImage3FileName) {
        this.thumbImage3FileName = thumbImage3FileName;
    }

    public String getThumbImage4FileName() {
        return thumbImage4FileName;
    }

    public void setThumbImage4FileName(final String thumbImage4FileName) {
        this.thumbImage4FileName = thumbImage4FileName;
    }

    public File getMainImage1() {
        return mainImage1;
    }

    public void setMainImage1(final File mainImage1) {
        this.mainImage1 = mainImage1;
    }

    public File getMainImage2() {
        return mainImage2;
    }

    public void setMainImage2(final File mainImage2) {
        this.mainImage2 = mainImage2;
    }

    public File getMainImage3() {
        return mainImage3;
    }

    public void setMainImage3(final File mainImage3) {
        this.mainImage3 = mainImage3;
    }

    public File getMainImage4() {
        return mainImage4;
    }

    public void setMainImage4(final File mainImage4) {
        this.mainImage4 = mainImage4;
    }

    public String getMainImage1FileName() {
        return mainImage1FileName;
    }

    public void setMainImage1FileName(final String mainImage1FileName) {
        this.mainImage1FileName = mainImage1FileName;
    }

    public String getMainImage2FileName() {
        return mainImage2FileName;
    }

    public void setMainImage2FileName(final String mainImage2FileName) {
        this.mainImage2FileName = mainImage2FileName;
    }

    public String getMainImage3FileName() {
        return mainImage3FileName;
    }

    public void setMainImage3FileName(final String mainImage3FileName) {
        this.mainImage3FileName = mainImage3FileName;
    }

    public String getMainImage4FileName() {
        return mainImage4FileName;
    }

    public void setMainImage4FileName(final String mainImage4FileName) {
        this.mainImage4FileName = mainImage4FileName;
    }

    public SummonHelper getSummonHelper() {
        return summonHelper;
    }

    public void setSummonHelper(final SummonHelper summonHelper) {
        this.summonHelper = summonHelper;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
