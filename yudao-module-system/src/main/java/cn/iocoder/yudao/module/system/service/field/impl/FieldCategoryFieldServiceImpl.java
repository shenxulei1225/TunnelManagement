package cn.iocoder.yudao.module.system.service.field.impl;

import cn.iocoder.yudao.module.system.dal.dataobject.field.FieldDefCategoryRelDO;
import cn.iocoder.yudao.module.system.dal.mysql.field.FieldDefCategoryRelMapper;
import cn.iocoder.yudao.module.system.service.field.FieldCategoryFieldService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldCategoryFieldServiceImpl implements FieldCategoryFieldService {

    private final FieldDefCategoryRelMapper mapper;

    @Override
    @Transactional
    public void save(Long categoryId, Long fieldId, boolean required, Integer sort) {
        LambdaQueryWrapper<FieldDefCategoryRelDO> qw = new LambdaQueryWrapper<FieldDefCategoryRelDO>()
                .eq(FieldDefCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldDefCategoryRelDO::getFieldDefId, fieldId);
        FieldDefCategoryRelDO rel = mapper.selectOne(qw);
        if (rel == null) {
            rel = new FieldDefCategoryRelDO();
            rel.setCategoryId(categoryId);
            rel.setFieldDefId(fieldId);
        }
        rel.setRequired(required);
        rel.setSort(sort == null ? 0 : sort);
        if (rel.getId() == null) {
            mapper.insert(rel);
        } else {
            mapper.updateById(rel);
        }
    }

    @Override
    public List<FieldDefCategoryRelDO> listByCategory(Long categoryId) {
        return mapper.selectList(new LambdaQueryWrapper<FieldDefCategoryRelDO>()
                .eq(FieldDefCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldDefCategoryRelDO::getDeleted, false)
                .orderByAsc(FieldDefCategoryRelDO::getSort));
    }

    @Override
    public void delete(Long categoryId, Long fieldId) {
        mapper.delete(new LambdaQueryWrapper<FieldDefCategoryRelDO>()
                .eq(FieldDefCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldDefCategoryRelDO::getFieldDefId, fieldId));
    }
}
