package cmsArt.cmsArt.biz.service.impl;

import com.github.pagehelper.PageHelper;
import cmsArt.cmsArt.biz.service.DictionaryService;
import cmsArt.cmsArt.biz.util.StringUtil;
import cmsArt.cmsArt.mapper.DictionaryMapper;
import cmsArt.cmsArt.model.annotation.CacheDelete;
import cmsArt.cmsArt.model.annotation.CacheDeleteAll;
import cmsArt.cmsArt.model.annotation.CacheGetOrSave;
import cmsArt.cmsArt.model.annotation.LogTime;
import cmsArt.cmsArt.model.constants.AppConstants;
import cmsArt.cmsArt.model.vo.Dictionary;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/20
 */
@Service
public class DictionaryServiceImpl extends BaseService<Dictionary> implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    @LogTime
    public List<Dictionary> searchDictionsries(int pageNum, String type, String value) {
        Example example = new Example(Dictionary.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotEmpty(type)) {
            criteria.andEqualTo("type", type);
        }

        if (StringUtils.isNotEmpty(value)) {
            criteria.andLike("value", StringUtil.toLikeString(value));
        }

        example.setOrderByClause("sort asc");

        PageHelper.startPage(pageNum, AppConstants.PAGE_SIZE);
        return super.selectByExample(example);
    }

    @Override
    @LogTime
    @CacheGetOrSave("dictionary:id:{0}")
    public Dictionary findDictionaryById(Long id) {
        return super.selectByPrimaryKey(id);
    }

    @Override
    @LogTime
    @CacheDelete("dictionary:id:{0:id}")
    @CacheDeleteAll("dictionary:type")
    public void updateDictionary(Dictionary dictionary) {
        super.updateByPrimaryKeySelective(dictionary);
    }

    @Override
    @LogTime
    @CacheDelete("dictionary:type:{0:type}")
    public void saveDictionary(Dictionary dictionary) {
        super.insertSelective(dictionary);
    }

    @Override
    @LogTime
    public boolean existsDictionaryCode(String code) {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode(code);

        return dictionaryMapper.selectCount(dictionary) == 1;
    }

    @Override
    @LogTime
    @CacheGetOrSave("dictionary:type:{0}")
    public List<Dictionary> findDictionariesByType(String type) {
        Example example = new Example(Dictionary.class);
        example.createCriteria().andEqualTo("type", type).andEqualTo("isDeleted", AppConstants.IS_DELETED_NO);

        example.setOrderByClause("sort asc");
        return super.selectByExample(example);
    }
}
