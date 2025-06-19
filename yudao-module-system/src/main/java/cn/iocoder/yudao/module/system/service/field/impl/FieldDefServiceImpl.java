package cn.iocoder.yudao.module.system.service.field.impl;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.field.FieldDefCategoryRelDO;
import cn.iocoder.yudao.module.system.dal.dataobject.field.FieldDefDO;
import cn.iocoder.yudao.module.system.dal.mysql.field.FieldDefCategoryRelMapper;
import cn.iocoder.yudao.module.system.dal.mysql.field.FieldDefMapper;
import cn.iocoder.yudao.module.system.service.field.FieldDefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldDefServiceImpl extends ServiceImpl<FieldDefMapper, FieldDefDO> implements FieldDefService {

    private final FieldDefMapper fieldDefMapper;
    private final FieldDefCategoryRelMapper relMapper;

    @Override
    @Transactional
    public Long createFieldDef(FieldDefCreateReqVO reqVO) {
        FieldDefDO def = BeanUtils.toBean(reqVO, FieldDefDO.class);
        fieldDefMapper.insert(def);
        // 关联分类
        if (reqVO.getCategoryIds() != null) {
            List<FieldDefCategoryRelDO> relList = reqVO.getCategoryIds().stream()
                    .map(cid -> new FieldDefCategoryRelDO().setFieldDefId(def.getId()).setCategoryId(cid))
                    .collect(Collectors.toList());
            relList.forEach(relMapper::insert);
        }
        return def.getId();
    }

    @Override
    @Transactional
    public boolean updateFieldDef(FieldDefUpdateReqVO reqVO) {
        FieldDefDO def = BeanUtils.toBean(reqVO, FieldDefDO.class);
        int updated = fieldDefMapper.updateById(def);
        // 更新关联
        relMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().eq("field_def_id", reqVO.getId()));
        if (reqVO.getCategoryIds() != null) {
            reqVO.getCategoryIds().forEach(cid -> relMapper.insert(new FieldDefCategoryRelDO().setFieldDefId(reqVO.getId()).setCategoryId(cid)));
        }
        return updated > 0;
    }

    @Override
    @Transactional
    public boolean deleteFieldDef(Long id) {
        int deleted = fieldDefMapper.deleteById(id);
        relMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().eq("field_def_id", id));
        return deleted > 0;
    }

    @Override
    public FieldDefDO getFieldDef(Long id) {
        return fieldDefMapper.selectById(id);
    }

    @Override
    public List<FieldDefDO> getFieldDefListByCategory(Long categoryId) {
        // 查询指定分类字段 + 通用字段（无关联记录）
        return fieldDefMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefDO>());

    }
}
