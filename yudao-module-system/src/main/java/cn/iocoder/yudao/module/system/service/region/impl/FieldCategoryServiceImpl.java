package cn.iocoder.yudao.module.system.service.region.impl;

import cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO;
import cn.iocoder.yudao.module.system.dal.mysql.region.FieldCategoryMapper;
import cn.iocoder.yudao.module.system.service.region.FieldCategoryService;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;

import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FieldCategoryServiceImpl implements FieldCategoryService {

    @Resource
    private FieldCategoryMapper categoryMapper;

    @Override
    public List<FieldCategoryDO> getCategoryTree() {
        LambdaQueryWrapper<FieldCategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(FieldCategoryDO::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(FieldCategoryDO bean) {
        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        categoryMapper.insert(bean);
        return bean.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategory(FieldCategoryDO bean) {
        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        return categoryMapper.updateById(bean) > 0;
    }

    @Override
    public Boolean deleteCategory(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }

    /**
     * 生成 treePath & level
     */
    private void fillTree(FieldCategoryDO bean) {
        if (bean.getParentId() == null) {
            bean.setParentId(0L);
        }
        if (bean.getParentId() == 0L) {
            bean.setTreePath("0");
            bean.setLevel(1);
        } else {
            FieldCategoryDO parent = categoryMapper.selectById(bean.getParentId());
            String parentPath = parent != null ? parent.getTreePath() : "0";
            bean.setTreePath(parentPath + "/" + bean.getParentId());
            bean.setLevel(parent != null ? parent.getLevel() + 1 : 2);
        }
    }

    /** 根据名称生成唯一编码 */
    private String genCode(String name) {
        String letters = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (letters.isEmpty()) {
            letters = "CAT";
        }
        if (letters.length() > 8) {
            letters = letters.substring(0, 8);
        }
        return letters + System.currentTimeMillis();
    }
}
