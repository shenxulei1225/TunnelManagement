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
import cn.hutool.core.util.IdUtil;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FieldDefServiceImpl extends ServiceImpl<FieldDefMapper, FieldDefDO> implements FieldDefService {

    private final FieldDefMapper fieldDefMapper;
    private final FieldDefCategoryRelMapper relMapper;
    private final cn.iocoder.yudao.module.system.dal.mysql.region.FieldCategoryMapper categoryMapper;

    @Override
    @Transactional
    public Long createFieldDef(FieldDefCreateReqVO reqVO) {
        log.info("Create FieldDef request: {}", reqVO);
        FieldDefDO def = BeanUtils.toBean(reqVO, FieldDefDO.class);
        def.setFieldKey(IdUtil.fastSimpleUUID());
        fieldDefMapper.insert(def);
        // 关联分类
        if (reqVO.getCategoryIds() != null) {
            List<FieldDefCategoryRelDO> relList = reqVO.getCategoryIds().stream()
                    .map(cid -> new FieldDefCategoryRelDO().setFieldDefId(def.getId()).setCategoryId(cid))
                    .collect(Collectors.toList());
            relList.forEach(relMapper::insert);
        }
        log.info("Create FieldDef success, id={}", def.getId());
        return def.getId();
    }

    @Override
    @Transactional
    public boolean updateFieldDef(FieldDefUpdateReqVO reqVO) {
        log.info("Update FieldDef request: {}", reqVO);
        FieldDefDO def = BeanUtils.toBean(reqVO, FieldDefDO.class);
        FieldDefDO origin = fieldDefMapper.selectById(reqVO.getId());
        if (origin != null) {
            def.setFieldKey(origin.getFieldKey());
        }
        int updated = fieldDefMapper.updateById(def);
        // 更新关联
        relMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().eq("field_def_id", reqVO.getId()));
        if (reqVO.getCategoryIds() != null) {
            reqVO.getCategoryIds().forEach(cid -> relMapper.insert(new FieldDefCategoryRelDO().setFieldDefId(reqVO.getId()).setCategoryId(cid)));
        }
        log.info("Update FieldDef [{}] success: {}", reqVO.getId(), updated > 0);
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
        // 1. 取所有字段定义
        List<FieldDefDO> defs = fieldDefMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>());
        if (defs.isEmpty()) {
            return defs;
        }
        // 2. 查询关联
        List<Long> defIds = defs.stream().map(FieldDefDO::getId).toList();
        List<FieldDefCategoryRelDO> rels = relMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().in("field_def_id", defIds));
        // 3. 按字段分组收集全部分类
        java.util.Map<Long, java.util.List<Long>> catMap = rels.stream()
                .collect(java.util.stream.Collectors.groupingBy(FieldDefCategoryRelDO::getFieldDefId,
                        java.util.stream.Collectors.mapping(FieldDefCategoryRelDO::getCategoryId, java.util.stream.Collectors.toList())));
        // 4. 填充 categoryIds 字段
        defs.forEach(d -> d.setCategoryIds(catMap.getOrDefault(d.getId(), java.util.Collections.emptyList())));

        // 5. 按需过滤
        if (categoryId != null && categoryId > 0) {
            return defs.stream().filter(d -> d.getCategoryIds() != null && d.getCategoryIds().contains(categoryId)).toList();
        }
        return defs;
    }

    @Override
    public List<FieldDefDO> getFieldDefListByBizType(String bizType) {
        if (cn.hutool.core.util.StrUtil.isBlank(bizType)) {
            return getFieldDefListByCategory(null);
        }
        // 找到业务根分类
        cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO root = categoryMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO>()
                .eq(cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO::getParentId, 0L)
                .eq(cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO::getCode, bizType)
                .eq(cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO::getDeleted, false));
        if (root == null) {
            return java.util.Collections.emptyList();
        }
        // 获取该业务所有分类 id
        List<cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO> cats = categoryMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO>()
                .and(w -> w.eq(cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO::getId, root.getId())
                        .or().likeRight(cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO::getTreePath, root.getTreePath() + "/" + root.getId())));
        java.util.Set<Long> catIds = cats.stream().map(c -> c.getId()).collect(java.util.stream.Collectors.toSet());

        // 复用已有逻辑
        List<FieldDefDO> all = getFieldDefListByCategory(null);
        return all.stream().filter(d -> d.getCategoryIds() != null && d.getCategoryIds().stream().anyMatch(catIds::contains)).toList();
    }
}
